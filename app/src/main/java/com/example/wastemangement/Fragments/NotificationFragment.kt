package com.example.wastemangement.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.wastemangement.DataClass.collectionRequest
import com.example.wastemangement.Notification.Notificationdata
import com.example.wastemangement.Notification.PushNotification
import com.example.wastemangement.Notification.RetrofitInstance
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val Topic="/topics/myTopic"
class NotificationFragment : Fragment() {

    val TAG:String="NotificationFragment"
private  lateinit var mauth:FirebaseAuth
private lateinit var mdatabaseref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_notification, container, false)
        val button = root.findViewById<Button>(R.id.sendbtn)
        button.setOnClickListener {
            val token =
                "fyDXmPkQSsefA7G3ocu9ZS:APA91bHs3i1E7fFI16HKQjUQ7eiN683jxsiT5RunelgSl6krydVn9Rhy4SY2UaZr_9lnvbZwWWYZ7sNeBENM2MRW9Pn-8BcRPez56TUlVWW7ya_XhFylzWrgvRmAbtUkxKMcosbnM4K_"
            PushNotification(Notificationdata("waste", "please collect the data from there"), token)
                .also { it ->
                    sendNotification(it)
                }
        }
//        mauth=FirebaseAuth.getInstance()
//       mdatabaseref=FirebaseDatabase.getInstance().getReference("CollectionRequests")
//        button.setOnClickListener {
//            mdatabaseref.addValueEventListener(object :ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for(snapshot2 in snapshot.children)
//                    {
//                        val collection= snapshot2.getValue(collectionRequest::class.java)
//                        if(collection!=null) {
//                            val bioagent=collection.bioAgency.fcmtoken
//                            val city=collection.city
//                            if(bioagent.isNotEmpty())
//                            {
//                                val title:String="Biodegradable Waste"
//                                val message:String="This $city Area has Biodegradable Waste.Please Come and Collect That Waste"
//                                PushNotification(Notificationdata(title, message), bioagent).also { it->
//                                    sendNotification(it)
//                                }
//                            }
//                            val nonbioagent = collection.nonBioAgency.fcmtoken
//                            if(nonbioagent.isNotEmpty())
//                            {
//                                val title:String ="NonBioDegradable Waste"
//                                val message:String ="This $city Area has NonBiodegradable Waste.Please Come and Collect That Waste"
//                                PushNotification(Notificationdata(title, message), nonbioagent).also { it->
//                                    sendNotification(it)
//
//
//                                }
//                            }
//                            val recagent =collection.recAgency.fcmtoken
//                            if(recagent.isNotEmpty())
//                            {
//                                val title:String ="Recyclable Waste"
//                                val message:String ="This $city Area has Recyclable Waste.Please Come and Collect That Waste"
//                                PushNotification(Notificationdata(title, message), recagent).also { it->
//                                    sendNotification(it)
//
//
//                                }
//                            }
//                            val ewagent =collection.eAgency.fcmtoken
//                            if(ewagent.isNotEmpty())
//                            {
//                                val title:String ="E-Waste"
//                                val message:String ="This $city Area has Electronics Waste.Please Come and Collect That Waste"
//                                PushNotification(Notificationdata(title, message), ewagent).also { it->
//                                    sendNotification(it)
//
//
//                                }
//                            }
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }



//            FirebaseMessaging.getInstance().subscribeToTopic(Topic)
//        dbrefNotify= FirebaseDatabase.getInstance().getReference("ToNotify")





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