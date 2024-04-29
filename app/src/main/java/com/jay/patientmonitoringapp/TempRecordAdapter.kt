package com.jay.patientmonitoringapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TempRecordAdapter(private val context: Context, private var tempList: List<Record_Data_DB2>) :
    RecyclerView.Adapter<TempRecordAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientNum: TextView = itemView.findViewById(R.id.patient_num)
        val tempVal: TextView = itemView.findViewById(R.id.temp_value)
        val stat: TextView = itemView.findViewById(R.id.status)
        val day: TextView = itemView.findViewById(R.id.day)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.temp_records_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = tempList[position]

        holder.patientNum.text = record.patientnum
        holder.tempVal.text = record.tempvalue
        holder.stat.text = record.status
        holder.day.text = record.nameday
        holder.date.text = record.date
        holder.time.text = record.time
    }

    override fun getItemCount(): Int {
        return tempList.size
    }
}
