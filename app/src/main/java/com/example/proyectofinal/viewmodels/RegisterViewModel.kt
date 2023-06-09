package com.example.proyectofinal.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.repositories.UserRepository

class RegisterViewModel : ViewModel() {

    private val _showDatePicker = MutableLiveData<Boolean>()
    private val userRepository: UserRepository = UserRepository()

    fun registerUser(user: Any, email: String, pwd: String, bitmaps: List<Bitmap>) {
        // Realiza las validaciones necesarias en los datos del jugador antes de llamar al repositorio
        Log.d("REGISTERPLAYER", "REGISTERPLAYER")
        userRepository.registerUser(user, email, pwd, bitmaps, object : UserRepository.OnRegistrationCompleteListener {
            override fun onRegistrationSuccess() {
                Log.d("REGISTRO", "REGISTRO CORRECTO")
                // AQUI QUE TE LLEVE A OTA ACTIVIDAD
            }

            override fun onRegistrationFailure(exception: Exception?) {
                if (exception != null) {
                    Log.d("REGISTRO", exception.toString())
                }else{
                    Log.d("REGISTRO", "ERROR REGISTRO")
                }
            }

        })
    }
    fun registerUserWithGoogle(user: Any, bitmaps: List<Bitmap>) {
        // Realiza las validaciones necesarias en los datos del jugador antes de llamar al repositorio
        Log.d("REGISTERPLAYER", "REGISTERPLAYER")
        userRepository.registerUserWithGoogle(user, bitmaps, object : UserRepository.OnRegistrationCompleteListener {
            override fun onRegistrationSuccess() {
                Log.d("REGISTRO", "REGISTRO CORRECTO")
            }

            override fun onRegistrationFailure(exception: Exception?) {
                if (exception != null) {
                    Log.d("REGISTRO", exception.toString())
                }else{
                    Log.d("REGISTRO", "ERROR REGISTRO")
                }
            }

        })
    }

    fun checkEmailExists(email: String, onComplete: (Boolean) -> Unit) {
        userRepository.checkEmailExists(email, onComplete)
    }

    val showDatePicker: LiveData<Boolean>
        get() = _showDatePicker

    fun onBirthdayEditTextClicked() {
        _showDatePicker.value = true
    }

    fun onDatePickerDismissed() {
        _showDatePicker.value = false
    }


}