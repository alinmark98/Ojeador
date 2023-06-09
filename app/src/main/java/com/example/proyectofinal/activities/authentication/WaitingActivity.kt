package com.example.proyectofinal.activities.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.activities.main.MainActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.repositories.UserRepository
import com.example.proyectofinal.viewmodels.WaitingViewModel

class WaitingActivity : AppCompatActivity() {

    private lateinit var viewModel: WaitingViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        viewModel = ViewModelProvider(this)[WaitingViewModel::class.java]
        userRepository = UserRepository()

        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val txtCounter: TextView = findViewById(R.id.txtCounter)

        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        progressBar.startAnimation(rotation)

        viewModel.counterText.observe(this) {
            txtCounter.visibility = View.VISIBLE
            txtCounter.text = it
        }

        viewModel.navigateToHome.observe(this) { navigate ->
            if (navigate) {
                navigateToHomeActivity()
            } else {
                userRepository.deleteUserAuthentication(
                    onSuccess = {
                        Log.e("WAITING", "ON-FAILURE")
                        navigateToAuthenticationActivity()
                    },
                    onFailure = {
                        Log.e("WAITING", "ON-FAILURE")
                        navigateToAuthenticationActivity()
                    }
                )
            }
        }

        viewModel.showToast.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAuthenticationActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }
}