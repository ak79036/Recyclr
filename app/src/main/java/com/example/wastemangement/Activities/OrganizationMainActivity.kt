package com.example.wastemangement.Activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrganizationMainActivity : AppCompatActivity() {

    private  lateinit var mauth: FirebaseAuth
    private lateinit var dbrefNotify: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_main)
        mauth=FirebaseAuth.getInstance()
        dbrefNotify= FirebaseDatabase.getInstance().getReference("ToNotify")

        sendnotification()
    }

    private fun sendnotification(){
        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, "saransh")
            .setSmallIcon(R.drawable.baseline_home_24)
            .setContentTitle("Pickup Called")
            .setContentText("Click to view location")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)


        val flag1 = dbrefNotify.child("${mauth.uid}")
        flag1.get().addOnSuccessListener {
                snapshot->
            val check1 = snapshot.child("isnotified").value
            val check2 = snapshot.child("isOrganization").value
            if(check1 == false && check2== true ){
                // Show the notification
                val notificationManager = NotificationManagerCompat.from(this)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@addOnSuccessListener
                }
                notificationManager.notify(1, notificationBuilder.build())
                val check= notifyDataClass(isnotified = true)
                dbrefNotify.child("${mauth.uid}").setValue(check)
            }
        }
    }
}