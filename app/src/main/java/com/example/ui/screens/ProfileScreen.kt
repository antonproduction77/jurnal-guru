package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.TeacherProfile
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary

@Composable
fun ProfileScreen(
    profile: TeacherProfile?,
    totalJournalCount: Int,
    onUpdateProfile: (nama: String, nip: String, email: String, mapel: String, sekolah: String, kepalaSekolah: String, nipKepalaSekolah: String, photoUri: String?) -> Unit,
    onChangePassword: (oldPass: String, newPass: String, confirmPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit,
    onDeleteDemoAccount: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProfile = profile ?: TeacherProfile()
    val scrollState = rememberScrollState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showDeleteDemoDialog by remember { mutableStateOf(false) }

    // Avatar photo picker
    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onUpdateProfile(
                currentProfile.namaLengkap,
                currentProfile.nip,
                currentProfile.email,
                currentProfile.mapelUtama,
                currentProfile.namaSekolah,
                currentProfile.namaKepalaSekolah,
                currentProfile.nipKepalaSekolah,
                uri.toString()
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // Profile Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyPrimary)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with Camera Badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(86.dp)
                    ) {
                        if (currentProfile.photoUri != null) {
                            AsyncImage(
                                model = currentProfile.photoUri,
                                contentDescription = "Foto Guru",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Foto Guru",
                                    tint = Color.White,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            avatarPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .background(NavySecondary, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Ganti Foto",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentProfile.namaLengkap,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Text(
                    text = "NIP: ${currentProfile.nip}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${currentProfile.mapelUtama} • ${currentProfile.namaSekolah}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                if (currentProfile.isDemo) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "Mode Akun Demo",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFB45309),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        // Demo Account Warning Card (if using demo)
        if (currentProfile.isDemo) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFEF3C7),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Anda Menggunakan Akun Demo",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFB45309)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hapus akun demo & data contoh ini jika Anda ingin menggunakan akun guru pribadi Anda.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showDeleteDemoDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_hapus_demo_profile_top")
                    ) {
                        Text("Hapus Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Data Kepala Sekolah Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kepala Sekolah",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentProfile.namaKepalaSekolah,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "NIP. ${currentProfile.nipKepalaSekolah}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Stats Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$totalJournalCount",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )
                    Text(
                        text = "Jurnal Terisi",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(modifier = Modifier
                    .height(36.dp)
                    .width(1.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "1 JP = 40m",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )
                    Text(
                        text = "Durasi Jam KBM",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(modifier = Modifier
                    .height(36.dp)
                    .width(1.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Kelas 7,8,9",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )
                    Text(
                        text = "Tingkat SMP",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action Menu List
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.5.dp)
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    title = "Edit Data Profil Guru & Sekolah",
                    subtitle = "Ubah Nama, NIP, Mapel, Sekolah, dan Kepala Sekolah",
                    onClick = { showEditProfileDialog = true },
                    testTag = "menu_edit_profile"
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Ubah Kata Sandi (Password)",
                    subtitle = "Ganti password login akun guru",
                    onClick = { showChangePasswordDialog = true },
                    testTag = "menu_change_password"
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Hapus Akun Demo / Data Contoh Option
                ProfileMenuItem(
                    icon = Icons.Default.DeleteForever,
                    title = "Hapus Akun Demo & Data Contoh",
                    subtitle = "Bersihkan data contoh jurnal dan akun demo Dra. Siti Rahmawati",
                    onClick = { showDeleteDemoDialog = true },
                    testTag = "menu_delete_demo"
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi & Panduan Jurnal",
                    subtitle = "Jurnal KBM Harian SMP v1.2 • 1 JP = 40 Menit",
                    onClick = { showAboutDialog = true },
                    testTag = "menu_about_app"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout Button
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = { showLogoutDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_logout")
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Keluar dari Akun (Logout)",
                    color = Color(0xFFDC2626),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Dialog Edit Profil
    if (showEditProfileDialog) {
        var editNama by remember { mutableStateOf(currentProfile.namaLengkap) }
        var editNip by remember { mutableStateOf(currentProfile.nip) }
        var editEmail by remember { mutableStateOf(currentProfile.email) }
        var editMapel by remember { mutableStateOf(currentProfile.mapelUtama) }
        var editSekolah by remember { mutableStateOf(currentProfile.namaSekolah) }
        var editKepalaSekolah by remember { mutableStateOf(currentProfile.namaKepalaSekolah) }
        var editNipKepalaSekolah by remember { mutableStateOf(currentProfile.nipKepalaSekolah) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profil Guru & Sekolah") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = editNama,
                        onValueChange = { editNama = it },
                        label = { Text("Nama Lengkap Guru & Gelar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editNip,
                        onValueChange = { editNip = it },
                        label = { Text("NIP Guru") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Dinas / Pribadi") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editMapel,
                        onValueChange = { editMapel = it },
                        label = { Text("Mata Pelajaran Utama") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editSekolah,
                        onValueChange = { editSekolah = it },
                        label = { Text("Nama Sekolah (Satuan Pendidikan)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editKepalaSekolah,
                        onValueChange = { editKepalaSekolah = it },
                        label = { Text("Nama Kepala Sekolah") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editNipKepalaSekolah,
                        onValueChange = { editNipKepalaSekolah = it },
                        label = { Text("NIP Kepala Sekolah") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(
                            editNama,
                            editNip,
                            editEmail,
                            editMapel,
                            editSekolah,
                            editKepalaSekolah,
                            editNipKepalaSekolah,
                            currentProfile.photoUri
                        )
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Simpan Profil")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Ubah Password
    if (showChangePasswordDialog) {
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }
        var confirmPass by remember { mutableStateOf("") }
        var passError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Ubah Kata Sandi") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (passError != null) {
                        Text(passError ?: "", color = Color(0xFFDC2626), style = MaterialTheme.typography.bodySmall)
                    }

                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it; passError = null },
                        label = { Text("Kata Sandi Saat Ini") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it; passError = null },
                        label = { Text("Kata Sandi Baru (Min. 5 karakter)") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = { confirmPass = it; passError = null },
                        label = { Text("Ulangi Kata Sandi Baru") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onChangePassword(oldPass, newPass, confirmPass, {
                            showChangePasswordDialog = false
                        }, { err ->
                            passError = err
                        })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Ganti Sandi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Konfirmasi Hapus Akun Demo
    if (showDeleteDemoDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDemoDialog = false },
            title = {
                Text(
                    text = "Hapus Akun & Data Demo?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Tindakan ini akan menghapus akun demo Dra. Hj. Siti Rahmawati beserta semua jurnal contoh yang ada. Aplikasi akan bersih dan siap digunakan untuk akun guru pribadi Anda."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDemoDialog = false
                        onDeleteDemoAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Ya, Hapus Demo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDemoDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar Akun Guru") },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi Jurnal Guru SMP?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Tentang Aplikasi
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Jurnal Mengajar Guru SMP") },
            text = {
                Column {
                    Text("Aplikasi Jurnal Mengajar Harian Guru SMP dirancang khusus untuk mempermudah guru mencatat kegiatan KBM harian, presensi kehadiran siswa, dan dokumentasi kelas secara cepat langsung dari HP.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Spesifikasi Jenjang SMP:\n• Tingkat Kelas: Kelas 7, Kelas 8, Kelas 9\n• Durasi: 1 Jam Pelajaran (JP) = 40 Menit\n• Kepala Sekolah: Anton Adiwijaya, S.Pd. (NIP 197711052009041001)\n• Format Ekspor Excel & Siap Cetak PDF Resmi")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Versi: 1.2 (Kurikulum Merdeka SMP)", style = MaterialTheme.typography.labelSmall, color = NavySecondary)
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFEFF6FF),
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
