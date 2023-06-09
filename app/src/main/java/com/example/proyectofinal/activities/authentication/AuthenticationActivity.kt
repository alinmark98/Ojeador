package com.example.proyectofinal.activities.authentication

import AuthViewModel
import CustomDialog
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
import com.example.proyectofinal.activities.main.MainActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAuthenticationBinding
import com.example.proyectofinal.handlers.InternetHandler
import com.example.proyectofinal.repositories.UserRepository


class AuthenticationActivity : AppCompatActivity(), CustomDialog.OnDialogClickListener  {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userRepository: UserRepository

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityReceiver = InternetHandler()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)

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
                            // Para mostrar el diálogo con el mensaje de credenciales inválidas
                            val invalidCredentialsDialog = CustomDialog.newInstance(CustomDialog.MESSAGE_TYPE_INVALID_CREDENTIALS)
                            invalidCredentialsDialog.show(supportFragmentManager, "CustomDialog")
                        }
                    }
                }else{
                    // Para mostrar el diálogo con el mensaje de campos vacíos
                    val emptyFieldsDialog = CustomDialog.newInstance(CustomDialog.MESSAGE_TYPE_EMPTY_FIELDS)
                    emptyFieldsDialog.show(supportFragmentManager, "CustomDialog")

                }
            }else{
                val dialog = connectivityReceiver.createNoInternetDialog(this)
                dialog.show()
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(InternetHandler())
    }

}