/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mitfg.R
import com.example.mitfg.ui.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Splash screen shown when the app is opened.
 */
class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Using a coroutine for the delay
        GlobalScope.launch {
            delay(splashTimeOut)

            //Starting the login activity
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

            // Finishing the splash activity
            finish()
        }
    }
}