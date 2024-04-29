package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Random

class Forgot_Password : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressBar
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database

        val email_EditText = findViewById<TextInputEditText>(R.id.email)
        val sendButton = findViewById<AppCompatButton>(R.id.send)
        val progressBar: ProgressBar = findViewById(R.id.forgetPassProgressBar)

        sendButton.setOnClickListener {
            val email = email_EditText.text.toString()

            if (email.isEmpty()){
                email_EditText.error = "Enter email address"
                Toast.makeText(this,"Enter email address", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())){
                email_EditText.error="Enter valid email address"
                Toast.makeText(this,"Enter valid email address", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        val intent = Intent (this, Login::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}