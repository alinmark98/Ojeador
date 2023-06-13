package com.example.proyectofinal.activities.authentication

import CustomDialog
import CustomDialog.Companion.MESSAGE_TYPE_EMPTY_FIELDS
import CustomDialog.Companion.MESSAGE_TYPE_INVALID_CREDENTIALS
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.main.MainActivity
import com.example.proyectofinal.databinding.ActivityAuthenticationBinding
import com.example.proyectofinal.handlers.InternetHandler
import com.example.proyectofinal.repositories.UserRepository
import com.example.proyectofinal.viewmodels.AuthViewModel


class AuthenticationActivity : AppCompatActivity(), CustomDialog.OnDialogClickListener  {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var internetHandler: InternetHandler

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        internetHandler = InternetHandler()
        registerReceiver(internetHandler, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        userRepository = UserRepository()

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val passwordEditText = findViewById<EditText>(R.id.etUserPassword)
        val eyeSelectorDrawable = resources.getDrawable(R.drawable.pwd_visibility)

        eyeSelectorDrawable.setBounds(0, 0, eyeSelectorDrawable.intrinsicWidth, eyeSelectorDrawable.intrinsicHeight)

        binding.btnSignIn.setOnClickListener{
            if (InternetHandler.isInternetAvailable(this)) {
                val email = binding.etUserEmail.text.toString()
                val pwd = binding.etUserPassword.text.toString()
                if(email.isNotEmpty() && pwd.isNotEmpty()){
                    userRepository.signInWithEmail(email, pwd){ success ->
                        if(success){
                            val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }else{
                            showErrorDialog(MESSAGE_TYPE_INVALID_CREDENTIALS)
                        }
                    }
                }else{
                    showErrorDialog(MESSAGE_TYPE_EMPTY_FIELDS)
                }
            }else{
                val dialog = internetHandler.createNoInternetDialog(this)
                dialog.show()
            }
        }

        binding.btnCreateAcc.setOnClickListener{
            if (InternetHandler.isInternetAvailable(this)) {
                navigateToSignUp()
            }else{
                val dialog = internetHandler.createNoInternetDialog(this)
                dialog.show()
            }
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

    private fun showErrorDialog(messageType: Int) {
        val dialog = CustomDialog.newInstance(messageType)
        supportFragmentManager.beginTransaction().apply {
            add(dialog, "CustomDialog")
            commitNowAllowingStateLoss()
        }
    }
    override fun onOkClicked() {
        // Acciones a realizar cuando se hace clic en el botón "OK" del cuadro de diálogo
        Log.d( "AuthACTIV","Botón OK presionado en la actividad de autenticación")
    }

    private fun navigateToSignUp() {
        authViewModel.showDialog(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetHandler)
    }

}