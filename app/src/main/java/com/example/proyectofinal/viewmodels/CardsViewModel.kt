package com.example.proyectofinal.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.repositories.UserRepository

class CardsViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()
    val imageLiveData: LiveData<Bitmap> = _imageLiveData

    private var currentIndex = 0
    private val players = mutableListOf<Player>()

    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    init {
        fetchUsers()
    }

   private fun fetchUsers() {
       userRepository.fetchUsers(
           onSuccess = { players ->
               this.players.clear()
               this.players.addAll(players)
               Log.e("CRD-VIEW", this.players[currentIndex].skills.dribbling.toString())

               if (this.players.isNotEmpty()) {
                   _currentPlayer.value = this.players[currentIndex]
               }
           },
           onFailure = { exception ->
               Log.e("HOME-A", exception.toString())           }
       )
   }

    fun imageHandler(imagePath: String) {
        userRepository.downloadProfileImages(
            imagePath,
            { bitmap ->
                _imageLiveData.value = bitmap
            },
            { exception ->
                Log.e("HOME-A", exception.toString())
            }
        )
    }

    fun onSwipeLeft() {
        if (currentIndex == players.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
        _currentPlayer.value = players[currentIndex]
    }

    fun onSwipeRight() {
        if (currentIndex == 0) {
            currentIndex = players.size - 1
        } else {
            currentIndex--
        }
        _currentPlayer.value = players[currentIndex]
    }
}