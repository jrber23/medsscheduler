/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityMainBinding
import com.example.mitfg.domain.model.HealthAdvice
import com.example.mitfg.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuProvider, TextToSpeech.OnInitListener {

    private lateinit var _binding : ActivityMainBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var auth : FirebaseAuth

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var textToSpeech: TextToSpeech

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        createChannel()

        popUpSetUp()

        val cierto = this.intent.getBooleanExtra("alarmDialog", false)

        if (cierto) {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("ALARMA ABIERTA!!")
            builder.setMessage("Has abierto la alarma desde la notificación")

            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.healthAdvice.collect { advice ->
                    updateHealthAdvicePopup(advice)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    showDoctorNavBar()
                }
            }
        }

        textToSpeech = TextToSpeech(applicationContext, this)

        navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        binding.bottomNavigationView
        binding.bottomNavigationView.setupWithNavController(navController)

        setSupportActionBar(binding.materialToolbar)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.newAlarmFragment, R.id.medicinesListFragment, R.id.appointmentReservationFragment, R.id.pharmacyLocationsFragment, R.id.doctorAppointmentsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        addMenuProvider(this)

        if (intent.action == "addTakenDosage") {
            val idAlarm = intent.getLongExtra("idAlarm", -1)

            viewModel.addTakenDosage(idAlarm)
        }
    }

    private fun playWelcomeMessage() {
        textToSpeech.speak(getString(R.string.welcomeVoiceMessage), TextToSpeech.QUEUE_FLUSH, null, "1")
    }

    fun showDoctorNavBar() {
        binding.bottomNavigationView.menu.findItem(R.id.medicinesListFragment).isVisible =
            viewModel.userIsDoctor()

        binding.bottomNavigationView.menu.findItem(R.id.doctorAppointmentsFragment).isVisible =
            viewModel.userIsDoctor()

        binding.bottomNavigationView.menu.findItem(R.id.appointmentReservationFragment).isVisible =
            !viewModel.userIsDoctor()
    }

    private fun popUpSetUp() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.popup_layout, null)

        imageView = dialogView.findViewById(R.id.healthAdvice_image)
        titleTextView = dialogView.findViewById(R.id.healthAdvice_title)
        descriptionTextView = dialogView.findViewById(R.id.healthAdvice_description)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun updateHealthAdvicePopup(advice: HealthAdvice?) {
        Glide.with(this)
            .load(advice?.image)
            .override(400, 400)
            .into(imageView)

        titleTextView.text = advice?.title
        descriptionTextView.text = advice?.description
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            MY_CHANNEL_ID,
            "MySuperChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "SUSCRIBETE"
        }

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.signOut -> {
                signOut()
                true
            }
            else -> false
        }
    }

    private fun signOut() {
        auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)

        finish()

        startActivity(intent)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = setVoiceLanguage()

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language not supported")
            } else {
                playWelcomeMessage()
            }
        } else {
            Log.e(TAG, "Initialization failed")
        }
    }

    private fun setVoiceLanguage() : Int {
        val locale = Locale.getDefault().displayLanguage

        val result = when (locale) {
            "English" -> textToSpeech.setLanguage(Locale("en", "US"))
            "Spanish" -> textToSpeech.setLanguage(Locale("es", "ES"))
            "Catalan" -> textToSpeech.setLanguage(Locale("ca", "ES"))
            else -> textToSpeech.setLanguage(Locale("es", "ES"))
        }

        return result
    }

    override fun onStop() {
        super.onStop()

        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()

        finish()
    }
}