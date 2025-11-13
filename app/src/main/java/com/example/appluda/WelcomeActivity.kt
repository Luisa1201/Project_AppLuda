package com.example.appluda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Verificar si el usuario ya inició sesión
        val sharedPrefs = getSharedPreferences("LudaPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Si ya está logueado, ir directamente al Home
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        // Configurar botón de inicio
        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}