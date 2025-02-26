package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class settings_activity : AppCompatActivity() {

    private lateinit var themeSwitch: SwitchMaterial
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.settings_activity)
        val back = findViewById<LinearLayout>(R.id.back)

        back.setOnClickListener {
                 finish()
        }
        val shareImageView: MaterialTextView = findViewById(R.id.share)

        shareImageView.setOnClickListener {
            shareApp()
        }
        val contactSupportButton: MaterialTextView = findViewById(R.id.support)

        contactSupportButton.setOnClickListener {
            sendSupportEmail()
        }
        val userAgreementButton: MaterialTextView = findViewById(R.id.agreements)
        userAgreementButton.setOnClickListener {
            openUserAgreement()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainSettings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Инициализация SharedPreferences для сохранения состояния переключателя
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("night_mode", false)

        // Найдем наш SwitchMaterial
        themeSwitch = findViewById(R.id.switcher)

        // Устанавливаем текущее состояние Switch (чтобы при перезапуске он не сбрасывался)
        themeSwitch.isChecked = isNightMode

        // Устанавливаем текущую тему
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Обрабатываем нажатие на Switch
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("night_mode", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("night_mode", false).apply()
            }
        }
    }

    private fun openUserAgreement() {
            val url = getString(R.string.url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
    }

    private fun sendSupportEmail() {
        val email = getString(R.string.eMail)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
      startActivity(intent)

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun shareApp() {
        val shareMessage = getString(R.string.http)


        val intent = Intent(Intent.ACTION_SEND).apply {
            type = getString(R.string.tp)
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }

        val chooser = Intent.createChooser(intent, getString(R.string.appShareText))
        startActivity(chooser)
    }
}