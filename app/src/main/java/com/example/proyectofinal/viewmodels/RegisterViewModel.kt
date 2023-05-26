package com.example.proyectofinal.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.modelos.Player
import com.example.proyectofinal.repositories.PlayerRepository

class RegisterViewModel : ViewModel() {

    private val _showDatePicker = MutableLiveData<Boolean>()
    private val playerRepository: PlayerRepository = PlayerRepository()

    fun registerPlayer(player: Player, bitmaps: List<Bitmap>,) {
        // Realiza las validaciones necesarias en los datos del jugador antes de llamar al repositorio
        Log.d("REGISTERPLAYER", "REGISTERPLAYER")
        playerRepository.registerPlayer(player,bitmaps, object : PlayerRepository.OnPlayerRegisteredListener {
            override fun onPlayerRegistered() {
                Log.d("REGISTRO", "REGISTRO CORRECTO")
                // AQUI QUE TE LLEVE A OTA ACTIVIDAD
            }

            override fun onRegistrationError(errorMessage: String?) {
                if (errorMessage != null) {
                    Log.d("REGISTRO", errorMessage)
                }else{
                    Log.d("REGISTRO", "ERROR REGISTRO")
                }
            }
        })
    }

    fun checkEmailExists(email: String, onComplete: (Boolean) -> Unit) {
        playerRepository.checkEmailExists(email, onComplete)
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