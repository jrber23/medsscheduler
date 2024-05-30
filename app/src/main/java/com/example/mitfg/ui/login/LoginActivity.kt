/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.login

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityLoginBinding
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.ui.register.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

/**
 * The login activity that shows up the UI
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    // Automatically generated binding class for activity_main.xml
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    // The Firebase Authentication instance
    private val auth: FirebaseAuth = Firebase.auth

    /**
     * All the actions required to swap to the Main Activity
     */
    private fun swapToMainScreen() {
        // Creates an intent that loads the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Finishes the current activity and starts the new one
        finish()
        startActivity(intent)
    }

    private fun swapToRegisterScreen() {
        // Creates an intent that loads the RegisterActivity
        val intent = Intent(this, RegisterActivity::class.java)

        // Finishes the current activity and starts the new one
        finish()
        startActivity(intent)
    }

    /**
     * All the stuff to create the LoginActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the layoutInflater to Inflate the LoginActivity
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bLogin.setOnClickListener {
            /* If all the fields has been filled, then checks if the email doesnt contains any uppercase
               letter. Else, shows a pop up */
            if (!fieldsEmpty()) {
                if (binding.tietEmail.text!!.any { it.isUpperCase() }) {
                    val message = getString(R.string.no_upper_cases_at_email)

                    showPopUp(message)
                } else {
                    val email = binding.tietEmail.text.toString()
                    val password = binding.tietPassword.text.toString()

                    // Sign in with the received email and password
                    signInWithEmailAndPassword(email, password)
                }
            } else {
                val message = getString(R.string.emptyFieldsLoginOrRegisterMessage)

                showPopUp(message)
            }
        }

        binding.tvRegisterLink.setOnClickListener {
            swapToRegisterScreen()
        }
    }

    /**
     * Shows a pop up with the given message
     * @param message The message to show to the user
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
     * Checks that every text field has been filled
     * @return True if every text field has been filled
     *         False otherwise
     */
    private fun fieldsEmpty(): Boolean {
        return (binding.tietEmail.text.toString().isEmpty() ||
                binding.tietPassword.text.toString().isEmpty())
    }

    /**
     * When the activity starts, it checks if the current
     * user is null or not. If is not null, it swaps to the main
     * screen. Else, the login activity is shown.
     */
    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            swapToMainScreen()
        }
    }

    /**
     * With the Firebase Authentication instance, the user signs in
     * with the given email and password. If it fails, a pop up is shown.
     * @param email The email address given by the user
     * @param password The password given by the user
     */
    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail: SUCCESFUL!!")

                    swapToMainScreen()
                } else {
                    Log.d(TAG, "signInWithEmail: FAILURE!!", task.exception)

                    val message = getString(R.string.userNotFoundMessage)

                    showPopUp(message)
                }
            }
    }
}