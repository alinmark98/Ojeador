package com.example.proyectofinal.repositories

import com.example.proyectofinal.models.Subscription

class SubscriptionRepository {
    // Lista mutable de Suscripciones almacenadas en memoria
    private val subscriptionList: MutableList<Subscription> = mutableListOf()

    // Función para agregar una Suscripción al repositorio
    fun addSubscription(subscription: Subscription) {
        subscriptionList.add(subscription)
    }

    // Función para obtener todas las Suscripciones del repositorio
    fun getAllSubscriptions(): List<Subscription> {
        return subscriptionList.toList()
    }

    // Función para obtener las Suscripciones de un Usuario por su ID del repositorio
    fun getSubscriptionsByUserId(userId: Int): List<Subscription> {
        return subscriptionList.filter { it.userId == userId }
    }

    // Función para actualizar los datos de una Suscripción en el repositorio
    fun updateSubscription(subscription: Subscription) {
        val index = subscriptionList.indexOfFirst { it.userId == subscription.userId }
        if (index >= 0) {
            subscriptionList[index] = subscription
        }
    }

    // Función para eliminar una Suscripción del repositorio
    fun deleteSubscription(subscription: Subscription) {
        subscriptionList.remove(subscription)
    }
}