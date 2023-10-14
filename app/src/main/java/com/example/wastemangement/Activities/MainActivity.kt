package com.example.wastemangement.Activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.wastemangement.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = this@MainActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(com.example.wastemangement.R.string.user_type), "user")
            apply()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        setupWithNavController(bottomNavigationView, navController)
        ActivityCompat.requestPermissions(this,
            (arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)),
            PackageManager.PERMISSION_GRANTED
        )

        var shouldNavigateToMapFragment = false
        val dataHomeFrag = intent
        val value = dataHomeFrag.getIntExtra("key", 0)

        if (value == 69) {
            shouldNavigateToMapFragment = true
        }

        if (shouldNavigateToMapFragment) {
            navController.navigate(R.id.ScanFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, true)
                .build())
        }

        //for creating notification
        createNotificationChannel()

    }

    private fun createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "My Channel"
            val descriptionText = "Channel for Pickup"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("saransh", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}