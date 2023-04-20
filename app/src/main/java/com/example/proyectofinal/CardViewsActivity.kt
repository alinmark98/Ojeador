package com.example.proyectofinal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.databinding.ActivityCardViewsBinding
import com.example.proyectofinal.viewmodels.CardsViewModel


class CardViewsActivity : AppCompatActivity() {

    private lateinit var viewModel: CardsViewModel
    private lateinit var binding: ActivityCardViewsBinding

    private var x1 = 0F
    private var x2 = 0F
    private val MIN_DISTANCE = 150

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        binding.cardView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    val deltaX = x2 - x1
                    if (deltaX > MIN_DISTANCE) {
                        // deslizar hacia la derecha
                        val animOut = AnimationUtils.loadAnimation(this@CardViewsActivity, R.anim.slide_out_right)
                        binding.cardView.startAnimation(animOut)
                        Toast.makeText(this, "Deslizo hacia la derecha", Toast.LENGTH_SHORT).show()

                        animOut.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {}
                            override fun onAnimationEnd(animation: Animation?) {
                                viewModel.onSwipeRight()
                                val animIn = AnimationUtils.loadAnimation(this@CardViewsActivity, R.anim.slide_in_left)
                                binding.cardView.startAnimation(animIn)
                            }


                            override fun onAnimationRepeat(animation: Animation?) {}
                        })

                        Toast.makeText(this, "Deslizo hacia la derecha 2", Toast.LENGTH_SHORT).show()

                        true
                    } else if (deltaX < -MIN_DISTANCE) {
                        // deslizar hacia la izquierda
                        val animOut = AnimationUtils.loadAnimation(this@CardViewsActivity, R.anim.slide_out_left)
                        binding.cardView.startAnimation(animOut)

                        Toast.makeText(this, "Deslizo hacia la izquierda", Toast.LENGTH_SHORT).show()
                        animOut.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {}

                            override fun onAnimationEnd(animation: Animation?) {
                                viewModel.onSwipeLeft()
                                val animIn = AnimationUtils.loadAnimation(this@CardViewsActivity, R.anim.slide_in_right)
                                binding.cardView.startAnimation(animIn)
                            }

                            override fun onAnimationRepeat(animation: Animation?) {}
                        })
                        Toast.makeText(this, "Deslizo hacia la izquierda 2", Toast.LENGTH_SHORT).show()

                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }

        viewModel.currentPlayer.observe(this) { player ->
            val drawable = ContextCompat.getDrawable(this, R.drawable.user_profile)
            binding.imageView.setImageDrawable(drawable)

            binding.textViewName.text = player.name
            binding.textViewInfo.text = player.surname
        }
    }
}