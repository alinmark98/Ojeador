package com.example.proyectofinal.models

data class Subscription(
    val userId: Int,
    val startDate: String,
    val endDate: String,
    val amount: Double,
    val plan: String
)
