package com.example.proyectofinal.modelos


data class User(
    var name: String = "",
    var surname: String = "",
    var born: String = "",
    var email: String = "",
    var location: String = "",
    var gender: String = "",
    var position: String = "",
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var description: String = "",
    var rol: String= "",
    var visibility: Int = 0,
    var skills: Skills = Skills(),
    var photos: Photos = Photos()
) {

    data class Skills(
        var dribbling: Int = 0,
        var shooting: Int = 0,
        var defending: Int = 0,
        var speed: Int = 0,
        var passing: Int = 0,
        var physicality: Int = 0
    )

    data class Photos(
        var photo0: String = "",
        var photo1: String = "",
        var photo2: String = "",
        var photo3: String = ""
    )
}