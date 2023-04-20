package com.example.proyectofinal.authenticationActivities

import AuthViewModel
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAuthenticationBinding
import com.example.proyectofinal.databinding.ActivityRegisterBinding
import com.example.proyectofinal.viewmodels.RegisterViewModel
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var progressRegister = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.btnContinuar.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.BELOW, R.id.etSurname)
        }

        binding.btnContinuar.setOnClickListener {
            when (progressRegister) {
                1 -> {
                    if (validarDatosParte1()) {
                        //guardarDatosParte1()
                        binding.btnContinuar.layoutParams = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            addRule(RelativeLayout.BELOW, R.id.etPasswordCheck)
                        }
                        binding.progressBar.progress = progressRegister * 33
                        binding.etName.visibility = View.GONE
                        binding.etSurname.visibility = View.GONE
                        binding.etEmail.visibility = View.VISIBLE
                        binding.etPassword.visibility = View.VISIBLE
                        binding.etPasswordCheck.visibility = View.VISIBLE
                        progressRegister++
                    }
                }
                2 -> {
                    if (validarDatosParte2()) {
                        //guardarDatosParte2()
                        binding.btnContinuar.layoutParams = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            addRule(RelativeLayout.BELOW, R.id.linearControl)
                        }
                        binding.progressBar.progress = progressRegister * 33
                        binding.etEmail.visibility = View.GONE
                        binding.etPassword.visibility = View.GONE
                        binding.etPasswordCheck.visibility = View.GONE

                        binding.linearControl.visibility = View.VISIBLE
                        binding.linearDribbling.visibility = View.VISIBLE
                        binding.linearShooting.visibility = View.VISIBLE
                        progressRegister++
                    }
                }
                3 -> {
                    if (validarDatosParte3()) {
                        //guardarDatosParte3()
                        binding.progressBar.progress = progressRegister * 33
                        finish()
                    }
                }
            }
        }
    }

    private fun validarDatosParte1(): Boolean {
        return binding.etName.text.isNotEmpty() && binding.etSurname.text.isNotEmpty()
    }

    private fun validarDatosParte2(): Boolean {

        val email = binding.etEmail.text.toString().trim()
        val pass = binding.etPassword.text.toString().trim()
        val passCheck = binding.etPasswordCheck.text.toString().trim()

        // Validar que ambos campos de contraseña coincidan
        if (binding.etPassword.text.toString().trim() != binding.etPasswordCheck.text.toString().trim()) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar que el campo de email tenga un valor válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingresa un email válido", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar que todos los campos tengan datos no vacíos
        if (email.isEmpty() || pass.isEmpty() || passCheck.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        // Si todos los datos son válidos, retornar true
        return true
    }
    private fun validarDatosParte3(): Boolean {
        return true
    }
}