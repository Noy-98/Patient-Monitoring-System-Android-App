package com.jay.patientmonitoringapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class Edit_Profile : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: FirebaseDatabase

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    val profileImageView = findViewById<ShapeableImageView>(R.id.profile_pic)
                    Glide.with(this)
                        .load(imageUri)
                        .into(profileImageView)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        var back_bttn = findViewById<ImageView>(R.id.back_bttn)

        back_bttn.setOnClickListener {
            val intent = Intent (this, Profile::class.java)
            startActivity(intent)
        }

        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance()

        val editImageButton = findViewById<FloatingActionButton>(R.id.edit_img)
        editImageButton.setOnClickListener {
            openImagePicker()
        }

        val editProfileButton = findViewById<AppCompatButton>(R.id.editProfile_bttn)
        editProfileButton.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val doctorReference = databaseReference.getReference("UserData/Doctor/$uid")

            // Update profile picture
            if (::imageUri.isInitialized) {
                val imageRef = storageReference.child("images/$uid/${imageUri.lastPathSegment}")
                imageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { uri: Uri ->
                            doctorReference.child("imageUrl").setValue(uri.toString())
                        }
                    }
            }

            // Update other profile information
            val firstName = findViewById<TextInputEditText>(R.id.first_name).text.toString()
            val lastName = findViewById<TextInputEditText>(R.id.last_name).text.toString()
            val mobileNum = findViewById<TextInputEditText>(R.id.mobile_num).text.toString()

            doctorReference.child("firstname").setValue(firstName)
            doctorReference.child("lastname").setValue(lastName)
            doctorReference.child("mobilenum").setValue(mobileNum)

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getContent.launch(intent)
    }
}