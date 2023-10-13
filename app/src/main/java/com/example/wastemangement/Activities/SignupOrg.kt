package com.example.wastemangement.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.wastemangement.DataClass.users
import com.example.wastemangement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupOrg : AppCompatActivity() {
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var signin : Button
    private lateinit var signup : Button
    private lateinit var email1 : EditText
    private lateinit var password1 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_org)

        signin = findViewById(R.id.signin1)
        signup = findViewById(R.id.signup)
        email1 = findViewById(R.id.editTextTextEmailAddress)
        password1= findViewById(R.id.editTextTextPassword)

        database =FirebaseDatabase.getInstance().getReference("User ID")

        signin.setOnClickListener {
            val intent = Intent(this, signin::class.java)
            startActivity(intent)
        }
        signup.setOnClickListener {
            val email = email1.text.toString()
            val pass = password1.text.toString()

            if (firebaseAuth.currentUser != null){
                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    val user1= users(email)
                    database.child(email.removeSuffix("@gmail.com"))
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, signin::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                else{
                    Toast.makeText(this, "Empty Field are not Allowed", Toast.LENGTH_SHORT).show()
                }
                run{
                    Toast.makeText(this, "User already registered", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}