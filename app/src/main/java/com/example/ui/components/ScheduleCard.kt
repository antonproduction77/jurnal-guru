package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TeachingSchedule
import com.example.ui.theme.ColorHadir
import com.example.ui.theme.ColorHadirBg
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary

@Composable
fun ScheduleCard(
    schedule: TeachingSchedule,
    isAlreadyFilled: Boolean,
    onFillJournal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("schedule_card_${schedule.kelas}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Class Badge & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NavyPrimary,
                        modifier = Modifier.size(width = 68.dp, height = 36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = schedule.kelas,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = schedule.jamKeLabel,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = schedule.waktu,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Status Chip
                if (isAlreadyFilled) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ColorHadirBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ColorHadir,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sudah Diisi",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = ColorHadir
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "Belum Diisi",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFFB45309)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subject and room
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = schedule.mataPelajaran,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = schedule.ruang,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action button
            if (isAlreadyFilled) {
                OutlinedButton(
                    onClick = onFillJournal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("btn_edit_schedule_${schedule.kelas}"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tambah Sesi / Edit Jurnal Kelas ${schedule.kelas}", fontSize = 13.sp)
                }
            } else {
                Button(
                    onClick = onFillJournal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("btn_fill_schedule_${schedule.kelas}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Isi Jurnal KBM Kelas ${schedule.kelas}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
