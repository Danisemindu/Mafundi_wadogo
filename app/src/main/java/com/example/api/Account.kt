package com.example.api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Account : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var accountNames: TextView
    private lateinit var accountEmail: TextView
    private lateinit var accountPassword: TextView
    private lateinit var newPasswordEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var deleteAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        accountNames = findViewById(R.id.accountNames)
        accountEmail = findViewById(R.id.accountEmail)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)
        deleteAccountButton = findViewById(R.id.deleteAccountButton)

        if (currentUser != null) {
            loadUserInfo(currentUser)
        }

        resetPasswordButton.setOnClickListener {
            resetPassword(currentUser)
        }

        deleteAccountButton.setOnClickListener {
            deleteAccount(currentUser)
        }
    }

    private fun loadUserInfo(user: FirebaseUser) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        database.get().addOnSuccessListener { snapshot ->
            val firstName = snapshot.child("firstName").value.toString()
            val secondName = snapshot.child("secondName").value.toString()
            val email = user.email.toString()
            val password = snapshot.child("password").value.toString()

            accountNames.text = "$firstName $secondName"
            accountEmail.text = email
        }.addOnFailureListener { e ->
            Log.e("Account", "Failed to load user data", e)
        }
    }

    private fun resetPassword(user: FirebaseUser?) {
        user?.let {
            val newPassword = newPasswordEditText.text.toString()

            if (newPassword.isNotEmpty()) {
                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            updatePasswordInDatabase(user.uid, newPassword)
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePasswordInDatabase(userId: String, newPassword: String) {
        val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        database.child("password").setValue(newPassword)
            .addOnSuccessListener {
                Log.d("Account", "Password updated in database successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Account", "Failed to update password in database", e)
            }
    }

    private fun deleteAccount(user: FirebaseUser?) {
        user?.let {
            val userId = it.uid
            val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            database.removeValue()
                .addOnSuccessListener {
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("Account", "Failed to delete user data", e)
                }
        }
    }
}
