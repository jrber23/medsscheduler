package com.example.mitfg.ui.doctor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mitfg.databinding.ActivityDoctorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = binding.doctorNavHostFragment.getFragment<NavHostFragment>().navController
        binding.doctorBottomNavigationView
        binding.doctorBottomNavigationView.setupWithNavController(navController)
    }
}