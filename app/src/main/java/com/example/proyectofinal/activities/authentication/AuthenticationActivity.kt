package com.example.proyectofinal.activities.authentication

import AuthViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.activities.main.MainActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.dialogs.CustomDialogFragment
import com.example.proyectofinal.databinding.ActivityAuthenticationBinding
import com.example.proyectofinal.repositories.UserRepository


class AuthenticationActivity : AppCompatActivity(), CustomDialogFragment.OnDialogClickListener  {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userRepository: UserRepository

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository()

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val passwordEditText = findViewById<EditText>(R.id.etUserPassword)
        val eyeSelectorDrawable = resources.getDrawable(R.drawable.pwd_visibility)

        eyeSelectorDrawable.setBounds(0, 0, eyeSelectorDrawable.intrinsicWidth, eyeSelectorDrawable.intrinsicHeight)

        binding.btnSignIn.setOnClickListener{
            val email = binding.etUserEmail.text.toString()
            val pwd = binding.etUserPassword.text.toString()
            userRepository.signInWithEmail(email, pwd){ success ->
                if(success){
                    val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else{
                    val dialogFragment = CustomDialogFragment()
                    dialogFragment.show(supportFragmentManager, "custom_dialog")
                }
            }
        }

        binding.btnCreateAcc.setOnClickListener{
            navigateToSignUp()
        }

        passwordEditText.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    passwordEditText.transformationMethod = null
                    passwordEditText.setSelection(passwordEditText.text.length)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                    passwordEditText.setSelection(passwordEditText.text.length)
                }
            }
            false
        }
    }

    override fun onOkClicked() {
        // Acciones a realizar cuando se hace clic en el botón "OK" del cuadro de diálogo
        Log.d( "AuthACTIV","Botón OK presionado en la actividad de autenticación")
    }

    private fun navigateToSignUp() {
        authViewModel.showDialog(this)
    }

}