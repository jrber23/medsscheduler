package com.example.mitfg.data.instructionAlarm

object AlarmContact {

    const val DB_NAME = "SettedAlarmsDB"

    object AlarmTable {
        const val TABLE_NAME = "Alarms";

        const val COLUMN_ID = "id";
        const val COLUMN_MEDICINE_NAME = "medicineName";
        const val COLUMN_QUANTITY = "quantity";
        const val COLUMN_FREQUENCY = "frequency";
        const val COLUMN_HOUR_START = "hourStart";
        const val COLUMN_MINUTE_START = "minuteStart";
    }

}