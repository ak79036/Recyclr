package com.example.wastemangement.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemangement.Adapters.WasteItemAdapter
import com.example.wastemangement.DataClass.Agency
import com.example.wastemangement.DataClass.collectionRequest
import com.example.wastemangement.DataClass.organisation
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.pow

class WasteClassificationActivity : AppCompatActivity() {
    private lateinit var wasteList: ArrayList<String>
    private lateinit var bioRecView: RecyclerView
    private lateinit var nonBioRecView: RecyclerView
    private lateinit var recRecView: RecyclerView
    private lateinit var ERecView: RecyclerView

    private lateinit var locationFetchButton:ImageView
    private lateinit var addressField:TextView
    private lateinit var submitButton: AppCompatButton

    private var city: String = ""
    private var lat: Double = 0.0
    private var long: Double = 0.0

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbrefuser: DatabaseReference

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200) {
            if (resultCode == 200) {
                Toast.makeText(this,"Finished Caught", Toast.LENGTH_SHORT).show()
                lat = data?.getDoubleExtra("latitude",0.0)!!
                long = data?.getDoubleExtra("longitude",0.0)!!// Replace "key" with the same key you used in the second activity.
                city = data.getStringExtra("address")!!// Handle the received data here.

                addressField.text = city

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"CANCELLED", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_classification)
        wasteList = intent.getStringArrayListExtra("LabelList")!!

        addressField=findViewById(R.id.AddressField)
        bioRecView = findViewById(R.id.BioRecView)
        nonBioRecView = findViewById(R.id.NonBioRecView)
        recRecView = findViewById(R.id.RecRecView)
        ERecView = findViewById(R.id.ERecView)
        submitButton=findViewById(R.id.submitButton)

        locationFetchButton=findViewById(R.id.LocationfetchButton)

        locationFetchButton.setOnClickListener {
            val intent = Intent(this, LocationPickerActivity::class.java)
            startActivityForResult(intent,200)
        }
        val masterlist = arrayListOf(
            "person",
            "bicycle",
            "car",
            "motorcycle",
            "airplane",
            "bus",
            "train",
            "truck",
            "boat",
            "traffic light",
            "fire hydrant",
            "street sign",
            "stop sign",
            "parking meter",
            "bench",
            "bird",
            "cat",
            "dog",
            "horse",
            "sheep",
            "cow",
            "elephant",
            "bear",
            "zebra",
            "giraffe",
            "hat",
            "backpack",
            "umbrella",
            "shoe",
            "eye glasses",
            "handbag",
            "tie",
            "suitcase",
            "frisbee",
            "skis",
            "snowboard",
            "sports ball",
            "kite",
            "baseball bat",
            "baseball glove",
            "skateboard",
            "surfboard",
            "tennis racket",
            "bottle",
            "plate",
            "wine glass",
            "cup",
            "fork",
            "knife",
            "spoon",
            "bowl",
            "banana",
            "apple",
            "sandwich",
            "orange",
            "broccoli",
            "carrot",
            "hot dog",
            "pizza",
            "donut",
            "cake",
            "chair",
            "couch",
            "potted plant",
            "bed",
            "mirror",
            "dining table",
            "window",
            "desk",
            "toilet",
            "door",
            "tv",
            "laptop",
            "mouse",
            "remote",
            "keyboard",
            "cell phone",
            "microwave",
            "oven",
            "toaster",
            "sink",
            "refrigerator",
            "blender",
            "book",
            "clock",
            "vase",
            "scissors",
            "teddy bear",
            "hair drier",
            "toothbrush",
            "hair brush"
        )
        val bioMasterItems = arrayOf(
            "banana",
            "apple",
            "sandwich",
            "orange",
            "broccoli",
            "carrot",
            "hot dog",
            "pizza",
            "donut",
            "cake",
            "potted plant",
            "food scraps",
            "yard waste",
            "wood scraps"
        )
        val nonBiodegradableMasterItems = arrayOf(
            "styrofoam",
            "rubber",
            "ceramics",
            "textiles",
            "thoes",
            "frisbee",
            "sports ball",
            "baseball glove",
            "tennis racket",
            "bottle",
            "plate",
            "wine glass",
            "cup",
            "fork",
            "knife",
            "spoon",
            "bowl",
            "window",
            "toilet",
            "sink",
            "vase",
            "scissors",
            "teddy bear",
            "hair brush",
            "toothbrush"
        )

        val recyclableMasterItems = arrayOf(
            "paper",
            "cardboard",
            "glass",
            "bicycle",
            "motorcycle",
            "backpack",
            "eye glasses",
            "handbag",
            "tie",
            "suitcase",
            "skis",
            "snowboard",
            "kite",
            "baseball bat",
            "skateboard",
            "surfboard",
            "bed",
            "dining table",
            "desk",
            "mirror",
            "bench",
            "book",
            "door"
        )
        val eWasteMasterItems = arrayOf(
            "tv",
            "Laptop",
            "mouse",
            "remote",
            "keyboard",
            "cell phone",
            "microwave",
            "oven",
            "toaster",
            "refrigerator",
            "blender",
            "clock",
            "hair drier",
            "batteries",
            "electronics"
        )
        val noneMasterItems = arrayOf(
            "airplane",
            "bus",
            "train",
            "truck",
            "boat",
            "car",
            "bird",
            "cat",
            "dog",
            "horse",
            "sheep",
            "cow",
            "elephant",
            "bear",
            "zebra",
            "giraffe",
            "fire hydrant",
            "street sign",
            "stop sign",
            "parking meter"
        )
        val biowaste = arrayListOf<String>()
        val nonbiowaste = arrayListOf<String>()
        val recwaste = arrayListOf<String>()
        val ewaste = arrayListOf<String>()
        for (item in wasteList) {
            if (bioMasterItems.contains(item))
                biowaste.add(item)
            else
                if (nonBiodegradableMasterItems.contains(item))
                    nonbiowaste.add(item)
                else
                    if (recyclableMasterItems.contains(item))
                        recwaste.add(item)
                    else
                        if (eWasteMasterItems.contains(item))
                            ewaste.add(item)
        }

        Log.i("testing", biowaste.size.toString())
        Log.i("testing", nonbiowaste.size.toString())
        Log.i("testing", recwaste.size.toString())
        Log.i("testing", ewaste.size.toString())

        bioRecView.layoutManager = LinearLayoutManager(this)
        val bioAdapter=WasteItemAdapter(this,biowaste)
        bioRecView.adapter = bioAdapter

        nonBioRecView.layoutManager = LinearLayoutManager(this)
        val nonBioAdapter = WasteItemAdapter(this, nonbiowaste)
        nonBioRecView.adapter = nonBioAdapter

        recRecView.layoutManager = LinearLayoutManager(this)
        val recAdapter = WasteItemAdapter(this, recwaste)
        recRecView.adapter = recAdapter

        ERecView.layoutManager = LinearLayoutManager(this)
        val eAdapter = WasteItemAdapter(this, ewaste)
        ERecView.adapter = eAdapter

        submitButton.setOnClickListener {
            if(lat!=0.0&&long!=0.0)
                databaseOps(bioAdapter.sendList(),nonBioAdapter.sendList(),recAdapter.sendList(),eAdapter.sendList())
            else
                Toast.makeText(this,"LOCATION NOT SELECTED!",Toast.LENGTH_SHORT).show()
            //uploadCollectionRequest(bioAdapter.sendList(),nonBioAdapter.sendList(),recAdapter.sendList(),eAdapter.sendList())

        }
    }
    /*private fun uploadCollectionRequest(bioList: ArrayList<String>, nonBioList: ArrayList<String>, RecList: ArrayList<String>, eList: ArrayList<String>)
    {
        firebaseAuth=FirebaseAuth.getInstance()
        dbrefuser =  FirebaseDatabase.getInstance().getReference("CollectionRequests")

        val request = collectionRequest(uid = "", fcmtoken = "", biowastelist = bioList, nonbiowastelist = nonBioList, recwastelist = RecList, ewastelist = eList, city = city, lat = lat, long = long)
        val key=dbrefuser.push().key!!
        dbrefuser.child(key).setValue(request).addOnSuccessListener {
            Toast.makeText(this,"SUCCESSFULLY UPLOADED",Toast.LENGTH_SHORT).show()
        }
    }*/


    private fun databaseOps(bioList: ArrayList<String>, nonBioList: ArrayList<String>, RecList: ArrayList<String>, eList: ArrayList<String>) {
        firebaseAuth=FirebaseAuth.getInstance()
        dbrefuser =  FirebaseDatabase.getInstance().getReference("Organization")

        var bioAgency:Agency
        var nonBioAgency:Agency
        var recAgency:Agency
        var eAgency:Agency
        var minFCM=""
        var minUID=""
        var minLat=0.0
        var minLong=0.0
        var distance = ((lat - minLat).pow(2.0)) + ((long-minLong).pow(2))
        dbrefuser.child("biode").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children)
                {
                    val user:organisation = postSnapshot.getValue(organisation::class.java)!!
                    //val fcm = postSnapshot.child("fcmtoken").value as String

                    val latitude:Double = postSnapshot.child("lat").value as Double
                    val longitude:Double = postSnapshot.child("long").value as Double
                    //val uid = postSnapshot.child("uid").value as String
                    val uid = user.uid
                    val fcm = user.fcmtoken
                    //Log.i("agencycalculation",uid+","+fcm)
                    val d1 = ((latitude - lat).pow(2.0)) + ((longitude-long).pow(2.0))
                    if(d1<distance)
                    {
                        distance=d1
                        minLat=latitude
                        minLong=longitude
                        minFCM=fcm
                        minUID=uid
                    }
                }
                bioAgency= Agency(uid = minUID, fcmtoken = minFCM)
                Log.i("agencycalculation1", "$minFCM,$minUID,$minLat,$minLong")
                minFCM=""
                minUID=""
                minLat=0.0
                minLong=0.0
                distance = ((lat - minLat).pow(2.0)) + ((long-minLong).pow(2))
                dbrefuser.child("nonbiode").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (postSnapshot in snapshot.children) {
                            val user: organisation =
                                postSnapshot.getValue(organisation::class.java)!!
                            //val fcm = postSnapshot.child("fcmtoken").value as String

                            val latitude: Double = postSnapshot.child("lat").value as Double
                            val longitude: Double = postSnapshot.child("long").value as Double
                            //val uid = postSnapshot.child("uid").value as String
                            val uid = user.uid
                            val fcm = user.fcmtoken
                            //Log.i("agencycalculation",uid+","+fcm)
                            val d1 = ((latitude - lat).pow(2.0)) + ((longitude - long).pow(2.0))
                            if (d1 < distance) {
                                distance = d1
                                minLat = latitude
                                minLong = longitude
                                minFCM = fcm
                                minUID = uid
                            }
                        }
                        nonBioAgency = Agency(uid = minUID, fcmtoken = minFCM)
                        Log.i("agencycalculation2", "$minFCM,$minUID,$minLat,$minLong")
                        //Toast.makeText(baseContext,"Was Successful",Toast.LENGTH_SHORT).show()
                        minFCM=""
                        minUID=""
                        minLat=0.0
                        minLong=0.0
                        distance = ((lat - minLat).pow(2.0)) + ((long-minLong).pow(2))
                        dbrefuser.child("ewaste").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (postSnapshot in snapshot.children) {
                                    val user: organisation =
                                        postSnapshot.getValue(organisation::class.java)!!
                                    //val fcm = postSnapshot.child("fcmtoken").value as String

                                    val latitude: Double = postSnapshot.child("lat").value as Double
                                    val longitude: Double = postSnapshot.child("long").value as Double
                                    //val uid = postSnapshot.child("uid").value as String
                                    val uid = user.uid
                                    val fcm = user.fcmtoken
                                    //Log.i("agencycalculation",uid+","+fcm)
                                    val d1 = ((latitude - lat).pow(2.0)) + ((longitude - long).pow(2.0))
                                    if (d1 < distance) {
                                        distance = d1
                                        minLat = latitude
                                        minLong = longitude
                                        minFCM = fcm
                                        minUID = uid
                                    }
                                }
                                eAgency = Agency(uid = minUID, fcmtoken = minFCM)
                                Log.i("agencycalculation3", "$minFCM,$minUID,$minLat,$minLong")
                                //Toast.makeText(baseContext,"Was Successful",Toast.LENGTH_SHORT).show()
                                minFCM=""
                                minUID=""
                                minLat=0.0
                                minLong=0.0
                                distance = ((lat - minLat).pow(2.0)) + ((long-minLong).pow(2))
                                dbrefuser.child("recyclabe").addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (postSnapshot in snapshot.children) {
                                            val user: organisation =
                                                postSnapshot.getValue(organisation::class.java)!!
                                            //val fcm = postSnapshot.child("fcmtoken").value as String

                                            val latitude: Double = postSnapshot.child("lat").value as Double
                                            val longitude: Double = postSnapshot.child("long").value as Double
                                            //val uid = postSnapshot.child("uid").value as String
                                            val uid = user.uid
                                            val fcm = user.fcmtoken
                                            //Log.i("agencycalculation",uid+","+fcm)
                                            val d1 = ((latitude - lat).pow(2.0)) + ((longitude - long).pow(2.0))
                                            if (d1 < distance) {
                                                distance = d1
                                                minLat = latitude
                                                minLong = longitude
                                                minFCM = fcm
                                                minUID = uid
                                            }
                                        }
                                        recAgency = Agency(uid = minUID, fcmtoken = minFCM)
                                        Log.i("agencycalculation4", "$minFCM,$minUID,$minLat,$minLong")
                                        //Toast.makeText(baseContext,"Was Successful",Toast.LENGTH_SHORT).show()

                                        //val name = firebaseAuth.currentUser?.displayName!!
                                        val uid = firebaseAuth.currentUser?.uid!!
                                        firebaseAuth=FirebaseAuth.getInstance()
                                        dbrefuser =  FirebaseDatabase.getInstance().getReference("CollectionRequests")

                                        val request = collectionRequest(uid = uid, name = "", biowastelist = bioList, nonbiowastelist = nonBioList, recwastelist = RecList, ewastelist = eList, city = city, lat = lat, long = long, bioAgency = bioAgency, nonBioAgency = nonBioAgency, recAgency = recAgency, eAgency = eAgency)
                                        val key=dbrefuser.push().key!!
                                        dbrefuser.child(key).setValue(request).addOnSuccessListener {
                                            Toast.makeText(
                                                baseContext,
                                                "SUCCESSFULLY UPLOADED",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dbrefuser=FirebaseDatabase.getInstance().getReference("Users")
                                            dbrefuser.child(firebaseAuth.uid.toString()).get().addOnSuccessListener {snap ->
                                                val user:users = snap.getValue(users::class.java)!!
                                                snap.ref.removeValue()
                                                user.count=user.count+1
                                                user.wasteCounts.bioCount+=bioList.size
                                                user.wasteCounts.nonBioCount+=nonBioList.size
                                                user.wasteCounts.recCount+=RecList.size
                                                user.wasteCounts.eCount+=eList.size
                                                dbrefuser.child(firebaseAuth.uid.toString()).setValue(user)

                                            }
                                            val intent = Intent(this@WasteClassificationActivity,UploadFinishedActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(
                                            baseContext,
                                            "Database Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    baseContext,
                                    "Database Error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            baseContext,
                            "Database Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    baseContext,
                    "Database Error",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }
}