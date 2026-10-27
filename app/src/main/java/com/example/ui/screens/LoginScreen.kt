package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.TeacherProfile
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.viewmodel.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: JournalViewModel,
    onLoginSuccess: () -> Unit
) {
    val demoProfile by viewModel.demoProfile.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Masuk, 1: Daftar Akun Baru

    // Login Form State
    var identifier by remember { mutableStateOf(demoProfile?.nip ?: "") }
    var password by remember { mutableStateOf(if (demoProfile != null) "guru12345" else "") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    var isLoggingIn by remember { mutableStateOf(false) }

    // Register Form State
    var regNama by remember { mutableStateOf("") }
    var regNip by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regMapel by remember { mutableStateOf("Bahasa Indonesia") }
    var regMapelExpanded by remember { mutableStateOf(false) }
    var regSekolah by remember { mutableStateOf("SMP Negeri 1") }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var regPassVisible by remember { mutableStateOf(false) }
    var regErrorMessage by remember { mutableStateOf<String?>(null) }
    var isRegistering by remember { mutableStateOf(false) }

    // Dialog states
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showDeleteDemoConfirmDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val mapelOptions = listOf(
        "Bahasa Indonesia",
        "Matematika",
        "Ilmu Pengetahuan Alam (IPA)",
        "Ilmu Pengetahuan Sosial (IPS)",
        "Bahasa Inggris",
        "Pendidikan Pancasila (PPKn)",
        "Pendidikan Agama & Budi Pekerti",
        "Informatika",
        "PJOK",
        "Seni Budaya"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // School & App Brand Header
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = NavyPrimary,
                modifier = Modifier.size(68.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Logo Pendidikan",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Jurnal Guru SMP",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
            )

            Text(
                text = "Sistem Informasi Kegiatan Belajar Mengajar Harian",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hero banner card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.teacher_hero_banner_1793052985227),
                    contentDescription = "Ilustrasi Guru Mengajar SMP",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Auth Tabs: Masuk vs Daftar
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = NavyPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NavyPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_tab_row")
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Masuk Akun",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) NavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.testTag("tab_masuk")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Daftar Akun Baru",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) NavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.testTag("tab_daftar")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TAB 0: LOGIN FORM
            if (selectedTab == 0) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Masuk ke Akun Guru",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Gunakan NIP atau Email dinas yang terdaftar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (loginErrorMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFEE2E2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = loginErrorMessage ?: "",
                                    color = Color(0xFFDC2626),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        // Field NIP / Email
                        OutlinedTextField(
                            value = identifier,
                            onValueChange = {
                                identifier = it
                                loginErrorMessage = null
                            },
                            label = { Text("NIP / Email Guru") },
                            placeholder = { Text("Contoh: 198507122010012005") },
                            leadingIcon = {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = NavyPrimary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NavyPrimary,
                                focusedLabelColor = NavyPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_nip_email")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Field Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                loginErrorMessage = null
                            },
                            label = { Text("Kata Sandi") },
                            placeholder = { Text("Masukkan kata sandi") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NavyPrimary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NavyPrimary,
                                focusedLabelColor = NavyPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_password")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Remember Me & Forgot Password
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.testTag("checkbox_remember_me")
                            ) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(checkedColor = NavyPrimary)
                                )
                                Text(
                                    text = "Ingat Saya",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            TextButton(
                                onClick = { showForgotPasswordDialog = true },
                                modifier = Modifier.testTag("btn_forgot_password")
                            ) {
                                Text(
                                    text = "Lupa Password?",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = NavySecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Login Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                isLoggingIn = true
                                loginErrorMessage = null
                                viewModel.login(
                                    identifier,
                                    password,
                                    rememberMe,
                                    {
                                        isLoggingIn = false
                                        onLoginSuccess()
                                    },
                                    { error ->
                                        isLoggingIn = false
                                        loginErrorMessage = error
                                    }
                                )
                            },
                            enabled = !isLoggingIn,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_login")
                        ) {
                            if (isLoggingIn) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                            } else {
                                Text(
                                    text = "Masuk ke Jurnal Mengajar",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Belum memiliki akun?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(
                                onClick = { selectedTab = 1 },
                                modifier = Modifier.testTag("btn_switch_to_register")
                            ) {
                                Text(
                                    text = "Daftar di Sini",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = NavySecondary
                                )
                            }
                        }
                    }
                }

                // Demo Account Card (shown only if demoProfile exists)
                if (demoProfile != null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = NavySecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Akun Demo Guru Tersedia",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                // Delete Demo Account Button
                                OutlinedButton(
                                    onClick = { showDeleteDemoConfirmDialog = true },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                    modifier = Modifier.testTag("btn_hapus_akun_demo")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Hapus Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "NIP: ${demoProfile?.nip}  •  Pass: guru12345",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    identifier = demoProfile?.nip ?: "198507122010012005"
                                    password = "guru12345"
                                    viewModel.login(
                                        identifier,
                                        password,
                                        true,
                                        { onLoginSuccess() },
                                        { err -> loginErrorMessage = err }
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NavySecondary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .testTag("btn_quick_demo_login")
                            ) {
                                Text("Gunakan Akun Demo (Dra. Siti Rahmawati)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // TAB 1: REGISTRATION FORM
            if (selectedTab == 1) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Form Pendaftaran Guru Baru",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Isi data pribadi guru SMP untuk mulai mencatat KBM",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (regErrorMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFEE2E2),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = regErrorMessage ?: "",
                                    color = Color(0xFFDC2626),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        // Nama Lengkap & Gelar
                        OutlinedTextField(
                            value = regNama,
                            onValueChange = { regNama = it; regErrorMessage = null },
                            label = { Text("Nama Lengkap & Gelar *") },
                            placeholder = { Text("Contoh: Ahmad Dahlan, S.Pd.") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_nama")
                        )

                        // NIP Guru
                        OutlinedTextField(
                            value = regNip,
                            onValueChange = { regNip = it; regErrorMessage = null },
                            label = { Text("NIP Guru *") },
                            placeholder = { Text("Contoh: 199008152015021004") },
                            leadingIcon = {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = NavyPrimary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_nip")
                        )

                        // Email
                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it; regErrorMessage = null },
                            label = { Text("Email Guru / Dinas *") },
                            placeholder = { Text("Contoh: guru@smp.sch.id") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = NavyPrimary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_email")
                        )

                        // Mata Pelajaran Dropdown
                        ExposedDropdownMenuBox(
                            expanded = regMapelExpanded,
                            onExpandedChange = { regMapelExpanded = !regMapelExpanded }
                        ) {
                            OutlinedTextField(
                                value = regMapel,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Mata Pelajaran Utama *") },
                                leadingIcon = {
                                    Icon(Icons.Default.Subject, contentDescription = null, tint = NavyPrimary)
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regMapelExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("dropdown_reg_mapel")
                            )

                            ExposedDropdownMenu(
                                expanded = regMapelExpanded,
                                onDismissRequest = { regMapelExpanded = false }
                            ) {
                                mapelOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt) },
                                        onClick = {
                                            regMapel = opt
                                            regMapelExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Nama Sekolah
                        OutlinedTextField(
                            value = regSekolah,
                            onValueChange = { regSekolah = it; regErrorMessage = null },
                            label = { Text("Nama Satuan Pendidikan (Sekolah) *") },
                            placeholder = { Text("Contoh: SMP Negeri 2 Merdeka") },
                            leadingIcon = {
                                Icon(Icons.Default.School, contentDescription = null, tint = NavyPrimary)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_sekolah")
                        )

                        // Kata Sandi
                        OutlinedTextField(
                            value = regPassword,
                            onValueChange = { regPassword = it; regErrorMessage = null },
                            label = { Text("Kata Sandi Baru (Min. 5 karakter) *") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NavyPrimary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { regPassVisible = !regPassVisible }) {
                                    Icon(
                                        imageVector = if (regPassVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (regPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_password")
                        )

                        // Konfirmasi Kata Sandi
                        OutlinedTextField(
                            value = regConfirmPassword,
                            onValueChange = { regConfirmPassword = it; regErrorMessage = null },
                            label = { Text("Konfirmasi Kata Sandi *") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NavyPrimary)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_confirm_password")
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Register Submit Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                isRegistering = true
                                regErrorMessage = null
                                viewModel.registerTeacher(
                                    nama = regNama,
                                    nip = regNip,
                                    email = regEmail,
                                    mapel = regMapel,
                                    sekolah = regSekolah,
                                    pass = regPassword,
                                    confirmPass = regConfirmPassword,
                                    onSuccess = {
                                        isRegistering = false
                                        onLoginSuccess()
                                    },
                                    onError = { err ->
                                        isRegistering = false
                                        regErrorMessage = err
                                    }
                                )
                            },
                            enabled = !isRegistering,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_submit_register")
                        ) {
                            if (isRegistering) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Daftar & Masuk ke Jurnal",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sudah memiliki akun guru?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(
                                onClick = { selectedTab = 0 },
                                modifier = Modifier.testTag("btn_switch_to_login")
                            ) {
                                Text(
                                    text = "Masuk di Sini",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = NavySecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog Konfirmasi Hapus Akun Demo
    if (showDeleteDemoConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDemoConfirmDialog = false },
            title = {
                Text(
                    text = "Hapus Akun Demo?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Akun demo (Dra. Hj. Siti Rahmawati) dan semua data jurnal contoh akan dihapus permanen dari sistem. Anda dapat membuat akun guru resmi baru melalui menu 'Daftar Akun Baru'."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDemoConfirmDialog = false
                        viewModel.deleteDemoAccount {
                            identifier = ""
                            password = ""
                            selectedTab = 1 // Switch to register so teacher can register their own account
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    modifier = Modifier.testTag("btn_confirm_delete_demo")
                ) {
                    Text("Ya, Hapus Akun Demo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDemoConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Lupa Kata Sandi?") },
            text = {
                Column {
                    Text("Untuk mereset password akun guru, Anda dapat menghubungi Operator SIMPKB/Tata Usaha Sekolah atau daftarkan akun baru.")
                    if (demoProfile != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Untuk akun demo, password default adalah: guru12345",
                            fontWeight = FontWeight.SemiBold,
                            color = NavyPrimary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (demoProfile != null) {
                            password = "guru12345"
                        }
                        showForgotPasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Tutup")
                }
            }
        )
    }
}
