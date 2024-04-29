package com.jay.patientmonitoringapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class Add_Patient : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var currentDoctorId: String
    private lateinit var ProgressBar: ProgressBar
    private lateinit var Add_Picture: AppCompatButton
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.home) {
                startActivity(Intent(applicationContext, Home_Dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            }  else if (item.itemId == R.id.profile) {
                startActivity(Intent(applicationContext, Profile::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
                return@setOnItemSelectedListener true
            } else if (item.itemId == R.id.logout) {
                startActivity(Intent(applicationContext, Login::class.java))
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
                return@setOnItemSelectedListener true
            }
            false
        }

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Get the current doctor's ID (UID is assumed to be the last name)
        val currentUser = auth.currentUser
        currentDoctorId = currentUser?.uid ?: ""

        val Patient_Num = findViewById<TextInputEditText>(R.id.patient_num)
        val FirstName = findViewById<TextInputEditText>(R.id.firstname)
        val LastName = findViewById<TextInputEditText>(R.id.lastname)
        val Gender= findViewById<TextInputEditText>(R.id.gender)
        val Age = findViewById<TextInputEditText>(R.id.age)
        val Weight = findViewById<TextInputEditText>(R.id.weight)
        val Height = findViewById<TextInputEditText>(R.id.height)
        val Save_Button = findViewById<AppCompatButton>(R.id.save_bttn)
        Add_Picture = findViewById(R.id.add_pic_bttn)
        ProgressBar = findViewById(R.id.signUpProgressBar)
        var back_button = findViewById<ImageView>(R.id.back_bttn)

        storageReference = FirebaseStorage.getInstance().reference

        Add_Picture.setOnClickListener {
            openImageChooser()
        }

        Save_Button.setOnClickListener {
            val Patienno = Patient_Num.text.toString()
            val fname = FirstName.text.toString()
            val lname = LastName.text.toString()
            val gen = Gender.text.toString()
            val age = Age.text.toString()
            val weight = Weight.text.toString()
            val height = Height.text.toString()

            ProgressBar.visibility = View.VISIBLE

            if (Patienno.isEmpty() || fname.isEmpty() || lname.isEmpty() || gen.isEmpty() || age.isEmpty() || weight.isEmpty() || height.isEmpty()){
                if (Patienno.isEmpty()){
                    Patient_Num.error = "Enter Patient Number"
                }
                if (fname.isEmpty()){
                    FirstName.error = "Enter Patient First Name"
                }
                if (lname.isEmpty()){
                    LastName.error = "Enter Patient Last Name"
                }
                if (gen.isEmpty()){
                    Gender.error = "Enter Patient Gender"
                }
                if (age.isEmpty()){
                    Age.error = "Enter Patient Age"
                }
                if (weight.isEmpty()){
                    Weight.error = "Enter Patient Weight"
                }
                if (height.isEmpty()){
                    Height.error = "Enter Patient Height"
                }
                Toast.makeText(this,"All fields are Required!", Toast.LENGTH_SHORT).show()
                ProgressBar.visibility = View.GONE
            }else{
                val patientData = Patient_Data_DB(
                    patientnum = Patienno,
                    firstname = fname,
                    lastname = lname,
                    gender = gen,
                    age = age,
                    weight = weight,
                    height = height
                )
                uploadImage(patientData)
            }
        }

        back_button.setOnClickListener {
            val intent = Intent (this, Home_Dashboard::class.java)
            startActivity(intent)
        }
    }

    private fun uploadImage(patientData: Patient_Data_DB) {
        val imageRef = storageReference.child("images/${System.currentTimeMillis()}.jpg")
        val imageView = findViewById<ShapeableImageView>(R.id.patient_picture)
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val uploadTask = imageRef.putBytes(imageData)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                savePatientData(patientData, downloadUrl)
            } else {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                ProgressBar.visibility = View.GONE
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)

            // Display the selected image in the ImageView
            val imageView = findViewById<ShapeableImageView>(R.id.patient_picture)
            imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE
        }
    }

    private fun savePatientData(patientData: Patient_Data_DB, imageUrl: String) {
        patientData.imageUrl = imageUrl

        val doctorsPatientsRef = database.reference.child("UserData").child("Doctor").child(currentDoctorId).child("Patients")
        val newPatientRef = doctorsPatientsRef.push() // Automatically generates a unique key

        newPatientRef.setValue(patientData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Patient data saved successfully", Toast.LENGTH_SHORT).show()
                    ProgressBar.visibility = View.GONE
                } else {
                    Toast.makeText(this, "Failed to save patient data", Toast.LENGTH_SHORT).show()
                    ProgressBar.visibility = View.GONE
                }
            }
    }
}