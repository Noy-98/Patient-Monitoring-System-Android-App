package com.jay.patientmonitoringapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TempRateAdapter(private val tempRateList: List<TempRateData>) :
    RecyclerView.Adapter<TempRateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateStamp: TextView = itemView.findViewById(R.id.temp_rate_datestamp)
        val tempRateValue: TextView = itemView.findViewById(R.id.temp_rate_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.temp_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tempRateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tempRateData = tempRateList[position]

        holder.dateStamp.text = tempRateData.dateStamp
        holder.tempRateValue.text = tempRateData.tempRate
    }
}