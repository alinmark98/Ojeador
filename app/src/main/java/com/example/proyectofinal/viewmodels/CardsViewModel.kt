package com.example.proyectofinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.modelos.Player

class CardsViewModel : ViewModel() {

    private val players = mutableListOf(
        Player("Alice", "Description of Alice"),
        Player("Bob", "Description of Bob"),
        Player("Charlie", "Description of Charlie")
    )

    private var currentIndex = 0

    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    init {
        _currentPlayer.value = players[currentIndex]
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