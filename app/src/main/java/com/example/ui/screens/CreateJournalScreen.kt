package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ColorAlpha
import com.example.ui.theme.ColorAlphaBg
import com.example.ui.theme.ColorHadir
import com.example.ui.theme.ColorHadirBg
import com.example.ui.theme.ColorIzin
import com.example.ui.theme.ColorIzinBg
import com.example.ui.theme.ColorSakit
import com.example.ui.theme.ColorSakitBg
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.viewmodel.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateJournalScreen(
    viewModel: JournalViewModel,
    onSavedSuccessfully: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val editingId by viewModel.formEditingId.collectAsState()
    val formattedDate by viewModel.formFormattedDate.collectAsState()
    val jamKe by viewModel.formJamKe.collectAsState()
    val jumlahJp by viewModel.formJumlahJp.collectAsState()
    val kelas by viewModel.formKelas.collectAsState()
    val mataPelajaran by viewModel.formMataPelajaran.collectAsState()
    val materiTP by viewModel.formMateriTP.collectAsState()
    val kegiatanPembelajaran by viewModel.formKegiatanPembelajaran.collectAsState()
    val hadirCount by viewModel.formHadirCount.collectAsState()
    val sakitCount by viewModel.formSakitCount.collectAsState()
    val izinCount by viewModel.formIzinCount.collectAsState()
    val alphaCount by viewModel.formAlphaCount.collectAsState()
    val siswaTidakHadir by viewModel.formSiswaTidakHadir.collectAsState()
    val catatanKejadian by viewModel.formCatatanKejadian.collectAsState()
    val photoUri by viewModel.formPhotoUri.collectAsState()

    // Jam list
    val jamList = listOf(
        "Jam Ke-1 (07:30 - 08:10)",
        "Jam Ke-1 s/d 2 (07:30 - 08:50)",
        "Jam Ke-2 & 3 (08:10 - 09:30)",
        "Jam Ke-3 & 4 (09:05 - 10:25)",
        "Jam Ke-5 & 6 (10:40 - 12:00)",
        "Jam Ke-7 & 8 (12:40 - 14:00)",
        "Jam Ke-9 & 10 (14:00 - 15:20)"
    )

    // Tingkat kelas hanya Kelas 7, 8, dan 9
    val kelasList = listOf("Kelas 7", "Kelas 8", "Kelas 9")

    val mapelList = listOf(
        "Bahasa Indonesia",
        "Matematika",
        "Ilmu Pengetahuan Alam (IPA)",
        "Ilmu Pengetahuan Sosial (IPS)",
        "Bahasa Inggris",
        "Pendidikan Pancasila (PPKn)",
        "Pendidikan Agama & Budi Pekerti",
        "Seni Budaya",
        "Informatika",
        "Pendidikan Jasmani & Olahraga (PJOK)"
    )

    var jamExpanded by remember { mutableStateOf(false) }
    var kelasExpanded by remember { mutableStateOf(false) }
    var mapelExpanded by remember { mutableStateOf(false) }

    // Android Photo Picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.formPhotoUri.value = uri.toString()
        }
    }

    // Quick Kurikulum Merdeka suggestions
    val quickTopics = listOf(
        "Teks Deskripsi & Objek Wisata",
        "Teks Cerita Fantasi",
        "Teks Prosedur & Praktik",
        "Puisi Rakyat & Pantun",
        "Struktur Teks Berita (Adiksimba)",
        "Pidato Persuasif",
        "Resensi Buku Fiksi & Nonfiksi"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = NavyPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.EditCalendar,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (editingId != null) "Edit Jurnal Mengajar" else "Input Jurnal Mengajar Harian",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Catatan KBM SMP • 1 JP = 40 Menit",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Section 1: Informasi Waktu, Jam Pelajaran & Tingkat Kelas
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "1. Waktu, Jam Pelajaran & Tingkat Kelas",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NavyPrimary
                )

                // Tanggal
                OutlinedTextField(
                    value = formattedDate,
                    onValueChange = { viewModel.formFormattedDate.value = it },
                    label = { Text("Tanggal Pelaksanaan KBM") },
                    leadingIcon = {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = NavyPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_tanggal")
                )

                // Jam Ke Dropdown
                ExposedDropdownMenuBox(
                    expanded = jamExpanded,
                    onExpandedChange = { jamExpanded = !jamExpanded }
                ) {
                    OutlinedTextField(
                        value = jamKe,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jam Pelajaran Ke- (SMP)") },
                        leadingIcon = {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = NavyPrimary)
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jamExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("dropdown_jam_ke")
                    )

                    ExposedDropdownMenu(
                        expanded = jamExpanded,
                        onDismissRequest = { jamExpanded = false }
                    ) {
                        jamList.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.formJamKe.value = option
                                    jamExpanded = false
                                }
                            )
                        }
                    }
                }

                // Field Jumlah Jam Pelajaran (1 JP = 40 Menit)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = NavyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Jumlah Jam Pelajaran (JP):",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF)
                        ) {
                            Text(
                                text = "$jumlahJp JP (${jumlahJp * 40} Menit)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NavyPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Standar SMP: Per 1 jam pelajaran (JP) = 40 menit",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1, 2, 3, 4).forEach { jp ->
                            val isSelected = jumlahJp == jp
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.formJumlahJp.value = jp },
                                label = { Text("$jp JP (${jp * 40}m)", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Tingkat Kelas (Hanya Kelas 7, 8, dan 9) & Mata Pelajaran
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Kelas Dropdown
                    ExposedDropdownMenuBox(
                        expanded = kelasExpanded,
                        onExpandedChange = { kelasExpanded = !kelasExpanded },
                        modifier = Modifier.weight(0.45f)
                    ) {
                        OutlinedTextField(
                            value = kelas,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tingkat Kelas") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kelasExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("dropdown_kelas")
                        )

                        ExposedDropdownMenu(
                            expanded = kelasExpanded,
                            onDismissRequest = { kelasExpanded = false }
                        ) {
                            kelasList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontWeight = FontWeight.SemiBold) },
                                    onClick = {
                                        viewModel.formKelas.value = option
                                        kelasExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Mata Pelajaran Dropdown
                    ExposedDropdownMenuBox(
                        expanded = mapelExpanded,
                        onExpandedChange = { mapelExpanded = !mapelExpanded },
                        modifier = Modifier.weight(0.55f)
                    ) {
                        OutlinedTextField(
                            value = mataPelajaran,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Mata Pelajaran") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mapelExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("dropdown_mapel")
                        )

                        ExposedDropdownMenu(
                            expanded = mapelExpanded,
                            onDismissRequest = { mapelExpanded = false }
                        ) {
                            mapelList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.formMataPelajaran.value = option
                                        mapelExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Materi & Kegiatan Pembelajaran
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "2. Materi / TP & Kegiatan Pembelajaran",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NavyPrimary
                )

                // Quick suggestions chips
                Text(
                    text = "Pilih Cepat Contoh Topik Kurikulum Merdeka:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickTopics.forEach { topic ->
                        FilterChip(
                            selected = materiTP.contains(topic),
                            onClick = { viewModel.formMateriTP.value = topic },
                            label = { Text(topic, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Materi / TP (Tujuan Pembelajaran)
                OutlinedTextField(
                    value = materiTP,
                    onValueChange = { viewModel.formMateriTP.value = it },
                    label = { Text("Materi / Tujuan Pembelajaran (TP) *") },
                    placeholder = { Text("Contoh: Menganalisis Unsur Kebahasaan Teks Deskripsi (Kurikulum Merdeka Fase D)") },
                    minLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_materi_tp")
                )

                // Kegiatan Pembelajaran
                OutlinedTextField(
                    value = kegiatanPembelajaran,
                    onValueChange = { viewModel.formKegiatanPembelajaran.value = it },
                    label = { Text("Kegiatan Pembelajaran (Aktivitas Kelas) *") },
                    placeholder = { Text("1. Pembukaan & apersepsi\n2. Eksplorasi materi & diskusi kelompok\n3. Presentasi karya dan asesmen formatif") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_kegiatan_pembelajaran")
                )
            }
        }

        // Section 3: Catatan Kehadiran Siswa
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "3. Presensi & Kehadiran Siswa",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary
                    )

                    val total = hadirCount + sakitCount + izinCount + alphaCount
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = "Total: $total Siswa",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = NavyPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Counters grid (Hadir, Sakit, Izin, Alpha)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AttendanceStepper(
                        label = "Hadir",
                        count = hadirCount,
                        color = ColorHadir,
                        bgColor = ColorHadirBg,
                        onIncrement = { viewModel.formHadirCount.value = hadirCount + 1 },
                        onDecrement = { if (hadirCount > 0) viewModel.formHadirCount.value = hadirCount - 1 },
                        modifier = Modifier.weight(1f)
                    )

                    AttendanceStepper(
                        label = "Sakit",
                        count = sakitCount,
                        color = ColorSakit,
                        bgColor = ColorSakitBg,
                        onIncrement = { viewModel.formSakitCount.value = sakitCount + 1 },
                        onDecrement = { if (sakitCount > 0) viewModel.formSakitCount.value = sakitCount - 1 },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AttendanceStepper(
                        label = "Izin",
                        count = izinCount,
                        color = ColorIzin,
                        bgColor = ColorIzinBg,
                        onIncrement = { viewModel.formIzinCount.value = izinCount + 1 },
                        onDecrement = { if (izinCount > 0) viewModel.formIzinCount.value = izinCount - 1 },
                        modifier = Modifier.weight(1f)
                    )

                    AttendanceStepper(
                        label = "Alpha",
                        count = alphaCount,
                        color = ColorAlpha,
                        bgColor = ColorAlphaBg,
                        onIncrement = { viewModel.formAlphaCount.value = alphaCount + 1 },
                        onDecrement = { if (alphaCount > 0) viewModel.formAlphaCount.value = alphaCount - 1 },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Kolom teks khusus: Nama Siswa Tidak Hadir & Keterangan
                OutlinedTextField(
                    value = siswaTidakHadir,
                    onValueChange = { viewModel.formSiswaTidakHadir.value = it },
                    label = { Text("Nama Siswa Tidak Hadir & Keterangan") },
                    placeholder = { Text("Contoh: Dimas (Sakit flu, surat dokter), Rian (Izin lomba pramuka)") },
                    minLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_siswa_tidak_hadir")
                )
            }
        }

        // Section 4: Catatan Kejadian Penting (Opsional)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "4. Catatan Kejadian / Perilaku Siswa (Opsional)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NavyPrimary
                )

                OutlinedTextField(
                    value = catatanKejadian,
                    onValueChange = { viewModel.formCatatanKejadian.value = it },
                    label = { Text("Catatan Perkembangan / Kejadian Penting") },
                    placeholder = { Text("Contoh: Siswa aktif berdiskusi kelompok, materi tersampaikan tepat waktu.") },
                    minLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_catatan_kejadian")
                )
            }
        }

        // Section 5: Foto Dokumentasi Pembelajaran
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "5. Foto Dokumentasi KBM (Opsional)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary
                    )

                    if (photoUri != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ColorHadirBg
                        ) {
                            Text(
                                text = "Foto Terpasang",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = ColorHadir,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (photoUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "Foto Dokumentasi",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        IconButton(
                            onClick = { viewModel.formPhotoUri.value = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus foto",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_upload_foto")
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = NavyPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (photoUri != null) "Ganti Foto Dokumentasi" else "Ambil / Pilih Foto Dokumentasi Pembelajaran",
                        color = NavyPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Action Buttons: Save & Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.resetJournalForm() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(0.35f)
                    .height(50.dp)
                    .testTag("btn_reset_form")
            ) {
                Text("Reset")
            }

            Button(
                onClick = {
                    viewModel.saveJournal(onSaved = onSavedSuccessfully)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                modifier = Modifier
                    .weight(0.65f)
                    .height(50.dp)
                    .testTag("btn_simpan_jurnal")
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (editingId != null) "Simpan Perubahan" else "Simpan Jurnal KBM",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun AttendanceStepper(
    label: String,
    count: Int,
    color: Color,
    bgColor: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = color
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                ),
                color = color
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onDecrement,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Kurang $label",
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah $label",
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
