package com.example.wastemangement.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wastemangement.R
import com.google.firebase.database.DatabaseReference

class SignUpScreen : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen)


    }
}