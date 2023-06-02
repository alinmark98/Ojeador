package com.example.proyectofinal.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.modelos.User
import com.example.proyectofinal.repositories.UserRepository
import com.google.firebase.firestore.FirebaseFirestore

class CardsViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()
    val imageLiveData: LiveData<Bitmap> = _imageLiveData

    private var currentIndex = 0
    private val users = mutableListOf<User>()

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser

    init {
        fetchUsers()
    }

   private fun fetchUsers() {
       userRepository.fetchUsers(
           onSuccess = { players ->
               users.clear()
               users.addAll(players)
               Log.e("CRD-VIEW", users[currentIndex].skills.dribbling.toString())

               if (users.isNotEmpty()) {
                   _currentUser.value = users[currentIndex]
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
        if (currentIndex == users.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
        _currentUser.value = users[currentIndex]
    }

    fun onSwipeRight() {
        if (currentIndex == 0) {
            currentIndex = users.size - 1
        } else {
            currentIndex--
        }
        _currentUser.value = users[currentIndex]
    }
}