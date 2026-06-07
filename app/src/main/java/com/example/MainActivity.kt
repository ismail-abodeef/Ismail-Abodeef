package com.example

import android.app.AlertDialog
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WebBuilderWorkspace(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun WebBuilderWorkspace(modifier: Modifier = Modifier) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        allowFileAccess = true
                        allowContentAccess = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        defaultTextEncodingName = "utf-8"
                        cacheMode = WebSettings.LOAD_DEFAULT
                    }
                    
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            if (url?.startsWith("file:///android_asset/") == true) {
                                return false
                            }
                            return false // Allow loading sub-resources or external frames
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onJsAlert(
                            view: WebView?,
                            url: String?,
                            message: String?,
                            result: JsResult?
                        ): Boolean {
                            AlertDialog.Builder(context)
                                .setTitle("مبدع الويب المحترف")
                                .setMessage(message)
                                .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                                .setCancelable(false)
                                .create()
                                .show()
                            return true
                        }

                        override fun onJsConfirm(
                            view: WebView?,
                            url: String?,
                            message: String?,
                            result: JsResult?
                        ): Boolean {
                            AlertDialog.Builder(context)
                                .setTitle("تأكيد")
                                .setMessage(message)
                                .setPositiveButton("موافق") { _, _ -> result?.confirm() }
                                .setNegativeButton("إلغاء") { _, _ -> result?.cancel() }
                                .setCancelable(false)
                                .create()
                                .show()
                            return true
                        }
                    }

                    loadUrl("file:///android_asset/www/index.html")
                }
            }
        )
    }
}
