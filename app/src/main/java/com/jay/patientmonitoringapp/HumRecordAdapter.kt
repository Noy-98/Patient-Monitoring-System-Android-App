package com.jay.patientmonitoringapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HumRecordAdapter(private val context: Context, private var humList: List<Record_Data_DB3>) :
    RecyclerView.Adapter<HumRecordAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientNum: TextView = itemView.findViewById(R.id.patient_num)
        val humVal: TextView = itemView.findViewById(R.id.hum_value)
        val stat: TextView = itemView.findViewById(R.id.status)
        val day: TextView = itemView.findViewById(R.id.day)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hum_records_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = humList[position]

        holder.patientNum.text = record.patientnum
        holder.humVal.text = record.humvalue
        holder.stat.text = record.status
        holder.day.text = record.nameday
        holder.date.text = record.date
        holder.time.text = record.time
    }

    override fun getItemCount(): Int {
        return humList.size
    }
}
