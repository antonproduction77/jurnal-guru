package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun TeacherBottomNavBar(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val isHome = currentScreen is ScreenDestination.Home
        val isCreate = currentScreen is ScreenDestination.CreateJournal
        val isHistory = currentScreen is ScreenDestination.History || currentScreen is ScreenDestination.DetailJournal
        val isProfile = currentScreen is ScreenDestination.Profile || currentScreen is ScreenDestination.EditProfile || currentScreen is ScreenDestination.ChangePassword

        NavigationBarItem(
            selected = isHome,
            onClick = { onNavigate(ScreenDestination.Home) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Beranda"
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("bottom_nav_home")
        )

        NavigationBarItem(
            selected = isCreate,
            onClick = { onNavigate(ScreenDestination.CreateJournal) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Buat Jurnal",
                    tint = if (isCreate) NavyPrimary else NavySecondary
                )
            },
            label = { Text("Buat Jurnal") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("bottom_nav_create")
        )

        NavigationBarItem(
            selected = isHistory,
            onClick = { onNavigate(ScreenDestination.History) },
            icon = {
                Icon(
                    imageVector = if (isHistory) Icons.Filled.Assessment else Icons.Outlined.Assessment,
                    contentDescription = "Rekap dan Riwayat"
                )
            },
            label = { Text("Rekap") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("bottom_nav_history")
        )

        NavigationBarItem(
            selected = isProfile,
            onClick = { onNavigate(ScreenDestination.Profile) },
            icon = {
                Icon(
                    imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profil Guru"
                )
            },
            label = { Text("Profil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("bottom_nav_profile")
        )
    }
}
