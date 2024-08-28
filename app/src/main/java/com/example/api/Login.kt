package com.example.api

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUp: TextView


    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.emaillogin)
        passwordEditText = findViewById(R.id.passwordlogin)
        loginButton = findViewById(R.id.loginbtn)
        signUp = findViewById(R.id.noacc)

        loginButton.setOnClickListener {
            loginUser()
        }

        signUp.setOnClickListener {
            val intent40 = Intent(this, Register::class.java)
            startActivity(intent40)
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Login succeful", Toast.LENGTH_SHORT).show()
                        // Navigate to main activity or another screen
                        val intent41 = Intent(this, MainActivity2::class.java)
                        startActivity(intent41)
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Login failed ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}






