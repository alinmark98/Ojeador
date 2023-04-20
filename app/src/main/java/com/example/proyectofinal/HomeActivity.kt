package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.proyectofinal.authenticationActivities.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnSignOut = findViewById<Button>(R.id.btnSignOut)
        val txtUser = findViewById<TextView>(R.id.txtUser)

        // Usar el nombre de usuario como desees, por ejemplo, mostrarlo en un TextView
        btnSignOut.setOnClickListener(){
            signOut()
            Toast.makeText(this, "Signed Out Succesfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun signOut() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        // Realizar acciones adicionales después de cerrar sesión, si es necesario
        val i = Intent(this, AuthenticationActivity::class.java)
        startActivity(i)
    }
}