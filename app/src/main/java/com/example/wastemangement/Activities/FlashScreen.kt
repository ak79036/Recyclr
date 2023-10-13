package com.example.wastemangement.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.wastemangement.R

class FlashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_screen)
      Handler().postDelayed({
          startActivity(Intent(this, MainActivity::class.java))

      },2500)
    }
}