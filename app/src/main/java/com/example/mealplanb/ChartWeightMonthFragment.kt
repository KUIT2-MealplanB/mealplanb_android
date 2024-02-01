package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mealplanb.databinding.FragmentChartWeightMonthBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartWeightMonthFragment : Fragment() {
    lateinit var binding : FragmentChartWeightMonthBinding
    var chartEntry = arrayListOf<Entry>()
    var dataList : List<ChartCommitData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartWeightMonthBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxWeight = 60f
        val minWeight = 50f

        val thismonthList = getMonththisDateList()
        val monthList = getMonthDateList()
        binding.chartWeightWeekDateTv.text = thismonthList[6]

        dataList = arrayListOf(
            ChartCommitData(monthList[0],55),
            ChartCommitData(monthList[1],57),
            ChartCommitData(monthList[2],54),
            ChartCommitData(monthList[3],56),
            ChartCommitData(monthList[4],57),
            ChartCommitData(monthList[5],55),
            ChartCommitData(monthList[6],55)
        )

        for(i in dataList.indices) { //indices : dataList의 최소 index ~ 최대 index
            chartEntry.add(Entry(i.toFloat(),dataList[i].commitNum.toFloat()))
        }

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
        binding.chartWeightWeekLinechart.apply {
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
        binding.chartWeightWeekLinechart.xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setDrawLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = XAxisCustomFormatter(changeDateText(dataList))
            textColor = resources.getColor(R.color.mono_gray3,null)
            textSize = 12f
            labelRotationAngle = 0f
            setLabelCount(7,true)
        }
        binding.chartWeightWeekLinechart.axisLeft.apply {
            setDrawAxisLine(false)
            axisMaximum = maxWeight
            axisMinimum = minWeight
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            labelCount = 5
            textColor = resources.getColor(R.color.mono_gray3,null)
            textSize = 12f
        }
        binding.chartWeightWeekLinechart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged()
            invalidate()
        }
    }

    fun changeDateText(dataList : List<ChartCommitData>): List<String> {
        val dataTextList = ArrayList<String>()
        for(i in dataList.indices) {
            dataTextList.add(dataList[i].date)
        }
        return dataTextList
    }

    private fun getMonthDateList(): List<String> {
        val dateList = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        val monthFormat = SimpleDateFormat("MM", Locale.getDefault())

        for (i in 0 until 7) { // 7개의 이전 달을 생성
            // 현재 달로부터 i달 전의 달을 구함
            val previousMonth = monthFormat.format(calendar.time)
            calendar.add(Calendar.MONTH, -1)

            // 현재 달의 기간을 리스트에 추가
            dateList.add("  $previousMonth")
        }

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
        dateList.reverse()

        return dateList
    }

    private fun getMonththisDateList(): List<String> {
        val thismonthdateList = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        val monthFormat = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())

        for (i in 0 until 7) { // 7개의 이전 달을 생성
            // 현재 달로부터 i달 전의 달을 구함
            val previousMonth = monthFormat.format(calendar.time)
            calendar.add(Calendar.MONTH, -1)

            // 현재 달의 기간을 리스트에 추가
            thismonthdateList.add("  $previousMonth")
        }

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
        thismonthdateList.reverse()

        return thismonthdateList
    }

    class XAxisCustomFormatter(val xAxisData : List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }
}