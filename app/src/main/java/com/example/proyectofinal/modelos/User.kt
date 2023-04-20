package com.example.proyectofinal.modelos

data class User(
    val email: String,
    val number: String,
    val location: String,
    val password: String,
    val visibility: Boolean,
    val created_at: String
)