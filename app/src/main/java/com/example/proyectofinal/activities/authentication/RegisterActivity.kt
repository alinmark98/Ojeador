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

        player = Player("")
        scout = Scout("")
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
        if (accountType == "player") {
            firstFragChecked = firstFragConfirmed
            player.setName(name ?: "")
            player.setSurname(surname ?: "")
            player.setBorn(birthday ?: "")
            player.setEmail(email ?: "")
            player.setLocation(location ?: "")
            player.getPhotos().setPhoto0("")
            player.getPhotos().setPhoto1("")
            player.getPhotos().setPhoto2("")
            player.getPhotos().setPhoto3("")

            toSendPwd = password ?: ""
            toSendEmail = email ?: ""
        } else {
            Log.e("REGISTER-A", "SE ESTA REGISTRANDO UN OJEADOR")
        }

    }

    override fun playerSendDataSecondFragment(
        gender: String?, position: String?, height: Double?,
        weight: Double?, description: String?,
        secondFragConfirmed: Boolean) {

        if (accountType == "player") {
            secondFragChecked = secondFragConfirmed
            player.setGender(gender ?: "")
            player.setPosition(position ?: "")
            player.setHeight(height ?: 0.0)
            player.setWeight(weight ?: 0.0)
            player.setDescription(description ?: "")
            player.setRol("player")
            player.setVisibility(1)
            player.setSubscription(0)
        } else {
            Log.e("REGISTER-A", "SE ESTA REGISTRANDO UN OJEADOR")
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
        if (accountType == "player") {
            player.getSkills().setDribbling(skills["dribbling"]!!)
            player.getSkills().setShooting(skills["shooting"]!!)
            player.getSkills().setDefending(skills["defending"]!!)
            player.getSkills().setSpeed(skills["speed"]!!)
            player.getSkills().setPassing(skills["passing"]!!)
            player.getSkills().setPhysicality(skills["physicality"]!!)

            if (signInWithGoogle) {
                registerViewModel.registerUserWithGoogle(player, images)
            } else {
                registerViewModel.registerUser(player, toSendEmail, toSendPwd, images)
            }
        } else {
            Log.e("REGISTER-A", "SE ESTA REGISTRANDO UN OJEADOR")
        }
    }
    override fun scoutSendDataFirstFragment(name: String?, surname: String?,
                                             birthday: String?, email: String?,
                                             password: String?, firstFragConfirmed: Boolean) {
        if (accountType == "scout") {
            firstFragChecked = firstFragConfirmed
            scout.setName(name ?: "")
            scout.setSurname(surname ?: "")
            scout.setBorn(birthday ?: "")
            scout.setEmail(email ?: "")

            scout.getPhotos().setPhoto0("")
            scout.getPhotos().setPhoto1("")
            scout.getPhotos().setPhoto2("")
            scout.getPhotos().setPhoto3("")

            toSendPwd = password ?: ""
            toSendEmail = email ?: ""
        } else {
            Log.e("REGISTER-A", "SE ESTA REGISTRANDO UN PLAYER")
        }

    }

    override fun scoutSendDataSecondFragment(team: String?,
        location: String?, secondFragConfirmed: Boolean) {

        if (accountType == "scout") {
            scout.setTeamID(team ?: "")
            scout.setLocation(location ?: "")
            scout.setVisibility(1)
        } else {
            Log.e("REGISTER-A", "SE ESTA REGISTRANDO UN PLAYER")
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
                scout.setDescription(info)
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