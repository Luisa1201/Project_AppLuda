package com.example.appluda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnLoginTab: Button
    private lateinit var btnRegisterTab: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvNoAccount: TextView
    private lateinit var btnFacebook: ImageView
    private lateinit var btnGoogle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnLoginTab = findViewById(R.id.btnLoginTab)
        btnRegisterTab = findViewById(R.id.btnRegisterTab)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvNoAccount = findViewById(R.id.tvNoAccount)
        btnFacebook = findViewById(R.id.btnFacebook)
        btnGoogle = findViewById(R.id.btnGoogle)
    }

    private fun setupListeners() {
        // Botón de Login
        btnLogin.setOnClickListener {
            performLogin()
        }

        // Tab de Registro
        btnRegisterTab.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        // Olvidó contraseña
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Recuperación de contraseña próximamente", Toast.LENGTH_SHORT).show()
        }

        // No tiene cuenta
        tvNoAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        // Login con Facebook
        btnFacebook.setOnClickListener {
            Toast.makeText(this, "Login con Facebook próximamente", Toast.LENGTH_SHORT).show()
        }

        // Login con Google
        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Login con Google próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validaciones
        if (email.isEmpty()) {
            etEmail.error = "Ingrese su correo electrónico"
            etEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Ingrese un correo válido"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Ingrese su contraseña"
            etPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            etPassword.requestFocus()
            return
        }

        // Verificar credenciales guardadas
        val sharedPrefs = getSharedPreferences("LudaPrefs", MODE_PRIVATE)
        val savedEmail = sharedPrefs.getString("userEmail", "")
        val savedPassword = sharedPrefs.getString("userPassword", "")

        if (email == savedEmail && password == savedPassword) {
            // Login exitoso
            sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()

            Toast.makeText(this, "¡Bienvenido a LUDA!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}