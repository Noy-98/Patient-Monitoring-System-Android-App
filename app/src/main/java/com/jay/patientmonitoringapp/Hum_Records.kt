package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Hum_Records : AppCompatActivity() {

    private lateinit var humRecordAdapter: HumRecordAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var humList: MutableList<Record_Data_DB3>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hum_records)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.hum_List)
        humList = mutableListOf()
        humRecordAdapter = HumRecordAdapter(this, humList)
        recyclerView.adapter = humRecordAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase database reference
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData/Doctor/$uid/Records/Humidity")

            // Retrieve patient data from Firebase
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        humList.clear()
                        for (patientSnapshot in snapshot.children) {
                            val rec3 = patientSnapshot.getValue(Record_Data_DB3::class.java)
                            if (rec3 != null) {
                                humList.add(rec3)
                            }
                        }
                        humRecordAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        val backButton: ImageView = findViewById(R.id.back_bttn)

        backButton.setOnClickListener {
            val intent = Intent (this, Records::class.java)
            startActivity(intent)
        }
    }
}