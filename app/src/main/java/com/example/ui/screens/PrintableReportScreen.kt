package com.example.ui.screens

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintableReportScreen(
    htmlContent: String,
    onBack: () -> Unit,
    onShareCsv: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var webViewInstance: WebView? = remember { null }

    fun printDocument(webView: WebView?) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
        val printAdapter = webView?.createPrintDocumentAdapter("Laporan_Jurnal_Mengajar_SMP")
        if (printManager != null && printAdapter != null) {
            val jobName = "Laporan Jurnal Mengajar Guru SMP"
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(PrintAttributes.Resolution("pdf", "pdf", 300, 300))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build()
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Format Siap Cetak (PDF)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { printDocument(webViewInstance) },
                        modifier = Modifier.testTag("btn_print_topbar")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = "Cetak / Simpan PDF", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyPrimary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onShareCsv,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_share_csv_bottom")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Kirim Excel")
                    }

                    Button(
                        onClick = { printDocument(webViewInstance) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_print_pdf_bottom")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cetak / Simpan PDF", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = false
                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    webViewInstance = webView
                },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("webview_report_preview")
            )
        }
    }
}
