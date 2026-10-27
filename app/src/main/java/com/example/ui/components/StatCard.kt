package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EducationAmber
import com.example.ui.theme.EducationTeal
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary

@Composable
fun TeacherStatsRow(
    jamMingguIni: Int,
    jurnalBulanIni: Int,
    persentaseKehadiran: String,
    jurnalHariIni: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Jam Minggu Ini",
                value = "$jamMingguIni Jam",
                subtitle = "Target: 24 Jam",
                icon = Icons.Default.AccessTime,
                iconTint = NavyPrimary,
                bgColor = Color(0xFFEFF6FF),
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Jurnal Bulan Ini",
                value = "$jurnalBulanIni Jurnal",
                subtitle = "Tercatat di sistem",
                icon = Icons.Default.AssignmentTurnedIn,
                iconTint = EducationTeal,
                bgColor = Color(0xFFF0FDFA),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Presensi Siswa",
                value = persentaseKehadiran,
                subtitle = "Rata-rata hadir",
                icon = Icons.Default.Groups,
                iconTint = NavySecondary,
                bgColor = Color(0xFFEEF2FF),
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Jurnal Hari Ini",
                value = "$jurnalHariIni Sesi",
                subtitle = if (jurnalHariIni > 0) "Terisi" else "Belum terisi",
                icon = Icons.Default.Today,
                iconTint = EducationAmber,
                bgColor = Color(0xFFFFFBEB),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = bgColor,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
