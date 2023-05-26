package com.example.proyectofinal.modelos

import android.graphics.Bitmap

data class Player(
    var name: String = "",
    var surname: String = "",
    var born: String = "",
    var email: String = "",
    var password: String = "",
    var location: String = "",
    var gender: String = "",
    var position: String = "",
    var height: Int = 0,
    var weight: Int = 0,
    var description: String = "",
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
        var photo1: Bitmap? = null,
        var photo2: Bitmap? = null,
        var photo3: Bitmap? = null,
        var photo4: Bitmap? = null
    )
}
