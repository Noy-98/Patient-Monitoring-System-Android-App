package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Add_Record : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var currentDoctorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser
        currentDoctorId = currentUser?.uid ?: ""

        val back_button = findViewById<ImageView>(R.id.back_bttn)
        val patientNumEditText = findViewById<TextInputEditText>(R.id.patient_num)
        val bpmValEditText = findViewById<TextInputEditText>(R.id.bpm_val)
        val normalRadioButton = findViewById<RadioButton>(R.id.normal)
        val notNormalRadioButton = findViewById<RadioButton>(R.id.not_normal)
        val monRadioButton = findViewById<RadioButton>(R.id.mon)
        val tuesRadioButton = findViewById<RadioButton>(R.id.tues)
        val wedRadioButton = findViewById<RadioButton>(R.id.wed)
        val thursRadioButton = findViewById<RadioButton>(R.id.thurs)
        val friRadioButton = findViewById<RadioButton>(R.id.frid)
        val satRadioButton = findViewById<RadioButton>(R.id.sat)
        val sunRadioButton = findViewById<RadioButton>(R.id.sun)
        val datePicker = findViewById<DatePicker>(R.id.date_picker)
        val timePicker = findViewById<TimePicker>(R.id.time_picker)
        val saveButton = findViewById<AppCompatButton>(R.id.save_bttn)

        saveButton.setOnClickListener {
            val patientNum = patientNumEditText.text.toString()
            val bpmVal = bpmValEditText.text.toString()
            val status = when {normalRadioButton.isChecked -> "Normal"
                                notNormalRadioButton.isChecked -> "Not Normal"
                                else -> "Not Set"}
            val nameday = when {
                monRadioButton.isChecked -> "Monday"
                tuesRadioButton.isChecked -> "Tuesday"
                wedRadioButton.isChecked -> "Wednesday"
                thursRadioButton.isChecked -> "Thursday"
                friRadioButton.isChecked -> "Friday"
                satRadioButton.isChecked -> "Saturday"
                sunRadioButton.isChecked -> "Sunday"
                else -> "Not Set"
            }

            val date = "${datePicker.month + 1}-${datePicker.dayOfMonth}-${datePicker.year}"
            val time = "${timePicker.hour}:${timePicker.minute}"

            // Create Record_Data_DB object
            val recordData = Record_Data_DB(patientNum, bpmVal, status, nameday, date, time)

            // Save data to Firebase Realtime Database
            saveRecordToFirebase(recordData)
        }


        back_button.setOnClickListener {
            val intent = Intent (this, Patient_Details::class.java)
            startActivity(intent)
        }
    }

    private fun saveRecordToFirebase(recordData: Record_Data_DB) {
        val recordsRef = database.reference.child("UserData").child("Doctor").child(currentDoctorId).child("Records").child("BPM")
        val recordId = recordsRef.push().key // Generate a unique key for the record

        if (recordId != null) {
            recordsRef.child(recordId).setValue(recordData)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val intent = Intent (this, BPM_Records::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Record saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save records data", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}