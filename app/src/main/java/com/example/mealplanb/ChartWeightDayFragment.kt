package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mealplanb.databinding.FragmentChartWeightDayBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.MonthlyWeight
import com.example.mealplanb.remote.StatView
import com.example.mealplanb.remote.StatWeightView
import com.example.mealplanb.remote.WeeklyWeight
import com.example.mealplanb.remote.WeightResponse
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartWeightDayFragment : Fragment(), StatView, StatWeightView {
    lateinit var binding : FragmentChartWeightDayBinding
    var chartEntry = arrayListOf<Entry>()
    var dataList : MutableList<ChartCommitData> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartWeightDayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thisdateList = getNewDate()
//        val dateList = getDateList()
        binding.chartWeightDayDateTv.text = thisdateList[6]

//        dataList = arrayListOf(
//            ChartCommitData(dateList[0],60),
//            ChartCommitData(dateList[1],55),
//            ChartCommitData(dateList[2],58),
//            ChartCommitData(dateList[3],57),
//            ChartCommitData(dateList[4],54),
//            ChartCommitData(dateList[5],56),
//            ChartCommitData(dateList[6],55)
//        )

        val authService = AuthService(requireContext())
        authService.setStatView(this)
        authService.setStatWeightView(this)
        authService.statWeightDayCheck()

    }

    fun changeDateText(dataList : List<ChartCommitData>): List<String> {
        val dataTextList = ArrayList<String>()
        for(i in dataList.indices) {
            dataTextList.add(dataList[i].date)
        }
        return dataTextList
    }

    private fun getDate(inputDate: String): String {
        val dateList = mutableListOf<String>()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
//        dateList.add(dateFormat.format(inputFormat.parse(inputDate)))
//            dateList.add(dateFormat.format(calendar.time))

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
//        dateList.reverse()

        return dateFormat.format(inputFormat.parse(inputDate))
    }

    private fun getNewDate(): List<String> {
        val thisdateList = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        for (i in 0 until 7) {
            val dateFormat = SimpleDateFormat("MM.dd(EEE)", Locale.getDefault())
            thisdateList.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, -1) // 1일씩 이전 날짜로 이동
        }

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
        thisdateList.reverse()

        return thisdateList
    }

    class XAxisCustomFormatter(val xAxisData : List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }

    override fun StatPlanCheckSuccess(
        initial_weight: Double,
        target_weight: Double,
        diet_type: String
    ) {
        TODO("Not yet implemented")
    }

    override fun StatWeightDayCheckSuccess(
        statistic_type: String,
        weights: List<WeightResponse>
    ) {
        val maxWeight = 100f
        val minWeight = 40f

        for(i in weights.indices) { //indices : dataList의 최소 index ~ 최대 index
            chartEntry.add(Entry(i.toFloat(),weights[i].weight.toFloat()))
            dataList.add(ChartCommitData(getDate(weights[i].date.toString()),weights[i].weight.toInt()))
        }

//        dataList = mutableListOf()
//        dataList.add(ChartCommitData(getDate(weights[0].date.toString()),weights[0].weight.toInt()))
//        if(dataList.size == 1) {
//            chartEntry.add(Entry(1.toFloat(),weights[0].weight.toFloat()))
//            dataList.add(ChartCommitData(getDate(weights[0].date.toString()),weights[0].weight.toInt()))
//            chartEntry.add(Entry(2.toFloat(),weights[0].weight.toFloat()))
//            dataList.add(ChartCommitData(getDate(weights[0].date.toString()),weights[0].weight.toInt()))
//            chartEntry.add(Entry(3.toFloat(),weights[0].weight.toFloat()))
//            dataList.add(ChartCommitData(getDate(weights[0].date.toString()),weights[0].weight.toInt()))
//            chartEntry.add(Entry(3.toFloat(),weights[0].weight.toFloat()))
//            dataList.add(ChartCommitData(getDate(weights[0].date.toString()),weights[0].weight.toInt()))
//        }


        Log.d("dataList",dataList.toString())
        val lineDataSet = LineDataSet(chartEntry,"chartEntry")
        lineDataSet.apply {
            color = resources.getColor(R.color.point,null)
            circleRadius = 0f
            lineWidth = 1f
            setDrawCircles(false)
            setCircleColor(resources.getColor(R.color.point,null))
            setDrawHighlightIndicators(false)
            setDrawValues(false)
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(),R.drawable.fade_point_color)
        }
        binding.chartWeightDayLinechart.apply {
            axisLeft.isEnabled = true
            axisRight.isEnabled = false
            description.isEnabled = true
            legend.isEnabled = false
            description.isEnabled = false
            isDragXEnabled = false
            isDragYEnabled = false
            isScaleXEnabled = false
            isScaleYEnabled = false
        }
        binding.chartWeightDayLinechart.xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setDrawLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = XAxisCustomFormatter(changeDateText(dataList))
            textColor = resources.getColor(R.color.mono_gray3,null)
            textSize = 12f
            labelRotationAngle = 0f
            setLabelCount(dataList.size,true)
        }
        binding.chartWeightDayLinechart.axisLeft.apply {
            setDrawAxisLine(false)
            axisMaximum = maxWeight
            axisMinimum = minWeight
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            labelCount = 5
            textColor = resources.getColor(R.color.mono_gray3,null)
            textSize = 12f
        }
        binding.chartWeightDayLinechart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged()
            invalidate()
        }
    }

    override fun StatWeightWeekCheckSuccess(
        statistic_type: String,
        weights: List<WeeklyWeight>
    ) {
        TODO("Not yet implemented")
    }

    override fun StatWeightMonthCheckSuccess(
        statistic_type: String,
        weights: List<MonthlyWeight>
    ) {
        TODO("Not yet implemented")
    }
}