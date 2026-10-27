package com.example.data.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.local.JournalEntry
import com.example.data.local.TeacherProfile
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object JournalExporter {

    /**
     * Generates an Excel-compatible CSV file (with UTF-8 BOM for Microsoft Excel / Sheets).
     */
    fun exportToCsv(
        context: Context,
        profile: TeacherProfile,
        entries: List<JournalEntry>
    ): File {
        val reportsDir = File(context.cacheDir, "reports")
        if (!reportsDir.exists()) reportsDir.mkdirs()

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(reportsDir, "Rekap_Jurnal_Mengajar_${timestamp}.csv")

        FileOutputStream(file).use { output ->
            // Write UTF-8 BOM for Excel compatibility
            output.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = output.bufferedWriter(Charsets.UTF_8)

            writer.write("LAPORAN JURNAL KEGIATAN BELAJAR MENGAJAR (KBM) HARIAN GURU SMP\n")
            writer.write("Nama Guru:,\"${profile.namaLengkap}\"\n")
            writer.write("NIP Guru:,\"${profile.nip}\"\n")
            writer.write("Mata Pelajaran:,\"${profile.mapelUtama}\"\n")
            writer.write("Satuan Pendidikan:,\"${profile.namaSekolah}\"\n")
            writer.write("Kepala Sekolah:,\"${profile.namaKepalaSekolah}\"\n")
            writer.write("NIP Kepala Sekolah:,\"${profile.nipKepalaSekolah}\"\n")
            writer.write("Standar Jam Pelajaran:,\"1 JP = 40 Menit\"\n")
            writer.write("Tanggal Cetak:,\"${SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(Date())}\"\n\n")

            // Headers
            writer.write("No,Tanggal,Jam Ke,Jumlah JP,Durasi (Menit),Kelas,Mata Pelajaran,Materi / TP (Tujuan Pembelajaran),Kegiatan Pembelajaran,Hadir,Sakit,Izin,Alpha,Keterangan Siswa Tidak Hadir,Catatan Kejadian / Perilaku\n")

            entries.forEachIndexed { index, entry ->
                val line = buildString {
                    append(index + 1).append(",")
                    append("\"${escapeCsv(entry.formattedDate)}\",")
                    append("\"${escapeCsv(entry.jamKe)}\",")
                    append(entry.jumlahJp).append(",")
                    append(entry.totalMenit).append(",")
                    append("\"${escapeCsv(entry.kelas)}\",")
                    append("\"${escapeCsv(entry.mataPelajaran)}\",")
                    append("\"${escapeCsv(entry.materiTP)}\",")
                    append("\"${escapeCsv(entry.kegiatanPembelajaran)}\",")
                    append(entry.hadirCount).append(",")
                    append(entry.sakitCount).append(",")
                    append(entry.izinCount).append(",")
                    append(entry.alphaCount).append(",")
                    append("\"${escapeCsv(entry.siswaTidakHadirKeterangan)}\",")
                    append("\"${escapeCsv(entry.catatanKejadian)}\"\n")
                }
                writer.write(line)
            }
            writer.flush()
        }

        return file
    }

    /**
     * Generates a printable HTML document formatted according to official Indonesian school journal standards.
     */
    fun generatePrintableHtml(
        profile: TeacherProfile,
        entries: List<JournalEntry>
    ): String {
        val todayStr = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(Date())

        val tableRows = entries.mapIndexed { index, e ->
            """
            <tr>
                <td style="text-align: center;">${index + 1}</td>
                <td>
                    ${escapeHtml(e.formattedDate)}<br>
                    <small style="color: #475569;">${escapeHtml(e.jamKe)}</small><br>
                    <span style="display: inline-block; background-color: #e0f2fe; color: #0369a1; padding: 1px 4px; border-radius: 4px; font-size: 10px; font-weight: bold;">
                        ${e.jumlahJp} JP (${e.totalMenit} Menit)
                    </span>
                </td>
                <td style="text-align: center; font-weight: bold; color: #1e3a8a;">${escapeHtml(e.kelas)}</td>
                <td>${escapeHtml(e.materiTP)}</td>
                <td><small>${escapeHtml(e.kegiatanPembelajaran).replace("\n", "<br>")}</small></td>
                <td style="text-align: center;">
                    <span style="color: green; font-weight: bold;">H: ${e.hadirCount}</span><br>
                    <small>S: ${e.sakitCount} | I: ${e.izinCount} | A: ${e.alphaCount}</small>
                </td>
                <td><small>${escapeHtml(e.siswaTidakHadirKeterangan)}</small></td>
                <td><small>${escapeHtml(e.catatanKejadian)}</small></td>
            </tr>
            """.trimIndent()
        }.joinToString("\n")

        return """
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Rekap Jurnal Mengajar Guru SMP</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    color: #0f172a;
                    font-size: 12px;
                    line-height: 1.4;
                }
                .header {
                    text-align: center;
                    border-bottom: 3px double #000;
                    padding-bottom: 10px;
                    margin-bottom: 16px;
                }
                .header h2 { margin: 0; font-size: 16px; text-transform: uppercase; }
                .header h3 { margin: 4px 0; font-size: 14px; text-transform: uppercase; }
                .header p { margin: 2px 0; font-size: 11px; color: #334155; }
                .meta-table {
                    width: 100%;
                    margin-bottom: 14px;
                    font-size: 12px;
                }
                .meta-table td {
                    padding: 3px 6px;
                    vertical-align: top;
                }
                .table-data {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 10px;
                    font-size: 11px;
                }
                .table-data th, .table-data td {
                    border: 1px solid #334155;
                    padding: 6px 8px;
                    vertical-align: top;
                }
                .table-data th {
                    background-color: #f1f5f9;
                    text-align: center;
                    font-weight: bold;
                }
                .signatures {
                    margin-top: 36px;
                    width: 100%;
                }
                .signatures td {
                    width: 50%;
                    text-align: center;
                    vertical-align: top;
                }
                .sign-space {
                    height: 70px;
                }
                @media print {
                    body { margin: 0; }
                    .no-print { display: none; }
                }
            </style>
        </head>
        <body>
            <div class="header">
                <h2>PEMERINTAH DAERAH DINAS PENDIDIKAN</h2>
                <h3>${escapeHtml(profile.namaSekolah.uppercase(Locale("id", "ID")))}</h3>
                <p>Jurnal Catatan Kegiatan Belajar Mengajar (KBM) Harian Guru SMP - Kurikulum Merdeka (1 JP = 40 Menit)</p>
            </div>

            <table class="meta-table">
                <tr>
                    <td style="width: 18%;"><strong>Nama Guru</strong></td>
                    <td style="width: 2%;">:</td>
                    <td style="width: 40%;">${escapeHtml(profile.namaLengkap)}</td>
                    <td style="width: 15%;"><strong>Tahun Ajaran</strong></td>
                    <td style="width: 2%;">:</td>
                    <td>2026/2027</td>
                </tr>
                <tr>
                    <td><strong>NIP Guru</strong></td>
                    <td>:</td>
                    <td>${escapeHtml(profile.nip)}</td>
                    <td><strong>Semester</strong></td>
                    <td>:</td>
                    <td>Ganjil</td>
                </tr>
                <tr>
                    <td><strong>Mata Pelajaran</strong></td>
                    <td>:</td>
                    <td>${escapeHtml(profile.mapelUtama)}</td>
                    <td><strong>Kepala Sekolah</strong></td>
                    <td>:</td>
                    <td>${escapeHtml(profile.namaKepalaSekolah)}</td>
                </tr>
                <tr>
                    <td><strong>Satuan Pendidikan</strong></td>
                    <td>:</td>
                    <td>${escapeHtml(profile.namaSekolah)}</td>
                    <td><strong>Tanggal Cetak</strong></td>
                    <td>:</td>
                    <td>$todayStr</td>
                </tr>
            </table>

            <table class="table-data">
                <thead>
                    <tr>
                        <th style="width: 4%;">No</th>
                        <th style="width: 15%;">Tanggal, Jam & Durasi</th>
                        <th style="width: 8%;">Tingkat</th>
                        <th style="width: 20%;">Materi / TP (Tujuan Pembelajaran)</th>
                        <th style="width: 23%;">Kegiatan Pembelajaran</th>
                        <th style="width: 10%;">Presensi</th>
                        <th style="width: 10%;">Keterangan Absen</th>
                        <th style="width: 10%;">Catatan Kejadian</th>
                    </tr>
                </thead>
                <tbody>
                    $tableRows
                </tbody>
            </table>

            <table class="signatures">
                <tr>
                    <td>
                        Mengetahui,<br>
                        <strong>Kepala Sekolah</strong>
                        <div class="sign-space"></div>
                        <strong><u>${escapeHtml(profile.namaKepalaSekolah)}</u></strong><br>
                        NIP. ${escapeHtml(profile.nipKepalaSekolah)}
                    </td>
                    <td>
                        Guru Mata Pelajaran,
                        <div class="sign-space"></div>
                        <strong><u>${escapeHtml(profile.namaLengkap)}</u></strong><br>
                        NIP. ${escapeHtml(profile.nip)}
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """.trimIndent()
    }

    fun shareCsvFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Rekap Jurnal Mengajar Guru SMP")
            putExtra(Intent.EXTRA_TEXT, "Berikut terlampir file rekap jurnal mengajar harian guru SMP format spreadsheet/Excel.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(sendIntent, "Bagikan Rekap Jurnal Excel / CSV")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun escapeCsv(value: String): String {
        return value.replace("\"", "\"\"")
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
