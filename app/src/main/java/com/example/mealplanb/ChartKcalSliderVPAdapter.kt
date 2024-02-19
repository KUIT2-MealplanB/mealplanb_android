package com.example.mealplanb

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChartKcalSliderVPAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ChartKcalDayFragment()
            1 -> ChartKcalWeekFragment()
            else -> ChartKcalMonthFragment()
        }
    }

}