package com.example.wastemangement.Activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.DataClass.organisation
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class RecyclingCentreActivity : AppCompatActivity() {
    private lateinit var mdatabaseref: DatabaseReference
    private lateinit var nameorg:EditText
    private lateinit var phoneog:EditText
    private lateinit var emailorg:EditText
    private lateinit var addressorg:EditText
    private lateinit var employesno:String
    private lateinit var truckno :String
    private lateinit var dbrefNotify:DatabaseReference
    private lateinit var register:Button
    private lateinit var password: EditText
    private var city: String = ""
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private lateinit var radio1:CheckBox
    private lateinit var radio2:CheckBox
    private lateinit var radio3: CheckBox
    private lateinit var radio4:CheckBox
   private lateinit var mauth:FirebaseAuth



    val data1= arrayOf("Less than 10","10 to 50","50 to 100","More than 100")
    val data2= arrayOf("Less than 2","2 to 5","5 to 10","10 to 20 ","More than 20")

    lateinit var autocompleteTV1: AutoCompleteTextView
    lateinit var autocompleteTV2: AutoCompleteTextView

    lateinit var adapter1: ArrayAdapter<String>
    lateinit var adapter2: ArrayAdapter<String>

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200) {
            if (resultCode == 200) {
                Toast.makeText(this,"Finished Caught",Toast.LENGTH_SHORT).show()
                lat = data?.getDoubleExtra("latitude",0.0)!!
                long = data?.getDoubleExtra("longitude",0.0)!!// Replace "key" with the same key you used in the second activity.
                city = data.getStringExtra("address")!!// Handle the received data here.

                addressorg.setText(city)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"CANCELLED",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycling_centre)

    mauth= FirebaseAuth.getInstance()
        radio1=findViewById(R.id.But1)
        radio2=findViewById(R.id.But2)
        radio3=findViewById(R.id.But3)
        radio4=findViewById(R.id.But4)

        autocompleteTV1=findViewById(R.id.drop2)
        autocompleteTV2=findViewById(R.id.drop3)

        adapter1 = ArrayAdapter<String>(this, R.layout.list_item,data1)
        adapter2 = ArrayAdapter<String>(this, R.layout.list_item,data2)


        autocompleteTV1.setAdapter(adapter1)
        autocompleteTV2.setAdapter(adapter2)


     mdatabaseref=FirebaseDatabase.getInstance().getReference("Organization")
        dbrefNotify=FirebaseDatabase.getInstance().getReference("ToNotify")
        nameorg=findViewById(R.id.centrename)
        phoneog=findViewById(R.id.phoneET)
        emailorg=findViewById(R.id.emailET)
        addressorg=findViewById(R.id.addressET)
        addressorg.setOnClickListener {
            val intent = Intent(this, LocationPickerActivity::class.java)
            startActivityForResult(intent,200)
        }

       register=findViewById(R.id.btnRegister)
        password=findViewById(R.id.password)
        register.setOnClickListener {
            registerorg()
        }

    }
    private fun registerorg()
    {
        val name=nameorg.text.toString().trim{it<=' '}
        val phone=phoneog.text.toString().trim{it<=' '}
        val email=emailorg.text.toString().trim{it<=' '}
        val address=addressorg.text.toString().trim{ it<=' ' }
        employesno=autocompleteTV1.text.toString()
        truckno=autocompleteTV2.text.toString()
        val password=password.text.toString().trim{it<=' '}
        if(validateForm(name,phone,email,address,employesno,truckno,password))
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email, password
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseuser: FirebaseUser = task.result!!.user!!
                    val firebaseemail = firebaseuser.email
                    val check=notifyDataClass(isOrganization = true,email=email)
                    dbrefNotify.child("${firebaseuser.uid}").setValue(check)
                    val sharedPreferences=getSharedPreferences("TokenPreferences", MODE_PRIVATE)
                    val savedToken=sharedPreferences.getString("SavedToken","Nothing")
                    if(savedToken.equals("Nothing")){
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w("Task Unsuccessful", "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }
                            // Get new FCM registration token
                            val token = task.result.toString()

                                var org = organisation(name=name,phone=phone,email=email,address=address, workforceNo = employesno,vehicle=truckno,lat=lat, long = long, fcmtoken = token, uid =mauth.uid.toString() )
                                if(radio1.isChecked)
                                {
                                    mdatabaseref.child("biode").child("${firebaseuser.uid}").setValue(org).addOnCompleteListener {

                                        finishAffinity()
                                        val intent: Intent = Intent(this, OrganizationMainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                                if(radio2.isChecked)
                                {
                                    mdatabaseref.child("nonbiode").child("${firebaseuser.uid}").setValue(org).addOnCompleteListener {

                                        finishAffinity()
                                        val intent: Intent = Intent(this, OrganizationMainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                                if(radio3.isChecked)
                                {
                                    mdatabaseref.child("recyclabe").child("${firebaseuser.uid}").setValue(org).addOnCompleteListener {

                                        finishAffinity()
                                        val intent: Intent = Intent(this, OrganizationMainActivity::class.java)
                                        startActivity(intent)
                                    }

                                }
                                if(radio4.isChecked)
                                {
                                    mdatabaseref.child("ewaste").child("${firebaseuser.uid}").setValue(org).addOnCompleteListener {

                                        finishAffinity()
                                        val intent: Intent = Intent(this, OrganizationMainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            Log.d("Your Device Token=>>>", token)
                            val editPref: SharedPreferences.Editor=sharedPreferences.edit()
                            editPref.putString("SavedToken",token)
                            editPref.commit()

//                            ApiService().addTokenService(token,this)
//                val list=ArrayList<String>()
//                list.add(token)
                        })
                    }








                } else {
                    Toast.makeText(
                        this,
                        "please check your connection and enter the correct credential",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }



        }

    }
    private fun validateForm(name:String,phone:String,email:String,address:String,employeesno:String,truckno:String,password:String):Boolean
    {
        return when{
            TextUtils.isEmpty(name)->
            {
                errorSnackBar("Please Enter your Name")
                false
            }
            TextUtils.isEmpty(email)->
            {
                errorSnackBar("Please Enter your Email")
                false
            }
            TextUtils.isEmpty(password)->
            {
                errorSnackBar("Please Enter your Password")
                false
            }
            TextUtils.isEmpty(phone)->
            {
                errorSnackBar("Please Enter your Phone Number")
                false
            }
            TextUtils.isEmpty(address)->
            {
                errorSnackBar("Please Select your address")
                false
            }
            TextUtils.isEmpty(employeesno)->
            {
                errorSnackBar("Please Enter your Number of your employees")
                false
            }
            TextUtils.isEmpty(truckno)->
            {
                errorSnackBar("Please Enter your Number of your trucks you have")
                false
            }





            else->
            {
                true
            }
        }

    }
    fun errorSnackBar(message:String)
    {
        val snackbar= Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
        val snackBarView=snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, com.jpardogo.android.googleprogressbar.library.R.color.red))
        snackbar.show()

    }



}