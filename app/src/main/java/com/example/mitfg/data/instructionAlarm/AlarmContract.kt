/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm

/**
 * Object where every name column of the alarms table is assigned
 */
object AlarmContract {

    const val DB_NAME = "SettedAlarmsDB"

    object AlarmTable {
        const val TABLE_NAME = "Alarms"

        const val COLUMN_ID = "id"
        const val COLUMN_MEDICINE_NAME = "medicineName"
        const val COLUMN_MEDICINE_PRESENTATION = "medicinePresentation"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_FREQUENCY = "frequency"
        const val COLUMN_HOUR_START = "hourStart"
        const val COLUMN_MINUTE_START = "minuteStart"
        const val COLUMN_TOTAL_DOSAGES = "totalDosages"
        const val COLUMN_TAKEN_DOSAGES = "takenDosages"
    }

}