package com.example

import com.example.data.export.JournalExporter
import com.example.data.local.JournalEntry
import com.example.data.local.TeacherProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testJournalEntryCalculations() {
        val entry = JournalEntry(
            id = 1L,
            dateEpochMillis = System.currentTimeMillis(),
            formattedDate = "Senin, 26 Oktober 2026",
            jamKe = "Jam Ke-1 & 2",
            jumlahJp = 2,
            kelas = "Kelas 7",
            mataPelajaran = "Bahasa Indonesia",
            materiTP = "Teks Deskripsi",
            kegiatanPembelajaran = "Diskusi Kelompok",
            hadirCount = 30,
            sakitCount = 1,
            izinCount = 1,
            alphaCount = 0
        )

        assertEquals(32, entry.totalSiswa)
        assertEquals(2, entry.jumlahJp)
        assertEquals(80, entry.totalMenit) // 2 JP * 40 Menit = 80 Menit
        val persentase = entry.persentaseHadir
        assertTrue(persentase > 93f && persentase < 94f)
    }

    @Test
    fun testPrintableHtmlGenerationWithKepalaSekolah() {
        val profile = TeacherProfile(
            namaLengkap = "Dra. Siti Rahmawati, M.Pd.",
            nip = "198507122010012005",
            mapelUtama = "Bahasa Indonesia",
            namaSekolah = "SMP Negeri 1 Merdeka Belajar",
            namaKepalaSekolah = "Anton Adiwijaya, S.Pd.",
            nipKepalaSekolah = "197711052009041001"
        )
        val entries = listOf(
            JournalEntry(
                id = 1L,
                dateEpochMillis = 1700000000000L,
                formattedDate = "Senin, 26 Oktober 2026",
                jamKe = "Jam Ke-1 s/d 2",
                jumlahJp = 2,
                kelas = "Kelas 7",
                mataPelajaran = "Bahasa Indonesia",
                materiTP = "Teks Deskripsi Objek Wisata",
                kegiatanPembelajaran = "Apersepsi dan eksplorasi kelompok",
                hadirCount = 31,
                sakitCount = 1,
                izinCount = 0,
                alphaCount = 0
            )
        )

        val html = JournalExporter.generatePrintableHtml(profile, entries)
        assertTrue(html.contains("DINAS PENDIDIKAN"))
        assertTrue(html.contains("SMP NEGERI 1 MERDEKA BELAJAR"))
        assertTrue(html.contains("Teks Deskripsi Objek Wisata"))
        assertTrue(html.contains("Anton Adiwijaya, S.Pd."))
        assertTrue(html.contains("197711052009041001"))
        assertTrue(html.contains("80 Menit"))
    }

    @Test
    fun testTeacherProfileDemoAndRegisteredFlags() {
        val demoProfile = TeacherProfile(
            id = 1,
            namaLengkap = "Dra. Hj. Siti Rahmawati, M.Pd.",
            nip = "198507122010012005",
            namaKepalaSekolah = "Anton Adiwijaya, S.Pd.",
            nipKepalaSekolah = "197711052009041001",
            isDemo = true
        )
        assertTrue(demoProfile.isDemo)
        assertEquals("Anton Adiwijaya, S.Pd.", demoProfile.namaKepalaSekolah)
        assertEquals("197711052009041001", demoProfile.nipKepalaSekolah)

        val registeredTeacher = TeacherProfile(
            id = 2,
            namaLengkap = "Budi Hartono, S.Pd.",
            nip = "199203142019031008",
            email = "budi@smp.sch.id",
            mapelUtama = "Matematika",
            namaSekolah = "SMP Negeri 2",
            isDemo = false
        )
        assertFalse(registeredTeacher.isDemo)
        assertEquals("Matematika", registeredTeacher.mapelUtama)
    }
}
