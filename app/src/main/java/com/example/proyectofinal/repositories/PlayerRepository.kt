package com.example.proyectofinal.repositories

import com.example.proyectofinal.modelos.Player
import com.google.firebase.firestore.FirebaseFirestore

class PlayerRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Crear un nuevo jugador en la base de datos
    fun crearJugador(jugador: Player, callback: (Boolean, String) -> Unit) {
        val jugadorRef = firestore.collection("jugadores").document()
        jugadorRef.set(jugador)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, jugadorRef.id)
                } else {
                    callback(false, "")
                }
            }
    }

    // Obtener la lista de jugadores de la base de datos
    fun obtenerListaJugadores(callback: (Boolean, List<Player>) -> Unit) {
        firestore.collection("jugadores").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val jugadoresList = mutableListOf<Player>()
                    for (document in task.result!!) {
                        val jugador = document.toObject(Player::class.java)
                        jugadoresList.add(jugador)
                    }
                    callback(true, jugadoresList)
                } else {
                    callback(false, emptyList())
                }
            }
    }

    // Actualizar los datos de un jugador en la base de datos
    fun actualizarJugador(jugadorId: String, jugador: Player, callback: (Boolean) -> Unit) {
        firestore.collection("jugadores").document(jugadorId).set(jugador)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Eliminar un jugador de la base de datos
    fun eliminarJugador(jugadorId: String, callback: (Boolean) -> Unit) {
        firestore.collection("jugadores").document(jugadorId).delete()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Crear un nuevo jugador en la colección de skills de un jugador existente
    fun crearSkill(jugadorId: String, skills: Player.Skills, callback: (Boolean) -> Unit) {
        firestore.collection("jugadores").document(jugadorId).update("skills", skills)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Crear un nuevo jugador en la colección de photos de un jugador existente
    fun crearPhotos(jugadorId: String, photos: Player.Photos, callback: (Boolean) -> Unit) {
        firestore.collection("jugadores").document(jugadorId).update("photos", photos)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
}