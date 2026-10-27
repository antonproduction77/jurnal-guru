package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Database(
    entities = [JournalEntry::class, TeacherProfile::class, TeachingSchedule::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jurnal_guru_smp.db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.journalDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: JournalDao) {
            // 1. Initial Teacher Profile with Kepala Sekolah
            val defaultProfile = TeacherProfile(
                id = 1,
                namaLengkap = "Dra. Hj. Siti Rahmawati, M.Pd.",
                nip = "198507122010012005",
                email = "siti.rahmawati@smpn1.sch.id",
                mapelUtama = "Bahasa Indonesia",
                namaSekolah = "SMP Negeri 1 Merdeka Belajar",
                namaKepalaSekolah = "Anton Adiwijaya, S.Pd.",
                nipKepalaSekolah = "197711052009041001",
                photoUri = null,
                password = "guru12345",
                isLoggedIn = true,
                rememberMe = true,
                isDemo = true
            )
            dao.insertOrUpdateProfile(defaultProfile)

            // 2. Initial Teaching Schedules (Kelas 7, 8, dan 9 - 1 JP = 40 Menit)
            val schedules = listOf(
                TeachingSchedule(
                    hari = "Senin",
                    jamKeLabel = "Jam Ke-1 & 2 (2 JP)",
                    jamKeRange = "1-2",
                    waktu = "07:30 - 08:50 (80 Menit)",
                    kelas = "Kelas 7",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Kelas 7",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Senin",
                    jamKeLabel = "Jam Ke-3 & 4 (2 JP)",
                    jamKeRange = "3-4",
                    waktu = "09:05 - 10:25 (80 Menit)",
                    kelas = "Kelas 8",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Kelas 8",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Selasa",
                    jamKeLabel = "Jam Ke-2 & 3 (2 JP)",
                    jamKeRange = "2-3",
                    waktu = "08:10 - 09:30 (80 Menit)",
                    kelas = "Kelas 8",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Kelas 8",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Selasa",
                    jamKeLabel = "Jam Ke-5 & 6 (2 JP)",
                    jamKeRange = "5-6",
                    waktu = "10:40 - 12:00 (80 Menit)",
                    kelas = "Kelas 9",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Kelas 9",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Rabu",
                    jamKeLabel = "Jam Ke-1 & 2 (2 JP)",
                    jamKeRange = "1-2",
                    waktu = "07:30 - 08:50 (80 Menit)",
                    kelas = "Kelas 7",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Laboratorium Bahasa",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Rabu",
                    jamKeLabel = "Jam Ke-7 & 8 (2 JP)",
                    jamKeRange = "7-8",
                    waktu = "12:40 - 14:00 (80 Menit)",
                    kelas = "Kelas 9",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Kelas 9",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Kamis",
                    jamKeLabel = "Jam Ke-3 & 4 (2 JP)",
                    jamKeRange = "3-4",
                    waktu = "09:05 - 10:25 (80 Menit)",
                    kelas = "Kelas 8",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Kelas 8",
                    jumlahJam = 2
                ),
                TeachingSchedule(
                    hari = "Jumat",
                    jamKeLabel = "Jam Ke-1 & 2 (2 JP)",
                    jamKeRange = "1-2",
                    waktu = "07:30 - 08:50 (80 Menit)",
                    kelas = "Kelas 9",
                    mataPelajaran = "Bahasa Indonesia",
                    ruang = "Ruang Perpustakaan",
                    jumlahJam = 2
                )
            )
            dao.insertSchedules(schedules)

            // 3. Initial Sample Journals (Tingkat Kelas 7, 8, dan 9)
            val cal = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))

            // Entry 1
            val todayDate = dateFormat.format(cal.time)
            dao.insertEntry(
                JournalEntry(
                    dateEpochMillis = cal.timeInMillis,
                    formattedDate = todayDate,
                    jamKe = "Jam Ke-1 s/d 2 (07:30 - 08:50)",
                    jumlahJp = 2,
                    kelas = "Kelas 7",
                    mataPelajaran = "Bahasa Indonesia",
                    materiTP = "Menganalisis Unsur Kebahasaan Teks Deskripsi (Kurikulum Merdeka Fase D)",
                    kegiatanPembelajaran = "1. Pembukaan dan apersepsi keindahan alam Nusantara.\n2. Diskusi kelompok mencari kata serapan dan majas asosiasi dalam teks deskripsi objek wisata.\n3. Presentasi perwakilan kelompok dan penguatan materi oleh guru.",
                    hadirCount = 31,
                    sakitCount = 1,
                    izinCount = 0,
                    alphaCount = 0,
                    siswaTidakHadirKeterangan = "Budi Pratama (Sakit flu, ada surat dokter)",
                    catatanKejadian = "Siswa sangat antusias mendeskripsikan tempat wisata lokal daerah.",
                    photoUri = null,
                    isDemoEntry = true
                )
            )

            // Entry 2 (1 day ago)
            cal.add(Calendar.DAY_OF_YEAR, -1)
            dao.insertEntry(
                JournalEntry(
                    dateEpochMillis = cal.timeInMillis,
                    formattedDate = dateFormat.format(cal.time),
                    jamKe = "Jam Ke-3 s/d 4 (09:05 - 10:25)",
                    jumlahJp = 2,
                    kelas = "Kelas 8",
                    mataPelajaran = "Bahasa Indonesia",
                    materiTP = "Menulis Teks Berita Fakta & Opini Berdasarkan Kejadian Lingkungan",
                    kegiatanPembelajaran = "1. Menyimak video liputan berita pendidikan aktual.\n2. Mengidentifikasi rumus 5W+1H (Adiksimba).\n3. Latihan menyusun kepala berita (lead) berkelompok dan koreksi silang antarteman.",
                    hadirCount = 30,
                    sakitCount = 1,
                    izinCount = 1,
                    alphaCount = 0,
                    siswaTidakHadirKeterangan = "Aulia Rahma (Izin lomba pramuka tingkat kwartir), Dinda (Sakit pusing)",
                    catatanKejadian = "Kelompok 3 berhasil menyusun kepala berita dengan sangat kreatif dan komunikatif.",
                    photoUri = null,
                    isDemoEntry = true
                )
            )

            // Entry 3 (2 days ago)
            cal.add(Calendar.DAY_OF_YEAR, -1)
            dao.insertEntry(
                JournalEntry(
                    dateEpochMillis = cal.timeInMillis,
                    formattedDate = dateFormat.format(cal.time),
                    jamKe = "Jam Ke-5 s/d 6 (10:40 - 12:00)",
                    jumlahJp = 2,
                    kelas = "Kelas 9",
                    mataPelajaran = "Bahasa Indonesia",
                    materiTP = "Menyajikan Pidato Persuasif Bertema Lingkungan Hidup & Kebersihan",
                    kegiatanPembelajaran = "1. Guru memodelkan teknik intonasi dan gestur berpidato.\n2. Praktik membaca naskah pidato singkat secara bergiliran di depan kelas.\n3. Teman sejawat memberikan rubrik penilaian apresiatif.",
                    hadirCount = 32,
                    sakitCount = 0,
                    izinCount = 0,
                    alphaCount = 0,
                    siswaTidakHadirKeterangan = "Semua siswa hadir lengkap.",
                    catatanKejadian = "Kehadiran 100%, seluruh siswa berpartisipasi aktif dalam penilaian rubrik.",
                    photoUri = null,
                    isDemoEntry = true
                )
            )
        }
    }
}
