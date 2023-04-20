package com.example.proyectofinal.repositories

import com.example.proyectofinal.modelos.Scout

class ScoutRepository {
    // Lista mutable de Scouts almacenados en memoria
    private val scoutList: MutableList<Scout> = mutableListOf()

    // Función para agregar un Scout al repositorio
    fun addScout(scout: Scout) {
        scoutList.add(scout)
    }

    // Función para obtener todos los Scouts del repositorio
    fun getAllScouts(): List<Scout> {
        return scoutList.toList()
    }

    // Función para obtener un Scout por su correo electrónico del repositorio
    fun getScoutByEmail(email: String): Scout? {
        return scoutList.find { it.email == email }
    }

    // Función para actualizar los datos de un Scout en el repositorio
    fun updateScout(scout: Scout) {
        val index = scoutList.indexOfFirst { it.email == scout.email }
        if (index >= 0) {
            scoutList[index] = scout
        }
    }

    // Función para eliminar un Scout del repositorio
    fun deleteScout(scout: Scout) {
        scoutList.remove(scout)
    }
}