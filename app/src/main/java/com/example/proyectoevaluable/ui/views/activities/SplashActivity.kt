package com.example.proyectoevaluable.ui.views.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoevaluable.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece pantalla completa
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Verifica si ya se mostró el splash (usando SharedPreferences)
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("first_time", true)) {
            irALogin()
            return
        } else {
            prefs.edit().putBoolean("first_time", false).apply()
        }

        setContentView(R.layout.activity_splash)

        // Configura el WebView
        val webView = findViewById<WebView>(R.id.videoWebView)
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        // Permitir autoplay sin interacción del usuario
        settings.mediaPlaybackRequiresUserGesture = false
        webView.clearCache(true)
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // Añade la interfaz para recibir llamadas desde JavaScript
        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        // Carga el archivo HTML desde assets
        webView.loadUrl("file:///android_asset/youtube_video.html")

        // Configura el botón "Omitir"
        val skipButton = findViewById<Button>(R.id.skipButton)
        skipButton.setOnClickListener { irALogin() }
    }

    private fun irALogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    // Interfaz para que JavaScript pueda llamar a métodos de Android
    class WebAppInterface(private val activity: Activity) {
        @JavascriptInterface
        fun onVideoEnded() {
            activity.runOnUiThread {
                if (activity is SplashActivity) {
                    activity.irALogin()
                }
            }
        }
    }
}
