package com.example.wastemangement.Activities

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
    private lateinit var passworduser:EditText
    private lateinit var passwordorg:EditText
    private lateinit var signupintent:TextView
    private lateinit var btn:Button
    private lateinit var checker:CheckBox
    private lateinit var layoutuserpass:View
    private lateinit var layoutorgpass:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        email= findViewById(R.id.EmailEntryField)
        passworduser = findViewById (R.id.passworduser)
        passwordorg=findViewById(R.id.organisationpassword)
        signupintent=findViewById(R.id.txtNewHereSign)
        btn=findViewById(R.id.btnSignin)
        layoutuserpass=findViewById(R.id.emailInputLayout1)
        layoutorgpass=findViewById(R.id.emailInputLayout2)
        checker=findViewById(R.id.numberCheckBox)
        signupintent.setOnClickListener {
            startActivity(Intent(this,SignUpScreen::class.java))
        }
        mauth=FirebaseAuth.getInstance()
        databaserefuser= FirebaseDatabase.getInstance().getReference("Users")
         databasereforg=FirebaseDatabase.getInstance().getReference("Organization")
         dbrefNotify=FirebaseDatabase.getInstance().getReference("ToNotify")

        checker.setOnClickListener {
            if(checker.isChecked)
            {
                layoutuserpass.visibility=ViewGroup.GONE
                passworduser.visibility= View.GONE
                passwordorg.visibility=View.VISIBLE
                layoutorgpass.visibility=ViewGroup.VISIBLE

            }
            else
            {
                passworduser.visibility= View.VISIBLE
                passwordorg.visibility=View.GONE
                layoutorgpass.visibility=ViewGroup.GONE
                layoutuserpass.visibility=ViewGroup.VISIBLE
            }
        }
            if(checker.isChecked)
            {





                btn.setOnClickListener {
                    signinuser()
                }

            }
            else if(!checker.isChecked)
            {

                btn.setOnClickListener {
                    signinuser1()
                }
            }










    }

    private fun signinuser1() {
        val email1=email.text.toString().trim{it<=' '}
        val password=passworduser.text.toString().trim{ it<=' ' }
        if (validateForm(email1,password)) {
            mauth.signInWithEmailAndPassword(email1,password).addOnCompleteListener(this)
            {
                    task->

                val check=notifyDataClass(email=email1)
                dbrefNotify.child(mauth.uid.toString()).setValue(check)

                if(task.isSuccessful)
                {
                    finishAffinity()
                    val intent = Intent(this, MainActivity::class.java)
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
        val password=passwordorg.text.toString().trim{ it<=' ' }
        if (validateForm(email1,password)) {


            mauth.signInWithEmailAndPassword(email1,password).addOnCompleteListener(this)
            {



                task->

                val check=notifyDataClass(isOrganization = true,email=email1)
                if(checker.isChecked)
                {
                    dbrefNotify.child(mauth.uid.toString()).setValue(check)
                }
             if(task.isSuccessful)
             {
                 finishAffinity()
                 val intent = Intent(this, MainActivity::class.java)
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