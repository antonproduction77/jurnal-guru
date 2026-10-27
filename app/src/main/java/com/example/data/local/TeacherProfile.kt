package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher_profile")
data class TeacherProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val namaLengkap: String = "Dra. Hj. Siti Rahmawati, M.Pd.",
    val nip: String = "198507122010012005",
    val email: String = "siti.rahmawati@smpn1.sch.id",
    val mapelUtama: String = "Bahasa Indonesia",
    val namaSekolah: String = "SMP Negeri 1 Merdeka Belajar",
    val namaKepalaSekolah: String = "Anton Adiwijaya, S.Pd.",
    val nipKepalaSekolah: String = "197711052009041001",
    val photoUri: String? = null,
    val password: String = "guru12345",
    val isLoggedIn: Boolean = true,
    val rememberMe: Boolean = true,
    val isDemo: Boolean = false
)
