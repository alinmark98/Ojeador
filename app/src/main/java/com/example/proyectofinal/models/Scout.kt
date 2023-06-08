package com.example.proyectofinal.models


data class Scout(
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var gender: String = "",
    var location: String = "",
    var team: String = "",
    var category: String = "",
    var shield: String = "",
    var description: String = "",
    var rol: String= "",
    var visibility: Int = 0,
    var subscription: Int = 0,
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