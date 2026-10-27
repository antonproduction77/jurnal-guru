package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.export.JournalExporter
import com.example.data.local.AppDatabase
import com.example.data.local.JournalEntry
import com.example.data.local.TeacherProfile
import com.example.data.local.TeachingSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed class ScreenDestination {
    object Home : ScreenDestination()
    object CreateJournal : ScreenDestination()
    object History : ScreenDestination()
    object Profile : ScreenDestination()
    data class DetailJournal(val entryId: Long) : ScreenDestination()
    object EditProfile : ScreenDestination()
    object ChangePassword : ScreenDestination()
    object ExportView : ScreenDestination()
}

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    private val dao = db.journalDao()

    // Navigation state
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Authentication & Active Profile
    val profile: StateFlow<TeacherProfile?> = dao.getProfileFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Demo Profile state (null if deleted)
    val demoProfile: StateFlow<TeacherProfile?> = dao.getDemoProfileFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // All registered profiles
    val allProfiles: StateFlow<List<TeacherProfile>> = dao.getAllProfilesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All journal entries
    val allEntries: StateFlow<List<JournalEntry>> = dao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All schedules
    val allSchedules: StateFlow<List<TeachingSchedule>> = dao.getAllSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // History filter states - Hanya Tingkat Kelas 7, 8, dan 9
    val searchQuery = MutableStateFlow("")
    val filterKelas = MutableStateFlow("Semua") // "Semua", "Kelas 7", "Kelas 8", "Kelas 9"
    val filterMapel = MutableStateFlow("Semua")
    val filterDateRange = MutableStateFlow("Semua") // "Semua", "Hari Ini", "Minggu Ini", "Bulan Ini"

    // Filtered entries flow
    val filteredEntries: StateFlow<List<JournalEntry>> = combine(
        allEntries,
        searchQuery,
        filterKelas,
        filterMapel,
        filterDateRange
    ) { entries, query, kelas, mapel, dateRange ->
        val startOfToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val startOfWeek = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        entries.filter { entry ->
            val matchesQuery = query.isBlank() ||
                    entry.materiTP.contains(query, ignoreCase = true) ||
                    entry.kelas.contains(query, ignoreCase = true) ||
                    entry.mataPelajaran.contains(query, ignoreCase = true) ||
                    entry.kegiatanPembelajaran.contains(query, ignoreCase = true) ||
                    entry.catatanKejadian.contains(query, ignoreCase = true) ||
                    entry.formattedDate.contains(query, ignoreCase = true)

            val matchesKelas = kelas == "Semua" || entry.kelas.equals(kelas, ignoreCase = true)
            val matchesMapel = mapel == "Semua" || entry.mataPelajaran.equals(mapel, ignoreCase = true)

            val matchesDate = when (dateRange) {
                "Hari Ini" -> entry.dateEpochMillis >= startOfToday
                "Minggu Ini" -> entry.dateEpochMillis >= startOfWeek
                "Bulan Ini" -> entry.dateEpochMillis >= startOfMonth
                else -> true
            }

            matchesQuery && matchesKelas && matchesMapel && matchesDate
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Form editing state
    val formEditingId = MutableStateFlow<Long?>(null)
    val formDateMillis = MutableStateFlow(System.currentTimeMillis())
    val formFormattedDate = MutableStateFlow(
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID")).format(Date())
    )
    val formJamKe = MutableStateFlow("Jam Ke-1 s/d 2 (07:30 - 08:50)")
    val formJumlahJp = MutableStateFlow(2) // Nilai jumlah jam pelajaran (1 JP = 40 menit)
    val formKelas = MutableStateFlow("Kelas 7") // Hanya tingkat Kelas 7, 8, dan 9
    val formMataPelajaran = MutableStateFlow("Bahasa Indonesia")
    val formMateriTP = MutableStateFlow("")
    val formKegiatanPembelajaran = MutableStateFlow("")
    val formHadirCount = MutableStateFlow(32)
    val formSakitCount = MutableStateFlow(0)
    val formIzinCount = MutableStateFlow(0)
    val formAlphaCount = MutableStateFlow(0)
    val formSiswaTidakHadir = MutableStateFlow("")
    val formCatatanKejadian = MutableStateFlow("")
    val formPhotoUri = MutableStateFlow<String?>(null)

    // User Feedback message
    val userMessage = MutableStateFlow<String?>(null)

    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
    }

    fun clearUserMessage() {
        userMessage.value = null
    }

    // Schedule prefill into Create Journal form
    fun startCreateJournalFromSchedule(schedule: TeachingSchedule) {
        resetJournalForm()
        formKelas.value = schedule.kelas
        formJumlahJp.value = schedule.jumlahJam
        formMataPelajaran.value = schedule.mataPelajaran
        formJamKe.value = "${schedule.jamKeLabel} (${schedule.waktu})"
        navigateTo(ScreenDestination.CreateJournal)
    }

    fun startEditJournal(entry: JournalEntry) {
        formEditingId.value = entry.id
        formDateMillis.value = entry.dateEpochMillis
        formFormattedDate.value = entry.formattedDate
        formJamKe.value = entry.jamKe
        formJumlahJp.value = entry.jumlahJp
        formKelas.value = entry.kelas
        formMataPelajaran.value = entry.mataPelajaran
        formMateriTP.value = entry.materiTP
        formKegiatanPembelajaran.value = entry.kegiatanPembelajaran
        formHadirCount.value = entry.hadirCount
        formSakitCount.value = entry.sakitCount
        formIzinCount.value = entry.izinCount
        formAlphaCount.value = entry.alphaCount
        formSiswaTidakHadir.value = entry.siswaTidakHadirKeterangan
        formCatatanKejadian.value = entry.catatanKejadian
        formPhotoUri.value = entry.photoUri
        navigateTo(ScreenDestination.CreateJournal)
    }

    fun resetJournalForm() {
        formEditingId.value = null
        formDateMillis.value = System.currentTimeMillis()
        formFormattedDate.value = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID")).format(Date())
        formJamKe.value = "Jam Ke-1 s/d 2 (07:30 - 08:50)"
        formJumlahJp.value = 2
        formKelas.value = "Kelas 7"
        formMataPelajaran.value = profile.value?.mapelUtama ?: "Bahasa Indonesia"
        formMateriTP.value = ""
        formKegiatanPembelajaran.value = ""
        formHadirCount.value = 32
        formSakitCount.value = 0
        formIzinCount.value = 0
        formAlphaCount.value = 0
        formSiswaTidakHadir.value = ""
        formCatatanKejadian.value = ""
        formPhotoUri.value = null
    }

    fun saveJournal(onSaved: () -> Unit) {
        if (formMateriTP.value.isBlank()) {
            userMessage.value = "Materi / TP pembelajaran wajib diisi!"
            return
        }
        if (formKegiatanPembelajaran.value.isBlank()) {
            userMessage.value = "Ringkasan kegiatan pembelajaran wajib diisi!"
            return
        }

        val entry = JournalEntry(
            id = formEditingId.value ?: 0L,
            dateEpochMillis = formDateMillis.value,
            formattedDate = formFormattedDate.value,
            jamKe = formJamKe.value,
            jumlahJp = formJumlahJp.value,
            kelas = formKelas.value,
            mataPelajaran = formMataPelajaran.value,
            materiTP = formMateriTP.value.trim(),
            kegiatanPembelajaran = formKegiatanPembelajaran.value.trim(),
            hadirCount = formHadirCount.value,
            sakitCount = formSakitCount.value,
            izinCount = formIzinCount.value,
            alphaCount = formAlphaCount.value,
            siswaTidakHadirKeterangan = formSiswaTidakHadir.value.trim(),
            catatanKejadian = formCatatanKejadian.value.trim(),
            photoUri = formPhotoUri.value,
            isDemoEntry = false
        )

        viewModelScope.launch {
            if (formEditingId.value != null && formEditingId.value != 0L) {
                dao.updateEntry(entry)
                userMessage.value = "Jurnal berhasil diperbarui!"
            } else {
                dao.insertEntry(entry)
                userMessage.value = "Jurnal berhasil disimpan!"
            }
            resetJournalForm()
            onSaved()
        }
    }

    fun deleteJournal(entry: JournalEntry, onDeleted: () -> Unit) {
        viewModelScope.launch {
            dao.deleteEntry(entry)
            userMessage.value = "Jurnal ${entry.kelas} berhasil dihapus."
            onDeleted()
        }
    }

    // --- Authentication & Account Management ---

    fun login(
        identifier: String,
        pass: String,
        remember: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val trimmedId = identifier.trim()
        val trimmedPass = pass.trim()

        if (trimmedId.isBlank() || trimmedPass.isBlank()) {
            onError("NIP / Email dan Password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            val found = dao.findProfileByIdentifier(trimmedId)
            if (found != null) {
                if (found.password == trimmedPass || (found.isDemo && trimmedPass == "guru12345")) {
                    dao.logoutAllProfiles()
                    dao.insertOrUpdateProfile(found.copy(isLoggedIn = true, rememberMe = remember))
                    userMessage.value = "Selamat datang, ${found.namaLengkap}!"
                    onSuccess()
                } else {
                    onError("Password salah. Silakan periksa kembali.")
                }
            } else {
                // If demo account exists and matches
                val demo = dao.getDemoProfileSync()
                if (demo != null && (trimmedId == demo.nip || trimmedId.equals(demo.email, ignoreCase = true))) {
                    if (trimmedPass == demo.password || trimmedPass == "guru12345") {
                        dao.logoutAllProfiles()
                        dao.insertOrUpdateProfile(demo.copy(isLoggedIn = true, rememberMe = remember))
                        userMessage.value = "Selamat datang, ${demo.namaLengkap}!"
                        onSuccess()
                    } else {
                        onError("Password salah.")
                    }
                } else {
                    onError("Akun guru dengan NIP/Email '$trimmedId' tidak ditemukan. Silakan daftar akun baru.")
                }
            }
        }
    }

    fun registerTeacher(
        nama: String,
        nip: String,
        email: String,
        mapel: String,
        sekolah: String,
        pass: String,
        confirmPass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val trimmedNama = nama.trim()
        val trimmedNip = nip.trim()
        val trimmedEmail = email.trim()
        val trimmedMapel = mapel.trim()
        val trimmedSekolah = sekolah.trim()
        val trimmedPass = pass.trim()

        if (trimmedNama.isBlank()) {
            onError("Nama lengkap guru beserta gelar wajib diisi!")
            return
        }
        if (trimmedNip.length < 5) {
            onError("NIP guru minimal 5 karakter!")
            return
        }
        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@")) {
            onError("Masukkan alamat email yang valid!")
            return
        }
        if (trimmedMapel.isBlank()) {
            onError("Pilih atau isi mata pelajaran utama!")
            return
        }
        if (trimmedSekolah.isBlank()) {
            onError("Nama satuan pendidikan / sekolah wajib diisi!")
            return
        }
        if (trimmedPass.length < 5) {
            onError("Kata sandi minimal 5 karakter!")
            return
        }
        if (trimmedPass != confirmPass.trim()) {
            onError("Konfirmasi kata sandi tidak cocok!")
            return
        }

        viewModelScope.launch {
            val existing = dao.findProfileByIdentifier(trimmedNip) ?: dao.findProfileByIdentifier(trimmedEmail)
            if (existing != null) {
                onError("Akun dengan NIP atau Email tersebut sudah terdaftar!")
                return@launch
            }

            // Logout any current session
            dao.logoutAllProfiles()

            // Insert new official teacher account with default Kepala Sekolah Anton Adiwijaya, S.Pd.
            val newTeacher = TeacherProfile(
                id = 0, // Auto-generate
                namaLengkap = trimmedNama,
                nip = trimmedNip,
                email = trimmedEmail,
                mapelUtama = trimmedMapel,
                namaSekolah = trimmedSekolah,
                namaKepalaSekolah = "Anton Adiwijaya, S.Pd.",
                nipKepalaSekolah = "197711052009041001",
                photoUri = null,
                password = trimmedPass,
                isLoggedIn = true,
                rememberMe = true,
                isDemo = false
            )

            dao.insertOrUpdateProfile(newTeacher)
            formMataPelajaran.value = trimmedMapel
            userMessage.value = "Akun Guru berhasil dibuat! Selamat datang, $trimmedNama."
            navigateTo(ScreenDestination.Home)
            onSuccess()
        }
    }

    fun deleteDemoAccount(onDeleted: () -> Unit) {
        viewModelScope.launch {
            val current = dao.getProfileSync()
            val wasDemoLoggedIn = current?.isDemo == true

            dao.deleteDemoProfile()
            dao.deleteDemoJournals()

            userMessage.value = "Akun demo dan data contoh berhasil dihapus."

            if (wasDemoLoggedIn) {
                dao.logoutAllProfiles()
            }
            onDeleted()
        }
    }

    fun logout() {
        viewModelScope.launch {
            dao.logoutAllProfiles()
            navigateTo(ScreenDestination.Home)
        }
    }

    fun updateProfile(
        nama: String,
        nip: String,
        email: String,
        mapel: String,
        sekolah: String,
        kepalaSekolah: String = "Anton Adiwijaya, S.Pd.",
        nipKepalaSekolah: String = "197711052009041001",
        photoUri: String? = null
    ) {
        viewModelScope.launch {
            val current = dao.getProfileSync() ?: TeacherProfile()
            val updated = current.copy(
                namaLengkap = nama.ifBlank { current.namaLengkap },
                nip = nip.ifBlank { current.nip },
                email = email.ifBlank { current.email },
                mapelUtama = mapel.ifBlank { current.mapelUtama },
                namaSekolah = sekolah.ifBlank { current.namaSekolah },
                namaKepalaSekolah = kepalaSekolah.ifBlank { current.namaKepalaSekolah },
                nipKepalaSekolah = nipKepalaSekolah.ifBlank { current.nipKepalaSekolah },
                photoUri = photoUri ?: current.photoUri
            )
            dao.insertOrUpdateProfile(updated)
            userMessage.value = "Profil guru dan data sekolah berhasil diperbarui!"
            navigateTo(ScreenDestination.Profile)
        }
    }

    fun changePassword(oldPass: String, newPass: String, confirmPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val current = dao.getProfileSync() ?: TeacherProfile()
            if (oldPass != current.password && (!current.isDemo || oldPass != "guru12345")) {
                onError("Password lama tidak sesuai!")
                return@launch
            }
            if (newPass.length < 5) {
                onError("Password baru minimal 5 karakter!")
                return@launch
            }
            if (newPass != confirmPass) {
                onError("Konfirmasi password baru tidak cocok!")
                return@launch
            }
            dao.insertOrUpdateProfile(current.copy(password = newPass))
            userMessage.value = "Password berhasil diubah!"
            onSuccess()
        }
    }

    fun exportAndShareCsv(onSuccess: (File) -> Unit) {
        val currentProfile = profile.value ?: TeacherProfile()
        val entries = allEntries.value
        if (entries.isEmpty()) {
            userMessage.value = "Belum ada data jurnal untuk diekspor."
            return
        }
        val file = JournalExporter.exportToCsv(getApplication(), currentProfile, entries)
        JournalExporter.shareCsvFile(getApplication(), file)
        onSuccess(file)
    }

    fun getPrintableHtml(): String {
        val currentProfile = profile.value ?: TeacherProfile()
        return JournalExporter.generatePrintableHtml(currentProfile, allEntries.value)
    }
}
