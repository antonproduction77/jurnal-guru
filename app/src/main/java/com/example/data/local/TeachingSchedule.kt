package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teaching_schedules")
data class TeachingSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hari: String,             // e.g. "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    val jamKeLabel: String,       // e.g. "Jam Ke-1 & 2"
    val jamKeRange: String,       // e.g. "1-2"
    val waktu: String,            // e.g. "07:30 - 08:50"
    val kelas: String,            // e.g. "7A", "8B", "9A"
    val mataPelajaran: String,    // e.g. "Bahasa Indonesia"
    val ruang: String = "Ruang Kelas",
    val jumlahJam: Int = 2
)
