package com.example.proyectofinal.authenticationActivities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityRegisterBinding
import com.example.proyectofinal.fragments.Register.FirstFragment
import com.example.proyectofinal.fragments.Register.SecondFragment
import com.example.proyectofinal.fragments.Register.ThirdFragment
import com.example.proyectofinal.modelos.Player
import com.example.proyectofinal.viewmodels.RegisterViewModel


class RegisterActivity : AppCompatActivity(), FirstFragment.SendDataFromFragment1,
    SecondFragment.SendDataFromFragment2, ThirdFragment.SendDataFromFragment3 {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var player: Player
    private var firstFragChecked: Boolean = false
    private var secondFragChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

        player = Player()
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> FirstFragment()
                1 -> SecondFragment()
                2 -> ThirdFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }

    override fun sendDataFirstFragment(name: String?, surname: String?,
                                       birthday: String?, email: String?,
                                       password: String?, location: String?,
                                       firstFragConfirmed: Boolean) {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 0
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        firstFragChecked = firstFragConfirmed
        player.name = name ?: ""
        player.surname = surname ?: ""
        player.born = birthday ?: ""
        player.email = email ?: ""
        player.password = password ?: ""
        player.location = location ?: ""
    }

    override fun sendDataSecondFragment(gender: String?, position: String?, height: Int?,
                                        weight: Int?, description: String?,
                                        secondFragConfirmed: Boolean) {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 1
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        secondFragChecked = secondFragConfirmed
        player.gender = gender ?: ""
        player.position = position ?: ""
        player.height = height ?: 0
        player.weight = weight ?: 0
        player.description = description ?: ""
    }

    override fun checkThirdFragment() {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 2
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment is ThirdFragment) {
            fragment.dataReceived(firstFragChecked, secondFragChecked)
        }
    }

    override fun sendDataThirdFragment(images: List<Bitmap>, skills: HashMap<String, Int>) {
        /*for (i in images.indices) {
            when (i) {
                0 -> player.photos.photo1 = images[i]
                1 -> player.photos.photo2 = images[i]
                2 -> player.photos.photo3 = images[i]
                3 -> player.photos.photo4 = images[i]
                else -> break // Salir del bucle si ya se asignaron los 4 elementos
            }
        }*/

        player.skills.dribbling = skills["dribbling"]!!
        player.skills.shooting = skills["shooting"]!!
        player.skills.defending = skills["defending"]!!
        player.skills.speed = skills["speed"]!!
        player.skills.passing = skills["passing"]!!
        player.skills.physicality = skills["physicality"]!!

        registerViewModel.registerPlayer(player,images)
    }

}