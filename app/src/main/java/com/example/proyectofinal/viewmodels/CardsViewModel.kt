package com.example.proyectofinal.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.models.Scout
import com.example.proyectofinal.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()
    val imageLiveData: LiveData<Bitmap> = _imageLiveData

    private var currentIndex = 0
    private val users = mutableListOf<Any>()

    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    private val _currentScout = MutableLiveData<Scout>()
    val currentScout: LiveData<Scout>
        get() = _currentScout

    init {
        CoroutineScope(Dispatchers.Main).launch {
            fetchUsers()
        }
    }

    private suspend fun fetchUsers() {
        val userType = userRepository.checkUserCollection()

        if (userType == "players") {
            userRepository.fetchScoutsUsers(
                onSuccess = { scouts ->
                    this.users.clear()
                    this.users.addAll(scouts)
                    Log.e("CRD-VIEW", "SCOUTS")

                    if (this.users.isNotEmpty()) {
                        val currentUser = this.users[currentIndex]
                        if (currentUser is Scout) {
                            _currentScout.value = currentUser
                        }
                    }
                },
                onFailure = { exception ->
                    Log.e("HOME-A", exception.toString())
                }
            )
        } else if (userType == "scouts") {
            userRepository.fetchPlayerUsers(
                onSuccess = { players ->
                    this.users.clear()
                    this.users.addAll(players)
                    Log.e("CRD-VIEW", "PLAYERS")

                    if (this.users.isNotEmpty()) {
                        val currentUser = this.users[currentIndex]
                        if (currentUser is Player) {
                            _currentPlayer.value = currentUser
                        }
                    }
                },
                onFailure = { exception ->
                    Log.e("HOME-A", exception.toString())
                }
            )
        }
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
        if (currentIndex == users.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
        val currentUser = users[currentIndex]
        if (currentUser is Player) {
            _currentPlayer.value = currentUser
        } else if (currentUser is Scout) {
            _currentScout.value = currentUser
        }
    }

    fun onSwipeRight() {
        if (currentIndex == 0) {
            currentIndex = users.size - 1
        } else {
            currentIndex--
        }
        val currentUser = users[currentIndex]
        if (currentUser is Player) {
            _currentPlayer.value = currentUser
        } else if (currentUser is Scout) {
            _currentScout.value = currentUser
        }
    }
}