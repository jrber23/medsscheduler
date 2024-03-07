package com.example.mitfg.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.example.mitfg.LoginActivity
import com.example.mitfg.R
import com.example.mitfg.ui.AlarmReceiver.Companion.NOTIFICATION_ID
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import java.util.Calendar

class MainActivity : AppCompatActivity(), MenuProvider {

    private lateinit var auth : FirebaseAuth

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        addMenuProvider(this)

        // createChannel()
        // scheduleNotification()
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 5)
            set(Calendar.SECOND, 0)
        }

        val interval: Long = 60*1000*5

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            pendingIntent
        )

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_HOUR,
            pendingIntent
        )

        val city = hashMapOf(
            "name" to "London",
            "state" to "LON",
            "country" to "ENG",
        )

        val db = Firebase.firestore

        db.collection("cities").document("LON")
            .set(city)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        val lat = 51.5074
        val lng = 0.1278
        val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng))

        val updates: MutableMap<String, Any> = mutableMapOf(
            "geohash" to hash,
            "lat" to lat,
            "lng" to lng,
        )

        val londonRef = db.collection("cities").document("LON")
        londonRef.update(updates)
            .addOnCompleteListener {
                Log.d(TAG, "Geohash successfully written!")
            }

        val center = GeoLocation(51.5074, 0.1278)
        val radiusInM = 50.0 * 1000.0

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q = db.collection("cities")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                val matchingDocs: MutableList<DocumentSnapshot> = ArrayList()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val lat = doc.getDouble("lat")!!
                        val lng = doc.getDouble("lng")!!

                        // We have to filter out a few false positives due to GeoHash
                        // accuracy, but most will match
                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                        if (distanceInM <= radiusInM) {
                            matchingDocs.add(doc)
                        }
                    }
                }

                for (doc in matchingDocs) {
                    Log.d(TAG, doc.getDouble("lat").toString())
                    Log.d(TAG, doc.getDouble("lng").toString())
                    Log.d(TAG, doc.getString("name")!!)
                    Log.d(TAG, doc.getString("state")!!)
                    Log.d(TAG, doc.getString("country")!!)
                }
            }


    }


    /**
     * Método para crear un canal de notificaciones
     * NO BORRAR
     */
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
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
        startActivity(intent)
    }
}