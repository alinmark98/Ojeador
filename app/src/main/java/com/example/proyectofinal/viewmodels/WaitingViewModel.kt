package com.example.proyectofinal.viewmodels

import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.repositories.UserRepository

class WaitingViewModel() : ViewModel() {

    private val userRepository = UserRepository()

    private val _counterText = MutableLiveData<String>()
    val counterText: LiveData<String> get() = _counterText

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> get() = _showToast

    private lateinit var timer: CountDownTimer
    private val handler = Handler()

    init {
        startTimer()
        checkEmailVerification()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _counterText.value = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                goToAuthenticationActivity()
            }
        }
        timer.start()
    }


    private fun checkEmailVerification() {
        val delay = 2000
        val runnable = object : Runnable {
            override fun run() {
                userRepository.checkEmailVerification { isEmailVerified ->
                    if (isEmailVerified) {
                        createAccount()
                    } else {
                        handler.postDelayed(this, delay.toLong())
                    }
                }
            }
        }
        handler.postDelayed(runnable, delay.toLong())
    }

    private fun createAccount() {
        _showToast.value = "Correo confirmado, creando cuenta..."
        Handler().postDelayed({
            _navigateToHome.value = true
        }, 2000)
    }

    private fun goToAuthenticationActivity() {
        _navigateToHome.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}