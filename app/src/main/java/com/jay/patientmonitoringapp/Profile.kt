package com.jay.patientmonitoringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.profile
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.home) {
                startActivity(Intent(applicationContext, Home_Dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.record) {
                startActivity(Intent(applicationContext, Records::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.profile) {
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.logout) {
                startActivity(Intent(applicationContext, Login::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            }
            false
        }

        var edit_bttn = findViewById<AppCompatButton>(R.id.edit_bttn)

        edit_bttn.setOnClickListener {
            val intent = Intent (this, Edit_Profile::class.java)
            startActivity(intent)
        }

            loadDoctorProfile()
    }

    private fun loadDoctorProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            val uid = currentUser.uid
            val doctorReference = FirebaseDatabase.getInstance().getReference("UserData/Doctor/$uid")

            doctorReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val doctor = snapshot.getValue(Employees_Structure_DB::class.java)
                        if (doctor != null) {
                            // Load the doctor's profile image using Glide
                            val profileImageView =
                                findViewById<ShapeableImageView>(R.id.profile_pic)
                            Glide.with(this@Profile)
                                .load(doctor.imageUrl)
                                .into(profileImageView)

                            // Set the text views with the doctor's information
                            findViewById<TextView>(R.id.first_name).text = doctor.firstname
                            findViewById<TextView>(R.id.last_name).text = doctor.lastname
                            findViewById<TextView>(R.id.mobile_num).text = doctor.mobilenum
                            findViewById<TextView>(R.id.email).text = doctor.email
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}