package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.searchButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        val msButton = findViewById<Button>(R.id.mediaStockButton)

        search.setOnClickListener {
            val intent = Intent(this, search_activity::class.java)
            startActivity(intent)
        }
        msButton.setOnClickListener {
            val intent = Intent(this, mediaStock_activity::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener {
          val intent = Intent(this, settings_activity::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }
}