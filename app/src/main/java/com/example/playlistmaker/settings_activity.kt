package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class settings_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.settings_activity)
        val back = findViewById<LinearLayout>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        finish()
        }
        val shareImageView: ImageView = findViewById(R.id.share)

        shareImageView.setOnClickListener {
            shareApp()
        }
        val contactSupportButton: ImageView = findViewById(R.id.support)

        contactSupportButton.setOnClickListener {
            sendSupportEmail()
        }
        val userAgreementButton: ImageView = findViewById(R.id.agreements)
        userAgreementButton.setOnClickListener {
            openUserAgreement()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainSettings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun openUserAgreement() {
        val url = getString(R.string.url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            showToast(getString(R.string.browsError))
        }
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

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)

        } else {
            showToast(getString(R.string.noMail))
        }
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