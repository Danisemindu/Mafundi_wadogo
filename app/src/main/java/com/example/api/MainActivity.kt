@file:Suppress("DEPRECATION")

package com.example.api

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            setContentView(R.layout.activity_main)
            Handler().postDelayed({
                // Start the Main Activity
                val mainIntent = Intent(this@MainActivity, Login::class.java)
                startActivity(mainIntent)
                finish()
            }, 2000)
    }
}