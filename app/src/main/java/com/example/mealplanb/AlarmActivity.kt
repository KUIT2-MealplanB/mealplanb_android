package com.example.mealplanb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.ActivityAlarmBinding
class AlarmActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)

        binding.alarmActivityBackIv.setOnClickListener {
            finish()
        }

        setContentView(binding.root)

        val alarms = listOf(
            AlarmData("꿀꿀", "10.13", "한 끼")
        )
        val adapter = AlarmAdapter(alarms)
        binding.alarmActivityRv.layoutManager = LinearLayoutManager(this)
        binding.alarmActivityRv.adapter = adapter
    }

}