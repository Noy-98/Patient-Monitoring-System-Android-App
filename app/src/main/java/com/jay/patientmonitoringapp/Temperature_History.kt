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

class Temperature_History : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tempRateAdapter: TempRateAdapter
    private lateinit var tempRateList: ArrayList<TempRateData>

    // Initialize Firebase Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_history)

        var back_button = findViewById<ImageView>(R.id.back_bttn)

        back_button.setOnClickListener {
            val intent = Intent (this, Patient_Details::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.patientList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tempRateList = ArrayList()
        tempRateAdapter = TempRateAdapter(tempRateList)
        recyclerView.adapter = tempRateAdapter

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("UserData").child("HardwareData")


        // Set up ValueEventListener to listen for data changes
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if the data exists
                if (snapshot.exists()) {
                    // Get the latest heart rate data
                    val tempRate = snapshot.child("Human Temperature").value.toString()
                    val dateStamp = snapshot.child("DateStamp").value.toString()

                    // Add new heart rate data to the list
                    val newTempRateData = TempRateData(dateStamp, tempRate)
                    tempRateList.add(newTempRateData)

                    // Notify the adapter that the data set has changed
                    tempRateAdapter.notifyDataSetChanged()

                    // Scroll to the latest item in the RecyclerView
                    recyclerView.smoothScrollToPosition(tempRateList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}