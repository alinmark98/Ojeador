package com.example.proyectofinal.modelos

data class Player(
    val name: String = "",
    val surname: String = "",
    val age: Int = 0,
    val position: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val email: String = "",
    val location: String = "",
    val description: String = "",
    val skills: Skills = Skills(),
    val photos: Photos = Photos()
) {
    data class Skills(
        val heading: Int = 0,
        val control: Int = 0,
        val passing: Int = 0,
        val dribbling: Int = 0,
        val shooting: Int = 0
    )

    data class Photos(
        val photo1: String = "",
        val photo2: String = "",
        val photo3: String = "",
        val photo4: String = ""
    )
}
