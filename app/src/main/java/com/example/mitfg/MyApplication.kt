/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The class where every component in the app is generated.
 */
@HiltAndroidApp
class MyApplication : Application()