package com.example.wastemangement.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.snapshots


class NotificationFragment : Fragment() {

private  lateinit var mauth:FirebaseAuth
private lateinit var dbrefNotify: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     mauth=FirebaseAuth.getInstance()
        dbrefNotify= FirebaseDatabase.getInstance().getReference("ToNotify")
        val root =  inflater.inflate(R.layout.fragment_notification, container, false)
        val button = root.findViewById<Button>(R.id.sendbtn)
        button.setOnClickListener {

            sendNotification()
        }

        return root
    }

    private fun sendNotification() {

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(requireContext(), "saransh")
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
                val notificationManager = NotificationManagerCompat.from(requireContext())
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
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