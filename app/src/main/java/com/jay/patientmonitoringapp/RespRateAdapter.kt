package com.jay.patientmonitoringapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RespRateAdapter(private val respRateList: List<RespRateData>) :
    RecyclerView.Adapter<RespRateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateStamp: TextView = itemView.findViewById(R.id.resp_rate_datestamp)
        val respRateValue: TextView = itemView.findViewById(R.id.resp_rate_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resp_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return respRateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val respRateData = respRateList[position]

        holder.dateStamp.text = respRateData.dateStamp
        holder.respRateValue.text = respRateData.respRate
    }
}