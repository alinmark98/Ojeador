package com.example.proyectofinal.activities.authentication

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityRegisterBinding
import com.example.proyectofinal.fragments.register.player.*
import com.example.proyectofinal.fragments.register.scout.*
import com.example.proyectofinal.modelos.User
import com.example.proyectofinal.viewmodels.RegisterViewModel


class RegisterActivity : AppCompatActivity(), PlayerFirstFragment.PlayerSendDataFromF1,
    PlayerSecondFragment.PlayerSendDataFromF2, PlayerThirdFragment.PlayerSendDataFromF3,
    ScoutFirstFragment.ScoutSendDataFromF1, ScoutSecondFragment.ScoutSendDataFromF2,
    ScoutThirdFragment.ScoutSendDataFromF3{

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var user: User
    private var pwd: String = ""
    private var firstFragChecked: Boolean = false
    private var secondFragChecked: Boolean = false
    private var accountType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

        val intent = intent

        accountType = intent.getStringExtra("account_type").toString()

        Toast.makeText(this, accountType, Toast.LENGTH_SHORT).show()

        user = User()
    }

    /*private inner class ViewPagerAdapter(fm: FragmentManager) :
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
    }*/
    private inner class ViewPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when (accountType) {
                "player" -> getPlayerFragment(position)
                "scout" -> getScoutFragment(position)
                else -> throw IllegalArgumentException("Invalid account type: $accountType")
            }
        }

        private fun getPlayerFragment(position: Int): Fragment {
            return when (position) {
                0 -> PlayerFirstFragment()
                1 -> PlayerSecondFragment()
                2 -> PlayerThirdFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        private fun getScoutFragment(position: Int): Fragment {
            return when (position) {
                0 -> ScoutFirstFragment()
                1 -> ScoutSecondFragment()
                2 -> ScoutThirdFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }

    override fun playerSendDataFirstFragment(name: String?, surname: String?,
                                       birthday: String?, email: String?,
                                       password: String?, location: String?,
                                       firstFragConfirmed: Boolean) {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 0
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        firstFragChecked = firstFragConfirmed
        user.name = name ?: ""
        user.surname = surname ?: ""
        user.born = birthday ?: ""
        user.email = email ?: ""
        user.location = location ?: ""
        user.photos.photo0 = ""
        user.photos.photo1 = ""
        user.photos.photo2 = ""
        user.photos.photo3 = ""

        pwd = password ?: ""
    }

    override fun playerSendDataSecondFragment(
        gender: String?, position: String?, height: Double?,
        weight: Double?, description: String?,
        secondFragConfirmed: Boolean) {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 1
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        secondFragChecked = secondFragConfirmed
        user.gender = gender ?: ""
        user.position = position ?: ""
        user.height = height ?: 0.0
        user.weight = weight ?: 0.0
        user.description = description ?: ""
        user.rol = "player"
        user.visibility = 1
    }

    override fun playerCheckThirdFragment() {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 2
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment is PlayerThirdFragment) {
            fragment.dataReceived(firstFragChecked, secondFragChecked)
        }
    }

    override fun playerSendDataThirdFragment(images: List<Bitmap>, skills: HashMap<String, Int>) {

        user.skills.dribbling = skills["dribbling"]!!
        user.skills.shooting = skills["shooting"]!!
        user.skills.defending = skills["defending"]!!
        user.skills.speed = skills["speed"]!!
        user.skills.passing = skills["passing"]!!
        user.skills.physicality = skills["physicality"]!!

        registerViewModel.registerUser(user,pwd,images)
    }
    override fun scoutSendDataFirstFragment(name: String?, surname: String?,
                                             birthday: String?, email: String?,
                                             password: String?, location: String?,
                                             firstFragConfirmed: Boolean) {

    }

    override fun scoutSendDataSecondFragment(
        gender: String?, position: String?, height: Double?,
        weight: Double?, description: String?,
        secondFragConfirmed: Boolean) {

    }

    override fun scoutCheckThirdFragment() {

    }

    override fun scoutSendDataThirdFragment(images: List<Bitmap>, skills: HashMap<String, Int>) {

    }
}