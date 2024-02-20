package com.example.mealplanb.remote

interface StatWeightView {
    fun StatWeightDayCheckSuccess(statistic_type: String, weights: List<WeightResponse>)
    fun StatWeightWeekCheckSuccess(statistic_type: String, weights: List<WeeklyWeight>)
    fun StatWeightMonthCheckSuccess(statistic_type: String, weights: List<MonthlyWeight>)
}