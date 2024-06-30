/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.main

import android.content.Intent
import android.os.Bundle
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
import com.example.mitfg.utils.NotificationChannelManager
import com.example.mitfg.utils.TextToSpeechHelper
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The main activity of the application
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuProvider {

    // Binding object for the activity
    private lateinit var _binding : ActivityMainBinding
    private val binding get() = _binding

    // ViewModel for the activity
    private val viewModel: MainViewModel by viewModels()

    // Firebase Authentication instance
    @Inject
    lateinit var auth : FirebaseAuth

    // UI elements for the popup
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView

    // Navigation components
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    // TextToSpeechHelper instance
    @Inject
    lateinit var voiceMessagePlayer : TextToSpeechHelper

    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout and set the content view
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        // If its not the first time this activity launches, the pop up won't be shown
        if (savedInstanceState == null) {
            popUpSetUp()

            // Collect health advice data and update popup
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.healthAdvice.collect { advice ->
                        updateHealthAdvicePopup(advice)
                    }
                }
            }
        }

        // Collect user data and update navigation bar
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    showDoctorNavBar()
                }
            }
        }

        // Setup navigation controller
        navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        // binding.bottomNavigationView as NavigationBarView
        binding.bottomNavigationView.setupWithNavController(navController)

        // Setup action bar
        setSupportActionBar(binding.materialToolbar)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.newAlarmFragment, R.id.medicinesListFragment, R.id.appointmentReservationFragment, R.id.pharmacyLocationsFragment, R.id.doctorAppointmentsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        addMenuProvider(this)

        // Handle "addTakenDosage" intent action
        if (intent.action.equals("addTakenDosage")) {
            val idAlarm = intent.getLongExtra("idAlarm", -1)
            val idAlarmToInt = idAlarm.toInt()

            notificationChannelManager.cancelNotification(idAlarmToInt)

            viewModel.addTakenDosage(idAlarm)
        }
    }

    /**
     * Depending on the user role, some sections in the navigation bar will be shown or not.
     * It will depend on if the user is a doctor or not.
     */
    fun showDoctorNavBar() {
        (binding.bottomNavigationView as NavigationBarView).menu.findItem(R.id.medicinesListFragment).isVisible =
            viewModel.userIsDoctor()

        (binding.bottomNavigationView as NavigationBarView).menu.findItem(R.id.doctorAppointmentsFragment).isVisible =
            viewModel.userIsDoctor()

        (binding.bottomNavigationView as NavigationBarView).menu.findItem(R.id.appointmentReservationFragment).isVisible =
            !viewModel.userIsDoctor()

        (binding.bottomNavigationView as NavigationBarView).menu.findItem(R.id.newAlarmFragment).isVisible =
            !viewModel.userIsDoctor()
    }

    /**
     * Creates the popup and shows it.
     */
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

    /**
     * Loads all the data of the received advice in every popup field.
     * @param advice the HealthAdvice object that contains every needed data
     * to load in the popup.
     */
    private fun updateHealthAdvicePopup(advice: HealthAdvice?) {
        Glide.with(this)
            .load(advice?.image)
            .override(400, 400)
            .into(imageView)

        titleTextView.text = advice?.title
        descriptionTextView.text = advice?.description
    }

    /**
     * Creates the notification channel where all the notifications will
     * be triggered by the established alarms.
     */
    private fun createNotificationChannel() {
        notificationChannelManager.createNotificationChannel()
    }

    /**
     * Creates the menu where the logging out option will be shown
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
    }

    /**
     * Sets the actions to do if any item is selected
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.signOut -> {
                signOut()
                true
            }
            else -> false
        }
    }

    /**
     * The user signs out and the main activity is finished
     */
    private fun signOut() {
        auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)

        finish()

        startActivity(intent)
    }
}