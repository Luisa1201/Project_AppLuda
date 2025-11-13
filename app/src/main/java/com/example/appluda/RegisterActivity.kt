package com.example.appluda

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var btnLoginTab: Button
    private lateinit var btnRegisterTab: Button
    private lateinit var tvHaveAccount: TextView
    private lateinit var btnFacebook: ImageView
    private lateinit var btnGoogle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.btnRegister)
        btnLoginTab = findViewById(R.id.btnLoginTab)
        btnRegisterTab = findViewById(R.id.btnRegisterTab)
        tvHaveAccount = findViewById(R.id.tvHaveAccount)
        btnFacebook = findViewById(R.id.btnFacebook)
        btnGoogle = findViewById(R.id.btnGoogle)
    }

    private fun setupListeners() {
        // Botón de Registro
        btnRegister.setOnClickListener {
            performRegister()
        }

        // Tab de Login
        btnLoginTab.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        // Ya tiene cuenta
        tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Registro con Facebook
        btnFacebook.setOnClickListener {
            Toast.makeText(this, "Registro con Facebook próximamente", Toast.LENGTH_SHORT).show()
        }

        // Registro con Google
        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Registro con Google próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performRegister() {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validaciones
        if (fullName.isEmpty()) {
            etFullName.error = "Ingrese su nombre completo"
            etFullName.requestFocus()
            return
        }

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

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Confirme su contraseña"
            etConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Las contraseñas no coinciden"
            etConfirmPassword.requestFocus()
            return
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar datos del usuario
        val sharedPrefs = getSharedPreferences("LudaPrefs", MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putString("userName", fullName)
            putString("userEmail", email)
            putString("userPassword", password)
            putBoolean("isLoggedIn", true)
            apply()
        }

        Toast.makeText(this, "¡Registro exitoso! Bienvenido a LUDA", Toast.LENGTH_SHORT).show()

        // Ir al Home
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}