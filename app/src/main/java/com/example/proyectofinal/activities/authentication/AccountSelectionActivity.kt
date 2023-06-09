package com.example.proyectofinal.activities.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAccountSelectionBinding
import com.example.proyectofinal.databinding.ActivityHomeBinding

class AccountSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSelectionBinding
    private var signInWithGoogle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_selection)

        binding = ActivityAccountSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInWithGoogle = intent.getBooleanExtra("signInWithGoogle", false)

        binding.btnPlayer.setOnClickListener {
            Log.e("ACC-SELECT" , "BTN SCOUT")
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("account_type", "player")
            if(signInWithGoogle){
                intent.putExtra("signInWithGoogle", true)
            }
            startActivity(intent)
        }

        binding.btnScout.setOnClickListener {
            Log.e("ACC-SELECT" , "BTN SCOUT")
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("account_type", "scout")
            if(signInWithGoogle){
                intent.putExtra("signInWithGoogle", true)
            }
            startActivity(intent)
        }
    }
}