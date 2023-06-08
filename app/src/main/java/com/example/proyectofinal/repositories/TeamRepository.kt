package com.example.proyectofinal.repositories

import com.google.firebase.database.*

class TeamsRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val teamsRef: DatabaseReference = database.getReference("teams")

    fun getRegions(callback: (List<String>) -> Unit) {
        teamsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val regions = mutableListOf<String>()
                for (teamSnapshot in snapshot.children) {
                    val region = teamSnapshot.child("region").getValue(String::class.java)
                    region?.let {
                        if (!regions.contains(it)) {
                            regions.add(it)
                        }
                    }
                }
                callback(regions)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de consulta a la base de datos
            }
        })
    }

    fun getCities(region: String, callback: (List<String>) -> Unit) {
        teamsRef.orderByChild("region").equalTo(region)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cities = mutableListOf<String>()
                    for (teamSnapshot in snapshot.children) {
                        val city = teamSnapshot.child("city").getValue(String::class.java)
                        city?.let {
                            if (!cities.contains(it)) {
                                cities.add(it)
                            }
                        }
                    }
                    callback(cities)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error de consulta a la base de datos
                }
            })
    }

    fun getTeams(region: String, city: String, callback: (List<String>) -> Unit) {
        teamsRef.orderByChild("region").equalTo(region)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val teams = mutableListOf<String>()
                    for (teamSnapshot in snapshot.children) {
                        val teamRegion = teamSnapshot.child("region").getValue(String::class.java)
                        val teamCity = teamSnapshot.child("city").getValue(String::class.java)
                        if (teamRegion == region && teamCity == city) {
                            val teamName = teamSnapshot.child("name").getValue(String::class.java)
                            teamName?.let {
                                teams.add(it)
                            }
                        }
                    }
                    callback(teams)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error de consulta a la base de datos
                }
            })
    }
}
