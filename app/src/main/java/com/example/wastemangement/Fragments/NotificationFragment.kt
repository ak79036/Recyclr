package com.example.wastemangement.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.wastemangement.Notification.Notificationdata
import com.example.wastemangement.Notification.PushNotification
import com.example.wastemangement.Notification.RetrofitInstance
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val Topic="/topics/myTopic"
class NotificationFragment : Fragment() {

    val TAG:String="NotificationFragment"
private  lateinit var mauth:FirebaseAuth
private lateinit var dbrefNotify: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

          mauth=FirebaseAuth.getInstance()

            FirebaseMessaging.getInstance().subscribeToTopic(Topic)
//        dbrefNotify= FirebaseDatabase.getInstance().getReference("ToNotify")
        val root =  inflater.inflate(R.layout.fragment_notification, container, false)
        val button = root.findViewById<Button>(R.id.sendbtn)



        button.setOnClickListener {
           val title:String="Wastage"
            val message:String="GO And Check the wastage in Map"
           PushNotification(Notificationdata(title,message), Topic).also { it->
               sendNotification(it)


           }
        }

        return root
    }

    private fun sendNotification(notification:PushNotification)=CoroutineScope(Dispatchers.IO).launch {
  try {
       val response= RetrofitInstance.api.postNotification(notification)
              if(response.isSuccessful)
      {
          Log.d(TAG, "Response:")
      }
      else
      {
          Log.e(TAG,response.errorBody().toString())
      }
  }catch (e:Exception)
  {
      Log.e(TAG,e.toString())
  }


    }

//    private fun sendNotification() {
//
//        // Build the notification
//        val notificationBuilder = NotificationCompat.Builder(requireContext(), "saransh")
//            .setSmallIcon(R.drawable.baseline_home_24)
//            .setContentTitle("Pickup Called")
//            .setContentText("Click to view location")
//            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//
//        val flag1 = dbrefNotify.child("${mauth.uid}")
//        flag1.get().addOnSuccessListener {
//            snapshot->
//             val check1 = snapshot.child("isnotified").value
//             val check2 = snapshot.child("organization").value
//            if(check1 == false && check2== true ){
//                // Show the notification
//                val notificationManager = NotificationManagerCompat.from(requireContext())
//                if (ActivityCompat.checkSelfPermission(
//                        requireContext(),
//                        Manifest.permission.POST_NOTIFICATIONS
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    return@addOnSuccessListener
//                }
//                notificationManager.notify(1, notificationBuilder.build())
//                val check= notifyDataClass(isnotified = true)
//                dbrefNotify.child("${mauth.uid}").setValue(check)
//            }
//        }
//
//
//
//    }

}