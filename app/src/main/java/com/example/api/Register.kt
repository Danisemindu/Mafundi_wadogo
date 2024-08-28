package com.example.api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firstNameEditText: EditText
    private lateinit var secondNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var numberEditText: EditText // Add this line
    private lateinit var registrationTypeRadioGroup: RadioGroup
    private lateinit var handymanTypeSpinner: Spinner
    private lateinit var specificSkillsEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        firstNameEditText = findViewById(R.id.fnameReg)
        secondNameEditText = findViewById(R.id.snameReg)
        emailEditText = findViewById(R.id.emailReg)
        passwordEditText = findViewById(R.id.passwordReg)
        confirmPasswordEditText = findViewById(R.id.confirmpasswordReg)
        locationEditText = findViewById(R.id.locationReg)
        genderEditText = findViewById(R.id.genderReg)
        numberEditText = findViewById(R.id.numberReg) // Add this line
        registrationTypeRadioGroup = findViewById(R.id.regRadioGroup)
        handymanTypeSpinner = findViewById(R.id.handyman_spinner)
        specificSkillsEditText = findViewById(R.id.specificSkillsEditText)
        signUpButton = findViewById(R.id.signupReg)

        val handymanTypes = arrayOf("Carpenter", "Shoemaker", "Painter", "Locksmith", "Tailor", "Plumber")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, handymanTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        handymanTypeSpinner.adapter = adapter

        registrationTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.handymanRadioBtn) {
                handymanTypeSpinner.visibility = View.VISIBLE
                specificSkillsEditText.visibility = View.VISIBLE
            } else {
                handymanTypeSpinner.visibility = View.GONE
                specificSkillsEditText.visibility = View.GONE
            }
        }

        signUpButton.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val firstName = firstNameEditText.text.toString()
        val secondName = secondNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        val location = locationEditText.text.toString()
        val gender = genderEditText.text.toString()
        val number = numberEditText.text.toString() // Add this line
        val registrationType = if (registrationTypeRadioGroup.checkedRadioButtonId == R.id.handymanRadioBtn) "Handyman" else "Client"
        val handymanType = if (registrationType == "Handyman") handymanTypeSpinner.selectedItem.toString() else ""
        val specificSkills = if (registrationType == "Handyman") specificSkillsEditText.text.toString() else ""

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && firstName.isNotEmpty() && secondName.isNotEmpty() && location.isNotEmpty() && gender.isNotEmpty() && number.isNotEmpty() && (registrationType != "Handyman" || specificSkills.isNotEmpty())) { // Update this line
            if (password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                            // Save additional user information to Firebase Database
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                val userMap = hashMapOf(
                                    "firstName" to firstName,
                                    "secondName" to secondName,
                                    "location" to location,
                                    "gender" to gender,
                                    "number" to number,
                                    "registrationType" to registrationType,
                                    "handymanType" to handymanType,
                                    "specificSkills" to specificSkills
                                )
                                val database = FirebaseDatabase.getInstance().reference
                                database.child("users").child(userId).setValue(userMap)
                                    .addOnSuccessListener {
                                        Log.d("Register", "User data saved successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Register", "Failed to save user data", e)
                                    }
                            } else {
                                Log.e("Register", "User ID is null")
                            }
                            // Navigate to login activity
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Sign up failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("Register", "Sign up failed", task.exception)
                        }
                    }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
