package com.jay.patientmonitoringapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeartRateAdapter(private val heartRateList: List<HeartRateData>) :
    RecyclerView.Adapter<HeartRateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateStamp: TextView = itemView.findViewById(R.id.heart_rate_datestamp)
        val heartRateValue: TextView = itemView.findViewById(R.id.heart_rate_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.heart_rate_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val heartRateData = heartRateList[position]

        holder.dateStamp.text = heartRateData.dateStamp
        holder.heartRateValue.text = heartRateData.heartRate
    }

    override fun getItemCount(): Int {
        return heartRateList.size
    }
}
