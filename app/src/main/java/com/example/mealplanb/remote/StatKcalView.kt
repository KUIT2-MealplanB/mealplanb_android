package com.example.mealplanb.remote

interface StatKcalView {
    fun StatKcalDayCheckSuccess(statistic_type: String, kcals: List<DailyKcal>)
    fun StatKcalWeekCheckSuccess(statistic_type: String, kcals: List<WeeklyKcal>)
    fun StatKcalMonthCheckSuccess(statistic_type: String, kcals: List<MonthlyKcal>)
}