package com.example.proyectofinal.activities.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.example.proyectofinal.fragments.menu.Menu_Bottons_Fragment
import com.example.proyectofinal.fragments.profile.Main_Fragment
import com.example.proyectofinal.fragments.profile.Profile_Fragment
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.viewmodels.CardsViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity(), Menu_Bottons_Fragment.OnButtonClickListener {

    private lateinit var viewModel: CardsViewModel
    private lateinit var binding: ActivityMainBinding
    private var isCardViewExpanded = false
    private lateinit var mAdView : AdView

    private var x1 = 0F
    private var x2 = 0F
    private val MIN_DISTANCE = 150

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoadingIndicator()

        MobileAds.initialize(this) {
            Log.e("MOBILEADS", "SUCCESFULL")
        }

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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
            viewModel.imageHandler(player.getPhotos().getPhoto0())

            viewModel.imageLiveData.observe(this) { bitmap ->
                binding.imageView.setImageBitmap(bitmap)
            }

            createRadar(player)
            binding.dataUserName.text = player.getName() + " " + player.getSurname()
            binding.dataUserPosition.text = player.getPosition()
            binding.dataAge.text = player.getBorn()
            binding.dataWeight.text = player.getWeight().toString()
            binding.dataHeight.text = player.getHeight().toString()
        }

        viewModel.currentScout.observe(this) { scout ->
            viewModel.imageHandler(scout.getPhotos().getPhoto0())

            viewModel.imageLiveData.observe(this) { bitmap ->
                binding.imageView.setImageBitmap(bitmap)
            }
            binding.dataUserName.text = scout.getName() + " " + scout.getSurname()
            binding.dataUserPosition.text = scout.getTeamID()
            binding.dataAge.text = scout.getBorn()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingIndicator()
            } else {
                hideLoadingIndicator()
            }
        }

        val bottomNavigationFragment = Menu_Bottons_Fragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.bottomNavigationContainer, bottomNavigationFragment)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomNavigationContainer, fragment)
            .commit()
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
            RadarEntry(player.getSkills().getDribbling().toFloat()),
            RadarEntry(player.getSkills().getShooting().toFloat()),
            RadarEntry(player.getSkills().getDefending().toFloat()),
            RadarEntry(player.getSkills().getSpeed().toFloat()),
            RadarEntry(player.getSkills().getPassing().toFloat()),
            RadarEntry(player.getSkills().getPhysicality().toFloat())
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

    private fun showLoadingIndicator() {
        binding.linearPrincipal.visibility = View.GONE
        binding.loadingIcon.visibility = View.VISIBLE
        binding.loadingIcon.setIndeterminateDrawable(FadingCircle())
    }

    private fun hideLoadingIndicator() {
        binding.loadingIcon.visibility = View.GONE
        binding.linearPrincipal.visibility = View.VISIBLE
    }

    override fun onButtonClick(buttonId: Int) {
        // Maneja el clic del botón en la actividad principal
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Ocultar fragmento actual antes de mostrar uno nuevo
        val currentFragment = fragmentManager.findFragmentById(R.id.bottomNavigationContainer)

        when (buttonId) {
            R.id.button1 -> {
                replaceFragment(Main_Fragment())
                Toast.makeText(this, "FUNCA BTN1", Toast.LENGTH_SHORT).show()
            }
            R.id.button2 -> {
                Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show()
            }
            R.id.button3 -> {

            }
            R.id.button4 -> {
                replaceFragment(Profile_Fragment())
                binding.linearPrincipal.visibility = View.GONE
            }

        }
        fragmentTransaction.commit()
    }
}