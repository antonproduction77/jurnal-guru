package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.JournalEntry
import com.example.data.local.TeacherProfile
import com.example.data.local.TeachingSchedule
import com.example.ui.components.ScheduleCard
import com.example.ui.components.TeacherStatsRow
import com.example.ui.theme.ColorHadir
import com.example.ui.theme.ColorHadirBg
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    profile: TeacherProfile?,
    allEntries: List<JournalEntry>,
    allSchedules: List<TeachingSchedule>,
    onAddJournalClick: () -> Unit,
    onFillScheduleClick: (TeachingSchedule) -> Unit,
    onViewEntryClick: (Long) -> Unit,
    onViewAllHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")
    
    // Determine current day of week in Indonesian
    val currentDayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val todayIndonesianName = when (currentDayIndex) {
        Calendar.MONDAY -> "Senin"
        Calendar.TUESDAY -> "Selasa"
        Calendar.WEDNESDAY -> "Rabu"
        Calendar.THURSDAY -> "Kamis"
        Calendar.FRIDAY -> "Jumat"
        else -> "Senin"
    }

    var selectedDay by remember { mutableStateOf(todayIndonesianName) }

    val schedulesForSelectedDay = remember(allSchedules, selectedDay) {
        allSchedules.filter { it.hari.equals(selectedDay, ignoreCase = true) }
    }

    // Statistics calculations
    val totalHoursThisWeek = remember(allSchedules) {
        allSchedules.sumOf { it.jumlahJam }
    }

    val totalEntriesThisMonth = remember(allEntries) {
        val cal = Calendar.getInstance()
        val currentMonth = cal.get(Calendar.MONTH)
        val currentYear = cal.get(Calendar.YEAR)
        allEntries.count { entry ->
            val entryCal = Calendar.getInstance().apply { timeInMillis = entry.dateEpochMillis }
            entryCal.get(Calendar.MONTH) == currentMonth && entryCal.get(Calendar.YEAR) == currentYear
        }
    }

    val todayEntries = remember(allEntries) {
        val todayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        allEntries.filter { entry ->
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(entry.dateEpochMillis)) == todayStr
        }
    }

    val avgAttendancePercent = remember(allEntries) {
        if (allEntries.isEmpty()) {
            "100%"
        } else {
            val totalPresent = allEntries.sumOf { it.hadirCount }
            val totalAll = allEntries.sumOf { it.totalSiswa }
            if (totalAll > 0) {
                String.format(Locale.US, "%.1f%%", (totalPresent.toFloat() / totalAll) * 100f)
            } else {
                "100%"
            }
        }
    }

    val teacherName = profile?.namaLengkap ?: "Bapak/Ibu Guru"
    val todayFormatted = remember {
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID")).format(Date())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Teacher Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyPrimary)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Foto Guru",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Selamat Datang,",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = teacherName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    ),
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Guru SMP",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date & School Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = todayFormatted,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = Color.White
                                )
                            }

                            Text(
                                text = profile?.mapelUtama ?: "Bahasa Indonesia",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF93C5FD)
                            )
                        }
                    }
                }
            }
        }

        // Quick Stats Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ringkasan Statistik KBM",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                TeacherStatsRow(
                    jamMingguIni = totalHoursThisWeek,
                    jurnalBulanIni = totalEntriesThisMonth,
                    persentaseKehadiran = avgAttendancePercent,
                    jurnalHariIni = todayEntries.size
                )
            }
        }

        // Prominent CTA: Tambah Jurnal Hari Ini
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Catat KBM Kelas Hari Ini",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Isi jurnal pembelajaran, kehadiran siswa, dan catatan penting.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onAddJournalClick,
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_tambah_jurnal_home")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Buat Jurnal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Jadwal Mengajar Hari Ini
        item {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Jadwal Mengajar Guru",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${schedulesForSelectedDay.size} Sesi Jadwal",
                        style = MaterialTheme.typography.bodySmall,
                        color = NavySecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day selector chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(daysOfWeek) { day ->
                        val isSelected = day == selectedDay
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDay = day },
                            label = {
                                Text(
                                    text = if (day == todayIndonesianName) "$day (Hari Ini)" else day,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Schedules items
        if (schedulesForSelectedDay.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Tidak ada jadwal mengajar pada hari $selectedDay.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(schedulesForSelectedDay) { schedule ->
                val isFilled = remember(allEntries, schedule) {
                    allEntries.any { it.kelas.equals(schedule.kelas, ignoreCase = true) }
                }

                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    ScheduleCard(
                        schedule = schedule,
                        isAlreadyFilled = isFilled,
                        onFillJournal = { onFillScheduleClick(schedule) }
                    )
                }
            }
        }

        // Section: Riwayat Jurnal Terakhir
        item {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Riwayat Jurnal Terkini",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = onViewAllHistoryClick) {
                        Text(
                            text = "Lihat Semua",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = NavySecondary
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = NavySecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        val recentEntries = allEntries.take(3)
        if (recentEntries.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Belum ada jurnal yang dicatat",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(recentEntries) { entry ->
                RecentJournalItem(
                    entry = entry,
                    onClick = { onViewEntryClick(entry.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun RecentJournalItem(
    entry: JournalEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("recent_journal_${entry.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NavyPrimary,
                    modifier = Modifier.size(width = 64.dp, height = 36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = entry.kelas,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ColorHadirBg
                    ) {
                        Text(
                            text = "${entry.hadirCount} Hadir",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorHadir,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = entry.materiTP,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = entry.kegiatanPembelajaran,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Buka detail",
                tint = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
