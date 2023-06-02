package com.example.proyectofinal.activities.terms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.proyectofinal.R

class TermsAndConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        val backButton = findViewById<ImageButton>(R.id.btnBackButton)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}