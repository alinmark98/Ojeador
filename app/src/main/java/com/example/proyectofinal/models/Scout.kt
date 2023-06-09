package com.example.proyectofinal.models


data class Scout(
    private var id: String = "",
    private var name: String = "",
    private var surname: String = "",
    private var email: String = "",
    private var gender: String = "",
    private var born: String = "",
    private var location: String = "",
    private var teamID: String = "",
    private var category: String = "",
    private var description: String = "",
    private var rol: String= "",
    private var visibility: Int = 0,
    private var subscription: Int = 0,
    private var photos: Photos = Photos()
) {
    constructor() : this("", "", "", "", "", "",
        "", "", "", "", "", 0, 0, Photos())

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

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getGender(): String {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getBorn(): String {
        return born
    }

    fun setBorn(born: String) {
        this.born = born
    }

    fun getLocation(): String {
        return location
    }

    fun setLocation(location: String) {
        this.location = location
    }

    fun getTeamID(): String {
        return teamID
    }

    fun setTeamID(teamID: String) {
        this.teamID = teamID
    }

    fun getCategory(): String {
        return category
    }

    fun setCategory(category: String) {
        this.category = category
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

    fun getPhotos(): Photos {
        return photos
    }

    fun setPhotos(photos: Photos) {
        this.photos = photos
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