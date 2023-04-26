package com.example.proyectofinal.authenticationActivities

import AuthViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.CardViewsActivity
import com.example.proyectofinal.HomeActivity
import com.example.proyectofinal.databinding.ActivityAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        auth = FirebaseAuth.getInstance()

        setupObservers()
        setupListeners()
        reload()
    }
    override fun onStart() {
        super.onStart()

        // Registra el listener de estado de autenticación
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()

        // Remueve el listener de estado de autenticación
        auth.removeAuthStateListener(authStateListener)
    }

    private fun reload(){
        // Define el listener de estado de autenticación
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // El usuario está autenticado, navega a la pantalla principal
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // El usuario no está autenticado
            }
        }
    }

    private fun setupObservers() {
        authViewModel.signInSuccess.observe(this) { success ->
            if (success) {
                // Inicio de sesión exitoso, navegar a la pantalla de inicio
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        authViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        binding.btnSignIn.setOnClickListener {
            //signIn()
            val intent = Intent(this, CardViewsActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun signIn() {
        val email = binding.etUserEmail.text.toString()
        val password = binding.etUserPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        authViewModel.signIn(email, password) {
            Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToSignUp() {
        val email = binding.etUserEmail.text.toString()
        val password = binding.etUserPassword.text.toString()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        //authViewModel.signUp(email, password)
    }

    private fun checkSesion(){
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            // Hay una sesión iniciada, navegar a la pantalla de inicio
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}