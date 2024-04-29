package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BPM_Records : AppCompatActivity() {

    private lateinit var bpmRecordAdapter: BPMRecordAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var bpmList: MutableList<Record_Data_DB>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bpm_records)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.bpm_List)
        bpmList = mutableListOf()
        bpmRecordAdapter = BPMRecordAdapter(this, bpmList)
        recyclerView.adapter = bpmRecordAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase database reference
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData/Doctor/$uid/Records/BPM")

            // Retrieve patient data from Firebase
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        bpmList.clear()
                        for (patientSnapshot in snapshot.children) {
                            val rec = patientSnapshot.getValue(Record_Data_DB::class.java)
                            if (rec != null) {
                                bpmList.add(rec)
                            }
                        }
                        bpmRecordAdapter.notifyDataSetChanged()

                        // Add a log statement to check the size of bpmList
                        Log.d("BPM_Records", "BPM List size: ${bpmList.size}")
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