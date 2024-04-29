package com.jay.patientmonitoringapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class Home_Dashboard : AppCompatActivity() {

    private lateinit var patientAdapter: PatientAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var patientList: MutableList<Patient_Data_DB>
    private lateinit var searchBox: EditText
    private lateinit var uploadButton: FloatingActionButton
    private lateinit var storageReference: StorageReference

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dashboard)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.home) {
                // Handle Home item
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.record) {
                startActivity(Intent(applicationContext, Records::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.profile) {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
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

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.patientList)
        patientList = mutableListOf()
        patientAdapter = PatientAdapter(this, patientList)
        recyclerView.adapter = patientAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase database reference
        val currentUser = FirebaseAuth.getInstance().currentUser

        val addPatient = findViewById<ImageView>(R.id.add_patient_bttn)
        uploadButton = findViewById(R.id.upload_bttn)
        searchBox = findViewById(R.id.search_box)

        storageReference = FirebaseStorage.getInstance().reference

        uploadButton.setOnClickListener {
            pickImage.launch(Intent(Intent.ACTION_PICK).setType("image/*"))
        }

        loadDoctorProfile()

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                filterPatients(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {
            }

        })

        if (currentUser != null) {
            val uid = currentUser.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData/Doctor/$uid/Patients")

            // Retrieve patient data from Firebase
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        patientList.clear()
                        for (patientSnapshot in snapshot.children) {
                            val patient = patientSnapshot.getValue(Patient_Data_DB::class.java)
                            if (patient != null) {
                                patientList.add(patient)
                            }
                        }
                        patientAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        addPatient.setOnClickListener {
            val intent = Intent (this, Add_Patient::class.java)
            startActivity(intent)
        }
    }

    private fun loadDoctorProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            val uid = currentUser.uid
            val doctorReference = FirebaseDatabase.getInstance().getReference("UserData/Doctor/$uid")

            // Retrieve doctor data from Firebase
            doctorReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val doctor = snapshot.getValue(Employees_Structure_DB::class.java)
                        if (doctor != null) {

                            Glide.with(this@Home_Dashboard)
                                .load(doctor.imageUrl)
                                .into(findViewById<ShapeableImageView>(R.id.profile_pic))

                            findViewById<TextView>(R.id.doc_lastname).text = doctor.lastname
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun filterPatients(query: String) {
        val filteredList = ArrayList<Patient_Data_DB>()

        for (patient in patientList) {
            val fullName = "${patient.firstname?.orEmpty()} ${patient.lastname?.orEmpty()}".toLowerCase()
            if (fullName.contains(query.toLowerCase())) {
                filteredList.add(patient)
            }
        }

        patientAdapter.filterList(filteredList)
    }

    private fun uploadImage(imageUri: android.net.Uri){
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            val uid = currentUser.uid
            val imageName = "image_${UUID.randomUUID()}"
            val imageRef = storageReference.child("images/$uid/$imageName")

            // Upload image to Firebase Storage
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    // Get the download URL for the uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Save the image URL to Firebase Realtime Database
                        saveImageUrlToDatabase(uid, downloadUri.toString())
                    }
                }
                .addOnFailureListener{exception ->
                    
                }
        }
    }

    private fun saveImageUrlToDatabase(uid: String, imageUrl: String) {
// Save the image URL to the Realtime Database under the doctor's UID
        val databaseReference = FirebaseDatabase.getInstance().getReference("UserData/Doctor/$uid")
        databaseReference.child("imageUrl").setValue(imageUrl)
            .addOnSuccessListener {
                // Handle successful image URL saving
            }
            .addOnFailureListener { exception ->
                // Handle failed image URL saving
            }
    }
}