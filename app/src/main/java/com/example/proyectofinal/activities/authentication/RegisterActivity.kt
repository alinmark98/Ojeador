package com.example.proyectofinal.activities.authentication

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
import com.example.proyectofinal.fragments.register.player.*
import com.example.proyectofinal.fragments.register.scout.*
import com.example.proyectofinal.models.Player
import com.example.proyectofinal.models.Scout
import com.example.proyectofinal.viewmodels.RegisterViewModel
import kotlin.math.sign


class RegisterActivity : AppCompatActivity(), PlayerFirstFragment.PlayerSendDataFromF1,
    PlayerSecondFragment.PlayerSendDataFromF2, PlayerThirdFragment.PlayerSendDataFromF3,
    ScoutFirstFragment.ScoutSendDataFromF1, ScoutSecondFragment.ScoutSendDataFromF2,
    ScoutThirdFragment.ScoutSendDataFromF3{

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var player: Player
    private lateinit var scout: Scout
    private var toSendPwd: String = ""
    private var toSendEmail: String = ""
    private var firstFragChecked: Boolean = false
    private var secondFragChecked: Boolean = false
    private var accountType: String = ""
    private var signInWithGoogle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        signInWithGoogle = intent.getBooleanExtra("signInWithGoogle", false)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

        val intent = intent

        accountType = intent.getStringExtra("account_type").toString()

        player = Player()
        scout = Scout()
    }

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
                0 ->  { val playerFirstFragment = PlayerFirstFragment()
                        val bundle = Bundle()
                        bundle.putBoolean("signInWithGoogle", signInWithGoogle)
                        playerFirstFragment.arguments = bundle
                        playerFirstFragment
                }
                1 -> PlayerSecondFragment()
                2 -> PlayerThirdFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        private fun getScoutFragment(position: Int): Fragment {
            return when (position) {
                0 -> {  val scoutFirstFragment = ScoutFirstFragment()
                        val bundle = Bundle()
                        bundle.putBoolean("signInWithGoogle", signInWithGoogle)
                        scoutFirstFragment.arguments = bundle
                        scoutFirstFragment
                }
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
        if(accountType == "player"){
            firstFragChecked = firstFragConfirmed
            player.name = name ?: ""
            player.surname = surname ?: ""
            player.born = birthday ?: ""
            player.email = email ?: ""
            player.location = location ?: ""
            player.photos.photo0 = ""
            player.photos.photo1 = ""
            player.photos.photo2 = ""
            player.photos.photo3 = ""

            toSendPwd = password ?: ""
            toSendEmail = email ?: ""
        }else{
            Log.e("REGISTER-A" ,"SE ESTA REGISTRANDO UN OJEADOR")
        }
    }

    override fun playerSendDataSecondFragment(
        gender: String?, position: String?, height: Double?,
        weight: Double?, description: String?,
        secondFragConfirmed: Boolean) {

        if(accountType == "player"){
            secondFragChecked = secondFragConfirmed
            player.gender = gender ?: ""
            player.position = position ?: ""
            player.height = height ?: 0.0
            player.weight = weight ?: 0.0
            player.description = description ?: ""
            player.rol = "player"
            player.visibility = 1
            player.subscription = 0
        }else{
            Log.e("REGISTER-A" ,"SE ESTA REGISTRANDO UN OJEADOR")
        }
    }

    override fun playerCheckThirdFragment() {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 2
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment is PlayerThirdFragment) {
            fragment.dataReceived(firstFragChecked, secondFragChecked)
        }
    }

    override fun playerSendDataThirdFragment(images: List<Bitmap>, skills: HashMap<String, Int>) {
        if(accountType == "player"){
            player.skills.dribbling = skills["dribbling"]!!
            player.skills.shooting = skills["shooting"]!!
            player.skills.defending = skills["defending"]!!
            player.skills.speed = skills["speed"]!!
            player.skills.passing = skills["passing"]!!
            player.skills.physicality = skills["physicality"]!!

            if(signInWithGoogle){
                registerViewModel.registerUserWithGoogle(player, images)
            }else{
                registerViewModel.registerUser(player,toSendEmail,toSendPwd,images)
            }


        }else{
            Log.e("REGISTER-A" ,"SE ESTA REGISTRANDO UN OJEADOR")
        }
    }
    override fun scoutSendDataFirstFragment(name: String?, surname: String?,
                                             birthday: String?, email: String?,
                                             password: String?, firstFragConfirmed: Boolean) {
        if(accountType == "scout"){
            firstFragChecked = firstFragConfirmed
            scout.name = name ?: ""
            scout.surname = surname ?: ""
            scout.born = birthday ?: ""
            scout.email = email ?: ""

            scout.photos.photo0 = ""
            scout.photos.photo1 = ""
            scout.photos.photo2 = ""
            scout.photos.photo3 = ""

            toSendPwd = password ?: ""
            toSendEmail = email ?: ""
        }else{
            Log.e("REGISTER-A" ,"SE ESTA REGISTRANDO UN PLAYER")
        }
    }

    override fun scoutSendDataSecondFragment(team: String?,
        location: String?, secondFragConfirmed: Boolean) {

        if(accountType == "scout"){
            scout.teamID = team ?: ""
            scout.location = location ?: ""
            scout.visibility = 1
        }else{
            Log.e("REGISTER-A" ,"SE ESTA REGISTRANDO UN PLAYER")
        }
    }

    override fun scoutCheckThirdFragment() {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 2
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment is ScoutThirdFragment) {
            fragment.dataReceived(firstFragChecked, secondFragChecked)
        }
    }

    override fun scoutSendDataThirdFragment(images: List<Bitmap>, info: String?) {
        if(accountType == "scout"){
            if (info != null) {
                scout.description = info
            }
            if(signInWithGoogle){
                registerViewModel.registerUserWithGoogle(scout,images)
            }else{
                registerViewModel.registerUser(scout,toSendEmail,toSendPwd,images)
            }
        }else{
            Log.e("REGISTER-A" ,"SE ESTA REGISTRANDO UN PLAYER")
        }
    }
}