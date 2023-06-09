package com.example.proyectofinal.models

data class Subscription(
    private val userId: Int,
    private val startDate: String,
    private val endDate: String,
    private val amount: Double,
    private val plan: String
) {
    fun getUserId(): Int {
        return userId
    }

    fun getStartDate(): String {
        return startDate
    }

    fun getEndDate(): String {
        return endDate
    }

    fun getAmount(): Double {
        return amount
    }

    fun getPlan(): String {
        return plan
    }
}

