package com.example.wastemangement.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.example.wastemangement.R

class FlashScreen : AppCompatActivity() {
    private lateinit var titleTextView:TextView
    var i=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_flash_screen)
        titleTextView=findViewById(R.id.titleText)
        animateText("Recyclr")

        val intent = Intent(this, LoginScreen::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
      Handler().postDelayed({
          startActivity(intent)
      },2000)
    }
    private fun animateText(text: String) {

        if (i <= text.length) {
            val fetchtext: String = text.substring(0, i);
            titleTextView.text = fetchtext
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    i++;
                    animateText(text)
                }, 100
            )
        }
    }
}