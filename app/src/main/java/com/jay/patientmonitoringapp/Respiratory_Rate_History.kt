package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Respiratory_Rate_History : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var respRateAdapter: RespRateAdapter
    private lateinit var respRateList: ArrayList<RespRateData>

    // Initialize Firebase Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_respiratory_rate_history)

        var back_button = findViewById<ImageView>(R.id.back_bttn)

        back_button.setOnClickListener {
            val intent = Intent (this, Patient_Details::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.patientList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        respRateList = ArrayList()
        respRateAdapter = RespRateAdapter(respRateList)
        recyclerView.adapter = respRateAdapter

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("UserData").child("HardwareData")


        // Set up ValueEventListener to listen for data changes
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if the data exists
                if (snapshot.exists()) {
                    // Get the latest heart rate data
                    val respRate = snapshot.child("Respiratory Rate").value.toString()
                    val dateStamp = snapshot.child("DateStamp").value.toString()

                    // Add new heart rate data to the list
                    val newRespRateData = RespRateData(dateStamp, respRate)
                    respRateList.add(newRespRateData)

                    // Notify the adapter that the data set has changed
                    respRateAdapter.notifyDataSetChanged()

                    // Scroll to the latest item in the RecyclerView
                    recyclerView.smoothScrollToPosition(respRateList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}