package com.example.proyectofinal.models


data class Team(
    private var name: String,
    private var city: String,
    private var region: String,
    private var shield: String,
    private var stadium: String
) {
    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getCity(): String {
        return city
    }

    fun setCity(city: String) {
        this.city = city
    }

    fun getRegion(): String {
        return region
    }

    fun setRegion(region: String) {
        this.region = region
    }

    fun getShield(): String {
        return shield
    }

    fun setShield(shield: String) {
        this.shield = shield
    }

    fun getStadium(): String {
        return stadium
    }

    fun setStadium(stadium: String) {
        this.stadium = stadium
    }
}
