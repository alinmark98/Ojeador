package com.example.proyectofinal.models


data class Scout(
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var gender: String = "",
    var born: String = "",
    var location: String = "",
    var teamID: String = "",
    var category: String = "",
    var description: String = "",
    var rol: String= "",
    var visibility: Int = 0,
    var subscription: Int = 0,
    var photos: Photos = Photos()
) {
    data class Photos(
        var photo0: String = "",
        var photo1: String = "",
        var photo2: String = "",
        var photo3: String = ""
    )
}