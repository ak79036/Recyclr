package com.example.wastemangement.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpScreen : AppCompatActivity() {
    private lateinit var mdatabaseref : DatabaseReference
    private lateinit var mauth:FirebaseAuth
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var button:Button
    private lateinit var intentauth:RadioButton
    private lateinit var dbrefNotify:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen)
   name=findViewById(R.id.editTextname)
        email=findViewById(R.id.editTextTextEmailAddress)
        password=findViewById(R.id.editTextTextPassword)
        button=findViewById(R.id.register)
        mauth= FirebaseAuth.getInstance()

        mdatabaseref= FirebaseDatabase.getInstance().getReference("Users")
        dbrefNotify=FirebaseDatabase.getInstance().getReference("ToNotify")


        button.setOnClickListener{
            registeruser()
        }
       intentauth=findViewById(R.id.Authority)
       intentauth.setOnClickListener{
           startActivity(Intent(this,RecyclingCentreActivity::class.java))
       }


    }
    private fun registeruser()
    {

      val name1=name.text.toString().trim{it<=' '}
        val email1=email.text.toString().trim{it<=' '}
        val password=password.text.toString().trim(){it<=' '}
        if(validateForm(name1,email1,password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email1, password
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseuser: FirebaseUser = task.result!!.user!!
                    val firebaseemail = firebaseuser.email


                    var user = users(name = name1, email = email1)

                    val check= notifyDataClass(email=email1)
                    dbrefNotify.child("${firebaseuser.uid}").setValue(check)


                    mdatabaseref.child("${firebaseuser.uid}").setValue(user).addOnCompleteListener {

                        finishAffinity()
                        val intent: Intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
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