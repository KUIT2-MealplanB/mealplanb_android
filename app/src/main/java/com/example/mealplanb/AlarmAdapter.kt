package com.example.mealplanb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemAlarmBinding

class AlarmAdapter(private val alarms : List<AlarmData>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(val binding:ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAlarmBinding.inflate(layoutInflater, parent, false)
        return AlarmViewHolder(binding)
    }

    override fun getItemCount():Int{
       return alarms.size
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.binding.itemAlarmDateTv.text = alarm.date
        holder.binding.itemAlarmNameTv.text = alarm.name
        holder.binding.itemAlarmDaymealTv.text = alarm.mealNum
    }


}