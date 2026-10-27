package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.JournalEntry
import com.example.ui.theme.ColorAlpha
import com.example.ui.theme.ColorHadir
import com.example.ui.theme.ColorHadirBg
import com.example.ui.theme.ColorIzin
import com.example.ui.theme.ColorSakit
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.viewmodel.JournalViewModel

@Composable
fun HistoryScreen(
    viewModel: JournalViewModel,
    onViewDetail: (Long) -> Unit,
    onEditEntry: (JournalEntry) -> Unit,
    onOpenPrintPreview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entries by viewModel.filteredEntries.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedKelas by viewModel.filterKelas.collectAsState()
    val selectedDateRange by viewModel.filterDateRange.collectAsState()

    var entryToDelete by remember { mutableStateOf<JournalEntry?>(null) }

    val dateRanges = listOf("Semua", "Hari Ini", "Minggu Ini", "Bulan Ini")
    // Tingkat kelas dibatasi hanya Kelas 7, 8, dan 9
    val kelasOptions = listOf("Semua", "Kelas 7", "Kelas 8", "Kelas 9")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            color = NavyPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Rekap & Riwayat Jurnal",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Laporan KBM SMP • 1 JP = 40 Menit",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${entries.size} Jurnal",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Cari materi, kelas, tanggal, kegiatan...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Cari", tint = Color.White)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Hapus", tint = Color.White)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.12f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        cursorColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar_history")
                )
            }
        }

        // Export Actions & Filter Bar
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            // Two Export Buttons (Excel/CSV & PDF/Print)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.exportAndShareCsv {} },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("btn_export_excel")
                ) {
                    Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ekspor Excel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onOpenPrintPreview,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("btn_cetak_pdf")
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cetak / PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Date Range chips
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Waktu: ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(dateRanges) { range ->
                        val isSelected = range == selectedDateRange
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.filterDateRange.value = range },
                            label = { Text(range, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Filter Class chips - Tingkat Kelas 7, 8, dan 9
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Tingkat: ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(kelasOptions) { k ->
                        val isSelected = k == selectedKelas
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.filterKelas.value = k },
                            label = { Text(k, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavySecondary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // List of filtered journal entries
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.HistoryEdu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tidak ada jurnal yang sesuai filter",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Coba ubah kata kunci atau pilih filter 'Semua'.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("history_journal_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    JournalHistoryCard(
                        entry = entry,
                        onClick = { onViewDetail(entry.id) },
                        onEdit = { onEditEntry(entry) },
                        onDelete = { entryToDelete = entry }
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    if (entryToDelete != null) {
        val entry = entryToDelete!!
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text("Hapus Jurnal Mengajar") },
            text = {
                Text("Apakah Anda yakin ingin menghapus jurnal ${entry.kelas} pada tanggal ${entry.formattedDate}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteJournal(entry) {
                            entryToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Ya, Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { entryToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun JournalHistoryCard(
    entry: JournalEntry,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("journal_card_${entry.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Class badge + Jam & JP + Overflow Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NavyPrimary,
                        modifier = Modifier.size(width = 64.dp, height = 30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = entry.kelas,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = entry.formattedDate,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Overflow Menu for Edit & Delete
                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu Aksi Jurnal",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Jurnal") },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = NavyPrimary) },
                            onClick = {
                                menuExpanded = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Hapus Jurnal", color = Color(0xFFDC2626)) },
                            leadingIcon = { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFDC2626)) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Time & JP Badge (1 JP = 40 Menit)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = entry.jamKe,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEFF6FF)
                ) {
                    Text(
                        text = "${entry.jumlahJp} JP (${entry.totalMenit} Menit)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NavyPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Materi / TP
            Text(
                text = entry.materiTP,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Kegiatan summary
            Text(
                text = entry.kegiatanPembelajaran,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Attendance row & badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Hadir Pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ColorHadirBg
                    ) {
                        Text(
                            text = "H: ${entry.hadirCount}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorHadir,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (entry.sakitCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF9C3)
                        ) {
                            Text(
                                text = "S: ${entry.sakitCount}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = ColorSakit,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (entry.izinCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE0F2FE)
                        ) {
                            Text(
                                text = "I: ${entry.izinCount}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = ColorIzin,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (entry.alphaCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEE2E2)
                        ) {
                            Text(
                                text = "A: ${entry.alphaCount}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = ColorAlpha,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Direct Quick Action Buttons: Edit & Hapus
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(
                        onClick = onEdit,
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = NavySecondary)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Edit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavySecondary)
                    }

                    TextButton(
                        onClick = onDelete,
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Hapus", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                }
            }
        }
    }
}
