package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.data.local.JournalEntry
import com.example.ui.theme.ColorAlpha
import com.example.ui.theme.ColorAlphaBg
import com.example.ui.theme.ColorHadir
import com.example.ui.theme.ColorHadirBg
import com.example.ui.theme.ColorIzin
import com.example.ui.theme.ColorIzinBg
import com.example.ui.theme.ColorSakit
import com.example.ui.theme.ColorSakitBg
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailJournalScreen(
    entry: JournalEntry?,
    onBack: () -> Unit,
    onEdit: (JournalEntry) -> Unit,
    onDelete: (JournalEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (entry == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Jurnal tidak ditemukan.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onBack) { Text("Kembali") }
            }
        }
        return
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rincian Jurnal KBM",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_detail")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEdit(entry) },
                        modifier = Modifier.testTag("btn_edit_from_detail")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Jurnal", tint = Color.White)
                    }
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.testTag("btn_delete_from_detail")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus Jurnal", tint = Color(0xFFFECACA))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyPrimary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavyPrimary,
                            modifier = Modifier.size(width = 54.dp, height = 40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = entry.kelas,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF6FF)
                        ) {
                            Text(
                                text = entry.mataPelajaran,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = entry.formattedDate,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = entry.jamKe,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = "${entry.jumlahJp} Jam Pelajaran (${entry.totalMenit} Menit) • Standar 1 JP = 40 Menit",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = NavyPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Materi & Tujuan Pembelajaran Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Materi / Tujuan Pembelajaran (TP)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = entry.materiTP,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Ringkasan Kegiatan Pembelajaran (KBM)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = entry.kegiatanPembelajaran,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Attendance Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Presensi Siswa",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyPrimary
                        )
                        Text(
                            text = "Tingkat Kehadiran: ${String.format("%.1f", entry.persentaseHadir)}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorHadir
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AttendanceSummaryBox("Hadir", entry.hadirCount, ColorHadir, ColorHadirBg, Modifier.weight(1f))
                        AttendanceSummaryBox("Sakit", entry.sakitCount, ColorSakit, ColorSakitBg, Modifier.weight(1f))
                        AttendanceSummaryBox("Izin", entry.izinCount, ColorIzin, ColorIzinBg, Modifier.weight(1f))
                        AttendanceSummaryBox("Alpha", entry.alphaCount, ColorAlpha, ColorAlphaBg, Modifier.weight(1f))
                    }

                    if (entry.siswaTidakHadirKeterangan.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Nama Siswa Tidak Hadir & Alasan:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = entry.siswaTidakHadirKeterangan,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Catatan Kejadian Penting
            if (entry.catatanKejadian.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Catatan Kejadian / Perilaku Siswa",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = entry.catatanKejadian,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Foto Dokumentasi
            if (entry.photoUri != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Foto Dokumentasi Pembelajaran",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = entry.photoUri,
                                contentDescription = "Dokumentasi Pembelajaran",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { showDeleteConfirmDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Hapus")
                }

                Button(
                    onClick = { onEdit(entry) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit Jurnal")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Konfirmasi Hapus Jurnal") },
            text = { Text("Apakah Anda yakin ingin menghapus jurnal kelas ${entry.kelas} pada tanggal ${entry.formattedDate}?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDelete(entry)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Ya, Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun AttendanceSummaryBox(
    label: String,
    count: Int,
    textColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = textColor
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
        }
    }
}
