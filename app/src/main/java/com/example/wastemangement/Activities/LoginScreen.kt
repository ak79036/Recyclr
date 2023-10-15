package com.example.wastemangement.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.wastemangement.DataClass.notifyDataClass
import com.example.wastemangement.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginScreen : AppCompatActivity() {
    private lateinit var mauth:FirebaseAuth
    private lateinit var databaserefuser:DatabaseReference
    private lateinit var databasereforg:DatabaseReference
    private lateinit var dbrefNotify:DatabaseReference
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var signupintent:TextView
    private lateinit var btn:Button
    private lateinit var checker:CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        email= findViewById(R.id.EmailEntryField)
        signupintent=findViewById(R.id.txtNewHereSign)
        btn=findViewById(R.id.btnSignin)
        checker=findViewById(R.id.numberCheckBox)
        password=findViewById(R.id.password)
        signupintent.setOnClickListener {
            startActivity(Intent(this,SignUpScreen::class.java))
        }
        mauth=FirebaseAuth.getInstance()
        databaserefuser= FirebaseDatabase.getInstance().getReference("Users")
         databasereforg=FirebaseDatabase.getInstance().getReference("Organization")
         dbrefNotify=FirebaseDatabase.getInstance().getReference("ToNotify")

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val isUser = sharedPreferences.getBoolean("type", true)

        if(mauth.currentUser!=null){
            var intent: Intent? = null
            if(isUser){
                intent = Intent(this, MainActivity::class.java)
            }else {
                intent = Intent(this, OrganizationMainActivity::class.java)
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btn.setOnClickListener {
            if(checker.isChecked){
                signinuser()
            }else{
                signinuser1()
            }
        }
    }

    private fun signinuser1() {
        val email1=email.text.toString().trim{it<=' '}
        val password=password.text.toString().trim{ it<=' ' }
        if (validateForm(email1,password)) {
            mauth.signInWithEmailAndPassword(email1,password).addOnCompleteListener(this)
            {
                    task->
//                val check=notifyDataClass(email=email1)
//                dbrefNotify.child(mauth.uid.toString()).setValue(check)

                if(task.isSuccessful)
                {
                    finishAffinity()
                    val intent = Intent(this, OrganizationMainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(
                        this,
                        "please check your connection and enter the correct credential",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun signinuser() {
        val email1=email.text.toString().trim{it<=' '}
        val password1=password.text.toString().trim{it<=' '}
        if (validateForm(email1,password1)) {
            mauth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(this)
            {
                task->
             if(task.isSuccessful)
             {
                 val check=notifyDataClass(isOrganization = true,email=email1)
                 if(checker.isChecked)
                 {
                     dbrefNotify.child(mauth.uid.toString()).setValue(check)
                 }
                 finishAffinity()
                 val intent = Intent(this, OrganizationMainActivity::class.java)
                 startActivity(intent)
             }
           else
             {
                 Toast.makeText(
                     this,
                     "please check your connection and enter the correct credential",
                     Toast.LENGTH_SHORT
                 ).show()
             }
            }
        }
    }
    private fun validateForm(email:String,password:String):Boolean
    {
        return when{
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