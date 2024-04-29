package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Patient_Details : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_details)

        // Retrieve patient data from intent extras
        val patientData = intent.getParcelableExtra<Patient_Data_DB>("patientData")

        // Find views in the layout
        val patientPicture: ShapeableImageView = findViewById(R.id.patient_picture)
        val patientNum: TextView = findViewById(R.id.patient_num)
        val patientFirstName: TextView = findViewById(R.id.patient_first_name)
        val patientLastName: TextView = findViewById(R.id.patient_last_name)
        val patientGender: TextView = findViewById(R.id.patient_gender)
        val patientAge: TextView = findViewById(R.id.patient_age)
        val patientHeight: TextView = findViewById(R.id.patient_height)
        val patientWeight: TextView = findViewById(R.id.patient_weight)

        // Display patient details
        Glide.with(this)
            .load(patientData?.imageUrl)
            .placeholder(R.drawable.profile_icon)
            .error(R.drawable.profile_icon)
            .into(patientPicture)

        patientNum.text = patientData?.patientnum
        patientFirstName.text = patientData?.firstname
        patientLastName.text = patientData?.lastname
        patientGender.text = patientData?.gender
        patientAge.text = patientData?.age
        patientHeight.text = patientData?.height
        patientWeight.text = patientData?.weight

        val History_Heart_Rate_History = findViewById<ImageView>(R.id.history_bpm_bttn)
        val History_Temp_History = findViewById<ImageView>(R.id.history_temp_bttn)
        val History_Resp_Rate_History = findViewById<ImageView>(R.id.respiratory_rate_bttn)
        val Back_Button = findViewById<ImageView>(R.id.back_bttn)
        val heart = findViewById<ImageView>(R.id.heart_rate)
        val temp = findViewById<ImageView>(R.id.human_temp)
        val resp = findViewById<ImageView>(R.id.resp)
        val rec1 = findViewById<ImageView>(R.id.bpm_line_chart)
        val rec2 = findViewById<ImageView>(R.id.temp_line_chart)
        val rec3 = findViewById<ImageView>(R.id.resp_line_chart)

        rec3.setOnClickListener {
            val intent = Intent (this, Add_Record_3::class.java)
            startActivity(intent)
        }

        rec2.setOnClickListener {
            val intent = Intent (this, Add_Record_2::class.java)
            startActivity(intent)
        }

        rec1.setOnClickListener {
            val intent = Intent (this, Add_Record::class.java)
            startActivity(intent)
        }

        resp.setOnClickListener {
            val intent = Intent (this, Resp_Value::class.java)
            startActivity(intent)
        }

        temp.setOnClickListener {
            val intent = Intent (this, Temp_Value::class.java)
            startActivity(intent)
        }

        heart.setOnClickListener {
            val intent = Intent (this, Heart_Rate_Value::class.java)
            startActivity(intent)
        }

        Back_Button.setOnClickListener {
            val intent = Intent (this, Home_Dashboard::class.java)
            startActivity(intent)
        }

        History_Resp_Rate_History.setOnClickListener {
            val intent = Intent (this, Respiratory_Rate_History::class.java)
            startActivity(intent)
        }

        History_Temp_History.setOnClickListener {
            val intent = Intent (this, Temperature_History::class.java)
            startActivity(intent)
        }

        History_Heart_Rate_History.setOnClickListener {
            val intent = Intent (this, Heart_Rate_History::class.java)
            startActivity(intent)
        }
    }
}