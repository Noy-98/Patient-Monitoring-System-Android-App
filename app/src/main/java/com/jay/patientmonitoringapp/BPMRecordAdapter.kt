package com.jay.patientmonitoringapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BPMRecordAdapter(private val context: Context, private var bpmList: List<Record_Data_DB>) :
    RecyclerView.Adapter<BPMRecordAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientNum: TextView = itemView.findViewById(R.id.patient_num)
        val bpmVal: TextView = itemView.findViewById(R.id.heart_rate_value)
        val stat: TextView = itemView.findViewById(R.id.status)
        val day: TextView = itemView.findViewById(R.id.day)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bpm_records_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = bpmList[position]

        holder.patientNum.text = record.patientnum
        holder.bpmVal.text = record.bpmvalue
        holder.stat.text = record.status
        holder.day.text = record.nameday
        holder.date.text = record.date
        holder.time.text = record.time
    }

    override fun getItemCount(): Int {
        return bpmList.size
    }
}
