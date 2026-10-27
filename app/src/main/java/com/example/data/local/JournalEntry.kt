package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateEpochMillis: Long,
    val formattedDate: String,            // e.g. "Senin, 26 Okt 2026"
    val jamKe: String,                    // e.g. "Jam Ke-1 s/d 2 (07:30 - 08:50)"
    val jumlahJp: Int = 2,                // Nilai jumlah jam pelajaran (1 JP = 40 menit)
    val kelas: String,                    // Tingkat kelas SMP: "Kelas 7", "Kelas 8", "Kelas 9"
    val mataPelajaran: String,            // e.g. "Bahasa Indonesia"
    val materiTP: String,                 // Materi / Tujuan Pembelajaran (Kurikulum Merdeka)
    val kegiatanPembelajaran: String,     // Ringkasan aktivitas KBM di kelas
    val hadirCount: Int = 30,
    val sakitCount: Int = 0,
    val izinCount: Int = 0,
    val alphaCount: Int = 0,
    val siswaTidakHadirKeterangan: String = "", // Catatan nama & alasan
    val catatanKejadian: String = "",           // Catatan penting / perilaku siswa (opsional)
    val photoUri: String? = null,               // Path atau URI foto dokumentasi KBM
    val isDemoEntry: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalSiswa: Int
        get() = hadirCount + sakitCount + izinCount + alphaCount

    val totalMenit: Int
        get() = jumlahJp * 40

    val persentaseHadir: Float
        get() = if (totalSiswa > 0) (hadirCount.toFloat() / totalSiswa) * 100f else 100f
}
