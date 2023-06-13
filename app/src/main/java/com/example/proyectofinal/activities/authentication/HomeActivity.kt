package com.example.proyectofinal.activities.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.main.MainActivity
import com.example.proyectofinal.databinding.ActivityHomeBinding
import com.example.proyectofinal.handlers.InternetHandler
import com.example.proyectofinal.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class HomeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userRepository: UserRepository
    private lateinit var internetHandler: InternetHandler

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this@HomeActivity, gso)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userRepository = UserRepository()
        checkSession()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepository = UserRepository()

        internetHandler = InternetHandler()
        registerReceiver(internetHandler, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        binding.btnContinueEmail.setOnClickListener {
            if (InternetHandler.isInternetAvailable(this)) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }else{
                val dialog = internetHandler.createNoInternetDialog(this)
                dialog.show()
            }
        }

        binding.btnContinueGoogle.setOnClickListener {
            if (InternetHandler.isInternetAvailable(this)) {
                // Hay conexión a Internet
                signInWithGoogle()
            }else{
                val dialog = internetHandler.createNoInternetDialog(this)
                dialog.show()
            }

        }
    }

    private fun checkSession(){
        val currentUser = userRepository.getCurrentUser()
        if (currentUser != null) {
            // Hay una sesión iniciada, navegar a la pantalla de inicio
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                userRepository.firebaseAuthWithGoogle(account.idToken, object :
                    UserRepository.AuthCallback {
                    override fun onAuthenticationSuccess() {
                        // Acción a realizar cuando la autenticación es exitosa
                        val intent = Intent(this@HomeActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    override fun onNewUserRegistration() {
                        // Acción a realizar cuando es un nuevo usuario
                        val intent = Intent(this@HomeActivity, AccountSelectionActivity::class.java)
                        intent.putExtra("signInWithGoogle", true)
                        startActivity(intent)
                    }

                    override fun onAuthenticationFailure() {
                        // Acción a realizar cuando se produce un error
                        Log.e(TAG, "Google sign in failed")
                    }
                })
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetHandler)
    }
}
