package com.example.appluda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var cardSupport: ConstraintLayout
    private lateinit var cardDiary: ConstraintLayout
    private lateinit var cardAppointments: ConstraintLayout
    private lateinit var cardTests: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initializeViews()
        setupListeners()

        // Mostrar mensaje de bienvenida
        val sharedPrefs = getSharedPreferences("LudaPrefs", MODE_PRIVATE)
        val userName = sharedPrefs.getString("userName", "Usuario")
        Toast.makeText(this, "Hola $userName", Toast.LENGTH_SHORT).show()
    }

    private fun initializeViews() {
        cardSupport = findViewById(R.id.cardSupport)
        cardDiary = findViewById(R.id.cardDiary)
        cardAppointments = findViewById(R.id.cardAppointments)
        cardTests = findViewById(R.id.cardTests)
    }

    private fun setupListeners() {
        // Card de Actividades de apoyo
        cardSupport.setOnClickListener {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
        }

        // Card de Diario Personal
        cardDiary.setOnClickListener {
            Toast.makeText(this, "Diario Personal próximamente", Toast.LENGTH_SHORT).show()
        }

        // Card de Reservas de citas
        cardAppointments.setOnClickListener {
            Toast.makeText(this, "Reservas de citas próximamente", Toast.LENGTH_SHORT).show()
        }

        // Card de Tests
        cardTests.setOnClickListener {
            Toast.makeText(this, "Tests próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                Toast.makeText(this, "Perfil próximamente", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_logout -> {
                showLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun logout() {
        val sharedPrefs = getSharedPreferences("LudaPrefs", MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("isLoggedIn", false).apply()

        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}