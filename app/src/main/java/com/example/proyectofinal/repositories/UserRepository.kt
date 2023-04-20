package com.example.proyectofinal.repositories

import com.example.proyectofinal.modelos.User

class UserRepository {
    // Lista mutable de Usuarios almacenados en memoria
    private val userList: MutableList<User> = mutableListOf()

    // Función para agregar un Usuario al repositorio
    fun addUser(user: User) {
        userList.add(user)
    }

    // Función para obtener todos los Usuarios del repositorio
    fun getAllUsers(): List<User> {
        return userList.toList()
    }

    // Función para obtener un Usuario por su correo electrónico del repositorio
    fun getUserByEmail(email: String): User? {
        return userList.find { it.email == email }
    }

    // Función para actualizar los datos de un Usuario en el repositorio
    fun updateUser(user: User) {
        val index = userList.indexOfFirst { it.email == user.email }
        if (index >= 0) {
            userList[index] = user
        }
    }

    // Función para eliminar un Usuario del repositorio
    fun deleteUser(user: User) {
        userList.remove(user)
    }
}