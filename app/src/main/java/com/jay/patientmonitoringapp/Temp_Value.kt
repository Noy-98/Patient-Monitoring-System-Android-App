package com.jay.patientmonitoringapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Temp_Value : AppCompatActivity() {

    // Initialize Firebase Database
    private lateinit var database: DatabaseReference

    // Declare TextViews
    private lateinit var tempValueTextView: TextView
    private lateinit var timestampTempTextView: TextView

    private val CHANNEL_ID = "SensorsChannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_value)

        // Initialize TextViews
        tempValueTextView = findViewById(R.id.temp_value)
        timestampTempTextView = findViewById(R.id.timestamp_temp)

        val Back_Button = findViewById<ImageView>(R.id.back_bttn)

        // Initialize Firebase Database
        //database = FirebaseDatabase.getInstance().reference.child("UserData").child("HardwareData")

        createNotificationChannel()

        // Fetch data from Firebase
        fetchDataFromFirebase()

        Back_Button.setOnClickListener {
            val intent = Intent (this, Patient_Details::class.java)
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Patient Temperature Warning"
            val descriptionText = "Channel for sensor notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun fetchDataFromFirebase() {
        database = FirebaseDatabase.getInstance().getReference()
        val listen = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempValue = snapshot.child("UserData/HardwareData/Human Temp").value.toString()
                val timestampTemp = snapshot.child("UserData/HardwareData/TimeStamp").value.toString()
                tempValueTextView.text = "$tempValue"
                timestampTempTextView.text = "$timestampTemp"

                if (tempValue < 37.00.toString()) {
                    postNotification("Patient Temperature is very low")
                } else if (tempValue > 37.00.toString()) {
                    postNotification("Patient Temperature is very high")
                 }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Temp_Value,"Failed to data", Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(listen)

//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // Check if the data exists
//                if (snapshot.exists()) {
//                    // Get data from snapshot
//                    val tempValue = snapshot.child("Human Temperature").value.toString()
//                    val timestampTemp = snapshot.child("TimeStamp").value.toString()
//
//                    // Update TextViews with the fetched data
//                    tempValueTextView.text = tempValue
//                    timestampTempTextView.text = timestampTemp
//
//                    if (tempValue < 37.00.toString()) {
//                        postNotification("Patient Temperature is very low")
//                    } else if (tempValue > 37.00.toString()) {
//                        postNotification("Patient Temperature is very high")
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
    }

    private fun postNotification(message: String) {

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.temperature_icon)
            .setContentTitle("Temperature Alert")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@Temp_Value,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(3, builder.build())
        }

    }
}