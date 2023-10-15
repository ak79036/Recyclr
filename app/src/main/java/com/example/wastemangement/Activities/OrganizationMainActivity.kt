package com.example.wastemangement.Activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.wastemangement.DataClass.collectionRequest
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrganizationMainActivity : AppCompatActivity() {
    private lateinit var mauth: FirebaseAuth
    private lateinit var dbrefNotify: DatabaseReference
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_main)

        mauth = FirebaseAuth.getInstance()
        dbrefNotify = FirebaseDatabase.getInstance().getReference("ToNotify")

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putBoolean("type", false)
        }.apply()

        //Toast.makeText(this, mauth.uid, Toast.LENGTH_SHORT).show()
        //for creating notification
//        createNotificationChannel()
//        sendnotification()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.orgFragContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel for Pickup"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("saransh", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            //Toast.makeText(this, mauth.uid, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendnotification() {
        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, "saransh")
            .setSmallIcon(R.drawable.baseline_home_24)
            .setContentTitle("Pickup Called")
            .setContentText("Click to view location")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)


        val flag1 = dbrefNotify.child("${mauth.uid}")
        flag1.get().addOnSuccessListener { snapshot ->
            val check1 = snapshot.child("isnotified").value
            val check2 = snapshot.child("organization").value
            Toast.makeText(this, "${check1},${check2}", Toast.LENGTH_SHORT).show()

            if (check1 == false && check2 == true) {
                // Show the notification

                Toast.makeText(this, mauth.uid, Toast.LENGTH_SHORT).show()

                val notificationManager = NotificationManagerCompat.from(this)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@addOnSuccessListener
                }
                notificationManager.notify(1, notificationBuilder.build())
                val check = notifyDataClass(isnotified = true)
                dbrefNotify.child("${mauth.uid}").setValue(check)
            }
        }
    }
}