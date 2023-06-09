package com.example.proyectofinal.models

class Player(
    private var id: String = "",
    private var name: String = "",
    private var surname: String = "",
    private var born: String = "",
    private var email: String = "",
    private var location: String = "",
    private var gender: String = "",
    private var position: String = "",
    private var height: Double = 0.0,
    private var weight: Double = 0.0,
    private var description: String = "",
    private var rol: String = "",
    private var visibility: Int = 1,
    private var subscription: Int = 0,
    private var skills: Skills = Skills(),
    private var photos: Photos = Photos()

) {
    constructor() : this("")


    fun getId(): String{
        return id
    }

    fun setId(id: String){
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getSurname(): String {
        return surname
    }

    fun setSurname(surname: String) {
        this.surname = surname
    }

    fun getBorn(): String {
        return born
    }

    fun setBorn(born: String) {
        this.born = born
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getLocation(): String {
        return location
    }

    fun setLocation(location: String) {
        this.location = location
    }

    fun getGender(): String {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getPosition(): String {
        return position
    }

    fun setPosition(position: String) {
        this.position = position
    }

    fun getHeight(): Double {
        return height
    }

    fun setHeight(height: Double) {
        this.height = height
    }

    fun getWeight(): Double {
        return weight
    }

    fun setWeight(weight: Double) {
        this.weight = weight
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getRol(): String {
        return rol
    }

    fun setRol(rol: String) {
        this.rol = rol
    }

    fun getVisibility(): Int {
        return visibility
    }

    fun setVisibility(visibility: Int) {
        this.visibility = visibility
    }

    fun getSubscription(): Int {
        return subscription
    }

    fun setSubscription(subscription: Int) {
        this.subscription = subscription
    }

    fun getSkills(): Skills {
        return skills
    }

    fun setSkills(skills: Skills) {
        this.skills = skills
    }

    fun getPhotos(): Photos {
        return photos
    }

    fun setPhotos(photos: Photos) {
        this.photos = photos
    }

    data class Skills(
        private var dribbling: Int = 0,
        private var shooting: Int = 0,
        private var defending: Int = 0,
        private var speed: Int = 0,
        private var passing: Int = 0,
        private var physicality: Int = 0
    ) {

        fun getDribbling(): Int {
            return dribbling
        }

        fun setDribbling(dribbling: Int) {
            this.dribbling = dribbling
        }

        fun getShooting(): Int {
            return shooting
        }

        fun setShooting(shooting: Int) {
            this.shooting = shooting
        }

        fun getDefending(): Int {
            return defending
        }

        fun setDefending(defending: Int) {
            this.defending = defending
        }

        fun getSpeed(): Int {
            return speed
        }

        fun setSpeed(speed: Int) {
            this.speed = speed
        }

        fun getPassing(): Int {
            return passing
        }

        fun setPassing(passing: Int) {
            this.passing = passing
        }

        fun getPhysicality(): Int {
            return physicality
        }

        fun setPhysicality(physicality: Int) {
            this.physicality = physicality
        }
    }

    data class Photos(
        private var photo0: String = "",
        private var photo1: String = "",
        private var photo2: String = "",
        private var photo3: String = ""
    ) {

        fun getPhoto0(): String {
            return photo0
        }

        fun setPhoto0(photo0: String) {
            this.photo0 = photo0
        }

        fun getPhoto1(): String {
            return photo1
        }

        fun setPhoto1(photo1: String) {
            this.photo1 = photo1
        }

        fun getPhoto2(): String {
            return photo2
        }

        fun setPhoto2(photo2: String) {
            this.photo2 = photo2
        }

        fun getPhoto3(): String {
            return photo3
        }

        fun setPhoto3(photo3: String) {
            this.photo3 = photo3
        }
    }
}
