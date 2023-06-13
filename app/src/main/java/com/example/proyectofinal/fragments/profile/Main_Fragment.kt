package com.example.proyectofinal.fragments.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.example.proyectofinal.databinding.FragmentMainBinding
import com.example.proyectofinal.fragments.menu.Menu_Bottons_Fragment
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Main_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Main_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: CardsViewModel
    private lateinit var binding: FragmentMainBinding
    private var isCardViewExpanded = false
    private lateinit var mAdView : AdView
    private lateinit var cardView : CardView

    private var x1 = 0F
    private var x2 = 0F
    private val MIN_DISTANCE = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding = FragmentMainBinding.inflate(layoutInflater)
        val rootView = binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_, container, false)

        MobileAds.initialize(requireContext()) {
            Log.e("MOBILEADS", "SUCCESFULL")
        }

        cardView = view.findViewById(R.id.cardView)
        mAdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        viewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        cardView.setOnTouchListener { view, event ->
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

        viewModel.currentPlayer.observe(requireActivity()) { player ->
            viewModel.imageHandler(player.getPhotos().getPhoto0())

            viewModel.imageLiveData.observe(requireActivity()) { bitmap ->
                binding.imageView.setImageBitmap(bitmap)
            }

            createRadar(player)
            binding.dataUserName.text = player.getName() + " " + player.getSurname()
            binding.dataUserPosition.text = player.getPosition()
            binding.dataAge.text = player.getBorn()
            binding.dataWeight.text = player.getWeight().toString()
            binding.dataHeight.text = player.getHeight().toString()
        }

        viewModel.currentScout.observe(viewLifecycleOwner) { scout ->
            viewModel.imageHandler(scout.getPhotos().getPhoto0())

            viewModel.imageLiveData.observe(viewLifecycleOwner) { bitmap ->
                binding.imageView.setImageBitmap(bitmap)
            }
            binding.dataUserName.text = scout.getName() + " " + scout.getSurname()
            binding.dataUserPosition.text = scout.getTeamID()
            binding.dataAge.text = scout.getBorn()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingIndicator()
            } else {
                hideLoadingIndicator()
            }
        }


        return view
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
        val animOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
        binding.cardView.startAnimation(animOut)

        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                viewModel.onSwipeRight()
                val animIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
                binding.cardView.startAnimation(animIn)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun handleSwipeLeft() {
        val animOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)
        binding.cardView.startAnimation(animOut)

        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                viewModel.onSwipeLeft()
                val animIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Main_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Main_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}