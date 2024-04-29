package com.jay.patientmonitoringapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class PatientAdapter(private val context: Context, private var patientList: List<Patient_Data_DB>) :
    RecyclerView.Adapter<PatientAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientPic: ShapeableImageView = itemView.findViewById(R.id.patient_pic)
        val patientLastName: TextView = itemView.findViewById(R.id.patient_last_name)
        val patientnum: TextView = itemView.findViewById(R.id.patient_id)
        val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetails_bttn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.patient_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patient = patientList[position]

        // Load image from URL using Glide
        Glide.with(context)

            .load(patient.imageUrl) // Assuming imageUrl is the field in your PatientData class
            .placeholder(R.drawable.profile_icon) // Placeholder image while loading
            .error(R.drawable.profile_icon) // Error image if the loading fails
            .into(holder.patientPic)

        holder.patientLastName.text = patient.lastname
        holder.patientnum.text = patient.patientnum

        // Handle click on view details button
        holder.viewDetailsButton.setOnClickListener {
            // Start a new activity to display patient details
            val intent = Intent(context, Patient_Details::class.java)
            intent.putExtra("patientData", patient)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    fun filterList(filteredList: List<Patient_Data_DB>) {
        patientList = filteredList.toMutableList()
        notifyDataSetChanged()
    }
}
