package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TeacherBottomNavBar
import com.example.ui.screens.CreateJournalScreen
import com.example.ui.screens.DetailJournalScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PrintableReportScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.JurnalGuruTheme
import com.example.ui.viewmodel.JournalViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JurnalGuruTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: JournalViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val allEntries by viewModel.allEntries.collectAsState()
    val allSchedules by viewModel.allSchedules.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    // If teacher is not logged in or profile is null
    if (profile == null || !profile!!.isLoggedIn) {
        LoginScreen(
            viewModel = viewModel,
            onLoginSuccess = {
                viewModel.navigateTo(ScreenDestination.Home)
            }
        )
        return
    }

    val showBottomBar = currentScreen is ScreenDestination.Home ||
            currentScreen is ScreenDestination.CreateJournal ||
            currentScreen is ScreenDestination.History ||
            currentScreen is ScreenDestination.Profile

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                TeacherBottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { dest ->
                        if (dest is ScreenDestination.CreateJournal && currentScreen !is ScreenDestination.CreateJournal) {
                            viewModel.resetJournalForm()
                        }
                        viewModel.navigateTo(dest)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is ScreenDestination.Home -> {
                    HomeScreen(
                        profile = profile,
                        allEntries = allEntries,
                        allSchedules = allSchedules,
                        onAddJournalClick = {
                            viewModel.resetJournalForm()
                            viewModel.navigateTo(ScreenDestination.CreateJournal)
                        },
                        onFillScheduleClick = { schedule ->
                            viewModel.startCreateJournalFromSchedule(schedule)
                        },
                        onViewEntryClick = { entryId ->
                            viewModel.navigateTo(ScreenDestination.DetailJournal(entryId))
                        },
                        onViewAllHistoryClick = {
                            viewModel.navigateTo(ScreenDestination.History)
                        }
                    )
                }

                is ScreenDestination.CreateJournal -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }
                    CreateJournalScreen(
                        viewModel = viewModel,
                        onSavedSuccessfully = {
                            viewModel.navigateTo(ScreenDestination.History)
                        }
                    )
                }

                is ScreenDestination.History -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }
                    HistoryScreen(
                        viewModel = viewModel,
                        onViewDetail = { entryId ->
                            viewModel.navigateTo(ScreenDestination.DetailJournal(entryId))
                        },
                        onEditEntry = { entry ->
                            viewModel.startEditJournal(entry)
                        },
                        onOpenPrintPreview = {
                            viewModel.navigateTo(ScreenDestination.ExportView)
                        }
                    )
                }

                is ScreenDestination.DetailJournal -> {
                    val entry = allEntries.find { it.id == screen.entryId }
                    DetailJournalScreen(
                        entry = entry,
                        onBack = { viewModel.navigateTo(ScreenDestination.History) },
                        onEdit = { toEdit ->
                            viewModel.startEditJournal(toEdit)
                        },
                        onDelete = { toDelete ->
                            viewModel.deleteJournal(toDelete) {
                                viewModel.navigateTo(ScreenDestination.History)
                            }
                        }
                    )
                }

                is ScreenDestination.Profile -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }
                    ProfileScreen(
                        profile = profile,
                        totalJournalCount = allEntries.size,
                        onUpdateProfile = { nama, nip, email, mapel, sekolah, kepalaSekolah, nipKepalaSekolah, photoUri ->
                            viewModel.updateProfile(nama, nip, email, mapel, sekolah, kepalaSekolah, nipKepalaSekolah, photoUri)
                        },
                        onChangePassword = { oldPass, newPass, confirmPass, onSuccess, onError ->
                            viewModel.changePassword(oldPass, newPass, confirmPass, onSuccess, onError)
                        },
                        onDeleteDemoAccount = {
                            viewModel.deleteDemoAccount {
                                viewModel.navigateTo(ScreenDestination.Home)
                            }
                        },
                        onLogout = {
                            viewModel.logout()
                        }
                    )
                }

                is ScreenDestination.ExportView -> {
                    val html = viewModel.getPrintableHtml()
                    PrintableReportScreen(
                        htmlContent = html,
                        onBack = { viewModel.navigateTo(ScreenDestination.History) },
                        onShareCsv = {
                            viewModel.exportAndShareCsv {}
                        }
                    )
                }

                else -> {
                    HomeScreen(
                        profile = profile,
                        allEntries = allEntries,
                        allSchedules = allSchedules,
                        onAddJournalClick = {
                            viewModel.resetJournalForm()
                            viewModel.navigateTo(ScreenDestination.CreateJournal)
                        },
                        onFillScheduleClick = { schedule ->
                            viewModel.startCreateJournalFromSchedule(schedule)
                        },
                        onViewEntryClick = { entryId ->
                            viewModel.navigateTo(ScreenDestination.DetailJournal(entryId))
                        },
                        onViewAllHistoryClick = {
                            viewModel.navigateTo(ScreenDestination.History)
                        }
                    )
                }
            }
        }
    }
}
