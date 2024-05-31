/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.register

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityRegisterBinding
import com.example.mitfg.ui.main.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The Register activity that shows every content of registration UI to the user.
 */
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    // Binding object for accessing the views in the layout
    private lateinit var binding: ActivityRegisterBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // ViewModel instance for managing registration-related data
    private val viewModel: RegisterViewModel by viewModels()

    // Constant for the minimum password length
    private val PASSWORD_MIN_LENGTH = 8

    /**
     * Called when the activity is first created. Initializes binding, Firebase auth,
     * and sets up the registration button click listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Set the content view to the root of the binding
        val view = binding.root
        setContentView(view)

        // Set the click listener for the register button
        binding.bRegister.setOnClickListener {
            // If is there any empty field, shows an error message
            if (!fieldsEmpty()) {

                // Checks if the password length is sufficient
                if (binding.tietPassword.text!!.length < PASSWORD_MIN_LENGTH) {
                    val message = getString(R.string.password_length_insufficient)

                    showPopUp(message)
                }
                // Check if the email contains any uppercase letters
                else if (binding.tietEmail.text!!.any { it.isUpperCase() }) {
                    val message = getString(R.string.no_upper_cases_at_email)

                    showPopUp(message)
                } else {
                    // Extract user details
                    val email = binding.tietEmail.text.toString()
                    val password = binding.tietPassword.text.toString()
                    val isDoctor = binding.chkBoxIsUserDoctor.isChecked
                    val name = binding.etUserName.text.toString()
                    val surname = binding.etUserSurname.text.toString()

                    registerUser(email, password)

                    lifecycleScope.launch {
                        viewModel.addUser(name, surname, email, password, isDoctor)
                    }
                }
            } else {
                val message = getString(R.string.emptyFieldsLoginOrRegisterMessage)

                showPopUp(message)
            }
        }
    }

    /**
     * Checks if any of the input fields are empty.
     * @return true if any field is empty, false otherwise.
     */
    private fun fieldsEmpty(): Boolean {
        return (binding.tietEmail.text.toString().isEmpty() ||
                binding.tietPassword.text.toString().isEmpty() ||
                binding.etUserName.text.toString().isEmpty() ||
                binding.etUserSurname.text.toString().isEmpty())
    }

    /**
     * Displays a pop-up message using an AlertDialog.
     * @param message The message to be displayed in the pop-up.
     */
    fun showPopUp(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.accept, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.show()
    }

    /**
     * Navigates to the main screen (MainActivity).
     */
    private fun swapToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

    /**
     * Navigates to the doctor selection screen (DoctorSelectionActivity) if the user is not a doctor,
     * otherwise navigates to the main screen.
     */
    private fun swapToSelectDoctor() {
        if (!binding.chkBoxIsUserDoctor.isChecked) {
            val intent = Intent(this, DoctorSelectionActivity::class.java)

            startActivity(intent)

            finish()
        } else {
            swapToMainScreen()
        }
    }

    /**
     * Registers a new user with Firebase Authentication.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    swapToSelectDoctor()

                    Log.d(TAG, "User created successfully!!")
                } else {
                    val message = getString(R.string.registerFail)
                    showPopUp(message)

                    Log.d(TAG, "Something went wrong")
                }
            }
    }
}