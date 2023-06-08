package com.example.proyectofinal.activities.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.viewmodels.CardsViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.ads.AdView


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CardsViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdView : AdView
    private var isCardViewExpanded = false

    private var x1 = 0F
    private var x2 = 0F
    private val MIN_DISTANCE = 150

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*MobileAds.initialize(this) {
            Log.e("MOBILEADS", "SUCCESFULL")
        }

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)*/

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        binding.cardView.setOnTouchListener { view, event ->
            if (!isCardViewExpanded) {
                handleTouchEvent(event)
            }
            true
        }

        binding.btnViewUserProfile.setOnClickListener {
            if (isCardViewExpanded) {
                // Contraer el CardView
                collapseCardView()
            } else {
                // Expandir el CardView
                expandCardView()
            }
        }

        viewModel.currentPlayer.observe(this) { player ->
            viewModel.imageHandler(player.photos.photo0)

            viewModel.imageLiveData.observe(this) { bitmap ->
                binding.imageView.setImageBitmap(bitmap)
            }

            createRadar(player)
            binding.dataUserName.text = player.name +" "+ player.surname
            binding.dataUserPosition.text = player.position
            binding.dataAge.text = player.born
            binding.dataWeight.text = player.weight.toString()
            binding.dataHeight.text = player.height.toString()
        }

        viewModel.currentScout.observe(this) { scout ->
            viewModel.imageHandler(scout.photos.photo0)

            viewModel.imageLiveData.observe(this) { bitmap ->
                binding.imageView.setImageBitmap(bitmap)
            }
            binding.dataUserName.text = scout.name +" "+ scout.surname
            binding.dataUserPosition.text = scout.teamID
            binding.dataAge.text = scout.born
        }


    }

    private fun expandCardView() {
        isCardViewExpanded = true
        showElements()
    }

    private fun collapseCardView() {
        isCardViewExpanded = false
        hideElements()
    }

    private fun showElements(){
        binding.lin1.visibility = View.VISIBLE
        binding.lin2.visibility = View.VISIBLE
        binding.llExpand1.visibility = View.VISIBLE
        binding.llExpand2.visibility = View.VISIBLE
        binding.llExpand3.visibility = View.VISIBLE
        binding.txtSkills.visibility = View.VISIBLE
        binding.radarChart.visibility = View.VISIBLE
    }

    private fun hideElements(){
        binding.lin1.visibility = View.GONE
        binding.lin2.visibility = View.GONE
        binding.llExpand1.visibility = View.GONE
        binding.llExpand2.visibility = View.GONE
        binding.llExpand3.visibility = View.GONE
        binding.txtSkills.visibility = View.GONE
        binding.radarChart.visibility = View.GONE
    }

    private fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX = x2 - x1
                if (deltaX > MIN_DISTANCE) {
                    handleSwipeRight()
                } else if (deltaX < -MIN_DISTANCE) {
                    handleSwipeLeft()
                }
            }
        }
        return true
    }

    private fun handleSwipeRight() {
        val animOut = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_out_right)
        binding.cardView.startAnimation(animOut)
        Toast.makeText(this, "Deslizo hacia la derecha", Toast.LENGTH_SHORT).show()

        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                viewModel.onSwipeRight()
                val animIn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_left)
                binding.cardView.startAnimation(animIn)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun handleSwipeLeft() {
        val animOut = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_out_left)
        binding.cardView.startAnimation(animOut)
        Toast.makeText(this, "Deslizo hacia la izquierda", Toast.LENGTH_SHORT).show()

        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                viewModel.onSwipeLeft()
                val animIn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_right)
                binding.cardView.startAnimation(animIn)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun createRadar (player: Player) {
        val labels = arrayOf("DRIBBLING", "SHOOTING", "DEFENDING",
            "SPEED", "PASSING", "PHYSICALITY")

        val entries = listOf(
            RadarEntry(player.skills.dribbling.toFloat()),
            RadarEntry(player.skills.shooting.toFloat()),
            RadarEntry(player.skills.defending.toFloat()),
            RadarEntry(player.skills.speed.toFloat()),
            RadarEntry(player.skills.passing.toFloat()),
            RadarEntry(player.skills.physicality.toFloat())
        )

        val radarDataSet = RadarDataSet(entries, "Datos")
        radarDataSet.color = Color.RED
        radarDataSet.fillColor = Color.RED
        radarDataSet.setDrawFilled(true)
        radarDataSet.setDrawValues(false)

        val radarData = RadarData(radarDataSet)
        radarData.setValueTextSize(15f)
        radarData.setValueTypeface(Typeface.DEFAULT_BOLD)

        binding.radarChart.data = radarData
        binding.radarChart.description.isEnabled = false
        binding.radarChart.legend.isEnabled = false
        binding.radarChart.webLineWidth = 2f
        binding.radarChart.webLineWidthInner = 2f

        val xAxis: XAxis = binding.radarChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return labels.getOrNull(value.toInt()) ?: ""
            }
        }

        val yAxis: YAxis = binding.radarChart.yAxis
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 5f
        yAxis.setDrawLabels(false)

        binding.radarChart.invalidate()
    }
}