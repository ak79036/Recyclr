package com.example.wastemangement.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemangement.Adapters.WasteItemAdapter
import com.example.wastemangement.R

class WasteClassificationActivity : AppCompatActivity() {
    private lateinit var wasteList: ArrayList<String>
    private lateinit var bioRecView: RecyclerView
    private lateinit var nonBioRecView: RecyclerView
    private lateinit var recRecView: RecyclerView
    private lateinit var ERecView: RecyclerView

    private lateinit var locationFetchButton:ImageView
    private lateinit var addressField:TextView

    private var city: String = ""
    private var lat: Double = 0.0
    private var long: Double = 0.0

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
        bioRecView.adapter = WasteItemAdapter(this, biowaste)

        nonBioRecView.layoutManager = LinearLayoutManager(this)
        nonBioRecView.adapter = WasteItemAdapter(this, nonbiowaste)

        recRecView.layoutManager = LinearLayoutManager(this)
        recRecView.adapter = WasteItemAdapter(this, recwaste)

        ERecView.layoutManager = LinearLayoutManager(this)
        ERecView.adapter = WasteItemAdapter(this, ewaste)


    }
}