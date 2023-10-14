package com.example.wastemangement.Activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.Fragments.ProfileFragmentViewModel
import com.example.wastemangement.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private val pfViewModel : ProfileFragmentViewModel by viewModels()
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

        // update user details
        signInUser()

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
    fun updateUserDetails(user: users) {
        pfViewModel.setName(user.name)
        pfViewModel.setImage(user.image)
        pfViewModel.setAddress(user.address)
    }

    fun signInUser(){

        /*dbrefuser.child("${firebaseAuth.uid}").get()
            .addOnSuccessListener {
                    snapshot->
                val imageURL = snapshot.child("image").value.toString()
                Glide.with(this@ProfileFragment)
                    .load(imageURL)
                    .placeholder(R.drawable.profilevector)
                    .centerCrop()
                    .circleCrop()
                    .into(iv_profile)

                Toast.makeText(activity,imageURL,Toast.LENGTH_SHORT).show()
            }*/

        mAuth = FirebaseAuth.getInstance()
        dbref = Firebase.database.reference
        val uid = mAuth.currentUser!!.uid
        dbref.child("Users").child(uid).get().addOnSuccessListener {
            //val loggedInUser = document.toObject(User::class.java)!!
            var taskObj = it.getValue() as HashMap<*, *>
            var loggedInUser = users()

            with(loggedInUser){
                name = taskObj ["name"].toString()
                image = taskObj ["image"].toString()
                address = taskObj ["address"].toString()
                updateUserDetails(users(name = name, image = image, address = address))
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
}