package com.example.wastemangement.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.wastemangement.Activities.MainActivity
import com.example.wastemangement.Activities.OrganizationMainActivity
import com.example.wastemangement.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.internal.notify
import kotlin.random.Random

private const val Channel_id= "my_channel"
class FirebaseService :FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this,OrganizationMainActivity::class.java)
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationid = Random.nextInt()

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNotificationChannel(notificationManager)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingintent =PendingIntent.getActivity(this,0,intent,
            FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val notification =NotificationCompat.Builder(this, Channel_id)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.baseline_message_24)
            .setAutoCancel(true)
            .setContentIntent(pendingintent)
            .build()
         notificationManager.notify(notificationid,notification)

    }
    @RequiresApi(Build.VERSION_CODES.O)

    private fun createNotificationChannel(notificationmanager:NotificationManager)
    {
        val channelName="channelName"
        val channel =NotificationChannel(Channel_id,channelName,IMPORTANCE_HIGH).apply {
            description= "My channel description"
            enableLights(true)
            lightColor =Color.GREEN

        }
        notificationmanager.createNotificationChannel(channel)


    }
}