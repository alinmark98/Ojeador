package com.example.proyectofinal.repositories

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.example.proyectofinal.modelos.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class PlayerRepository {
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    private val collectionRef = db.collection("players")

    fun registerPlayer(player: Player, bitmaps: List<Bitmap>, listener: OnPlayerRegisteredListener) {
        Log.d(player.name, "PRINCIPIO")
        db.collection("players")
            .add(player)
            .addOnSuccessListener { documentRef ->
                val userId = documentRef.id
                documentRef.update("userId", userId)
                    .addOnSuccessListener {
                        // Llama al método uploadPhotos pasando el userId y la lista de bitmaps
                        uploadPhotos(userId, bitmaps,
                            onSuccess = { photoUrls ->
                                // Aquí puedes realizar las acciones deseadas con las URLs de las fotos subidas
                                // Por ejemplo, mostrar un mensaje de éxito, actualizar la interfaz de usuario, etc.
                                listener.onPlayerRegistered()
                                Log.d("ADDSUCCESLISTENER", "SUCCES FOTOS AÑADIDAS")
                            },
                            onFailure = { exception ->
                                // Aquí puedes manejar el error de alguna manera
                                // Por ejemplo, mostrar un mensaje de error, registrar el error, etc.
                                listener.onRegistrationError(exception.message)
                                Log.d("FAILURE", "FAILURE")
                            }
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.d("FAILURE", "FAILURE")
                        listener.onRegistrationError(e.message)
                    }
            }
            .addOnFailureListener { e ->
                Log.d("FAILURE", "FAILURE")
                listener.onRegistrationError(e.message)
            }
    }

    private fun uploadPhotos(userId: String, bitmaps: List<Bitmap>, onSuccess: (List<String>) -> Unit, onFailure: (Exception) -> Unit) {
        val photoUrls = mutableListOf<String>()
        val totalPhotos = bitmaps.size
        var uploadedCount = 0

        for ((index, bitmap) in bitmaps.withIndex()) {
            val storageRef = storage.reference.child("$userId/photo${index + 1}.jpg")

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val photoUrl = uri.toString()
                    photoUrls.add(photoUrl)
                    uploadedCount++

                    if (uploadedCount == totalPhotos) {
                        onSuccess(photoUrls)
                    }
                }.addOnFailureListener { exception ->
                    onFailure(exception)
                }
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        }
    }

    fun checkEmailExists(email: String, onComplete: (Boolean) -> Unit) {
        collectionRef
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    val emailExists = querySnapshot != null && !querySnapshot.isEmpty
                    onComplete(emailExists)
                } else {
                    onComplete(false) // Se produjo un error al consultar Firestore
                }
            }
    }


    interface OnPlayerRegisteredListener {
        fun onPlayerRegistered()
        fun onRegistrationError(errorMessage: String?)
    }
}