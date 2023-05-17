package com.example.proyectofinal.authenticationActivities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityRegisterBinding
import com.example.proyectofinal.fragments.FirstFragment
import com.example.proyectofinal.fragments.SecondFragment
import com.example.proyectofinal.fragments.ThirdFragment
import com.example.proyectofinal.viewmodels.RegisterViewModel


class RegisterActivity : AppCompatActivity(), FirstFragment.SendDataFromFragment{

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
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

    override fun sendData(name: String?) {
        val tag = "android:switcher:" + R.id.viewPager + ":" + 2
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment is ThirdFragment) {
            Log.d("LLEGA", "METODO FINAL")
            if(name != null){
                Log.d("LLEGA", "NOT NULL")
                fragment.displayReceivedData(name)
            }
        }
    }

}