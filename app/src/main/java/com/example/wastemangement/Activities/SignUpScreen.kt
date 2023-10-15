package com.example.wastemangement.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.cloudinary.Cloudinary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class   SignUpScreen : AppCompatActivity() {
    private lateinit var mdatabaseref : DatabaseReference
    private val cloudinary = Cloudinary("cloudinary://621452581592841:jgeMsIs1fffBPUm53Qiqk3LBsno@dqev44wx0")
    private lateinit var mauth:FirebaseAuth
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var button:Button
    private lateinit var address:EditText
    private lateinit var intentauth:RadioButton
    private lateinit var dbrefNotify:DatabaseReference
    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var imageUrl: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen)
        name=findViewById(R.id.editTextname)
        email=findViewById(R.id.editTextTextEmailAddress)
        password=findViewById(R.id.editTextTextPassword)
        button=findViewById(R.id.register)
        mauth= FirebaseAuth.getInstance()
        address=findViewById(R.id.editTextAddress)
        mdatabaseref= FirebaseDatabase.getInstance().getReference("Users")
        dbrefNotify=FirebaseDatabase.getInstance().getReference("ToNotify")

        imageView =findViewById(R.id.uploadPic)

        imageView.setOnClickListener{
            openImageChooser()
            imageView.setColorFilter(ContextCompat.getColor(this, com.cloudinary.android.preprocess.R.color.mtrl_btn_transparent_bg_color),android.graphics.PorterDuff.Mode.ADD);

        }


        button.setOnClickListener{
            registeruser()
        }
        intentauth=findViewById(R.id.Authority)
        intentauth.setOnClickListener{
            startActivity(Intent(this,RecyclingCentreActivity::class.java))
        }


    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            var filePath: String = ""
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = imageUri?.let { this.contentResolver.query(it, projection, null, null, null) }
            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                it.moveToFirst()

                filePath = it.getString(columnIndex)

            }
            Glide
                .with(imageView)
                .load(imageUri)
                .placeholder(R.drawable.profilevector)
                .centerCrop()
                .circleCrop()
                .into(imageView)
            GlobalScope.launch(Dispatchers.IO) {
                try {


                    // Get the selected image as a Bitmap
                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    //    val imageBitmap = BitmapFactory.decodeStream(inputStream)

                    // Display the selected image in the ImageView



//                 Upload the image to Cloudinary
                    val result = cloudinary.uploader().upload(filePath , null)
                    imageUrl = result["url"].toString()
                    launch(Dispatchers.Main) {

                        Log.i("url", imageUrl!!)

                    }




                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun registeruser()
    {

        val name1=name.text.toString().trim{it<=' '}
        val email1=email.text.toString().trim{it<=' '}
        val addr = address.text.toString().trim()
        val password=password.text.toString().trim(){it<=' '}
        if(validateForm(name1,email1,password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email1, password
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseuser: FirebaseUser = task.result!!.user!!
                    val firebaseemail = firebaseuser.email

                    val sharedPreferences=getSharedPreferences("TokenPreferences", MODE_PRIVATE)
                    val savedToken=sharedPreferences.getString("SavedToken","Nothing")
                    if(savedToken.equals("Nothing")){
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w("Task Unsuccessful", "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }
                            // Get new FCM registration token
                            val token = task.result.toString()
                            var user = users()
                            if(imageUrl!=null){
                                user = users(name = name1, email = email1, fcmtoken=token, uid = mauth.uid.toString(), address = addr, image = imageUrl!!, count = 0)
                            }else{
                                user = users(name = name1, email = email1, fcmtoken=token, uid = mauth.uid.toString(), address = addr, count = 0)
                            }
                            mdatabaseref.child("${firebaseuser.uid}").setValue(user).addOnCompleteListener {

                                finishAffinity()
                                val intent: Intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)


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



                    val check= notifyDataClass(email=email1)
                    dbrefNotify.child("${firebaseuser.uid}").setValue(check)





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
    private fun validateForm(name:String,email:String,password:String):Boolean
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