package com.example.proyectofinal.authenticationActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.proyectofinal.HomeActivity
import com.example.proyectofinal.R

class WaitingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var timer: CountDownTimer
    private lateinit var progressBar: ProgressBar
    private lateinit var txtCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        auth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.progressBar)
        txtCounter = findViewById(R.id.txtCounter)

        // Iniciar la animación de rotación del ProgressBar
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        progressBar.startAnimation(rotation)

        // Iniciar el temporizador de cuenta regresiva de 5 minutos
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtCounter.visibility = View.VISIBLE
                txtCounter.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                // Si se acaba el tiempo, regresar a AuthenticationActivity
                goToAuthenticationActivity()
            }
        }
        timer.start()

        // Comprobar continuamente si el usuario ha confirmado el correo electrónico
        val currentUser = auth.currentUser
        val handler = Handler()
        val delay = 2000 // Retraso de 2 segundos
        val runnable = object : Runnable {
            override fun run() {
                currentUser?.reload()
                if (currentUser != null && currentUser.isEmailVerified) {
                    // Si el correo ha sido verificado, llamar al método createAccount
                    createAccount()
                } else {
                    // Si el correo no ha sido verificado, esperar y luego comprobar de nuevo
                    handler.postDelayed(this, delay.toLong())
                }
            }
        }
        handler.postDelayed(runnable, delay.toLong())
    }

    private fun createAccount() {
        Toast.makeText(this, "Correo confirmado, creando cuenta...", Toast.LENGTH_SHORT).show()

        // Simular un retraso de 2 segundos para demostrar la redirección a HomeActivity
        Handler().postDelayed({
            // Redirigir a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun goToAuthenticationActivity() {
        // Regresar a AuthenticationActivity
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener el temporizador cuando la actividad se destruye
        timer.cancel()
    }
}