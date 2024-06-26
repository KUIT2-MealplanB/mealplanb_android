package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mealplanb.databinding.FragmentChartWeightWeekBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartWeightWeekFragment : Fragment() {
    lateinit var binding : FragmentChartWeightWeekBinding
    var chartEntry = arrayListOf<Entry>()
    var dataList : List<ChartCommitData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartWeightWeekBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxWeight = 60f
        val minWeight = 50f

        val thisweekList = getWeekthisDateList()
        val weekList = getWeekDateList()
        binding.chartWeightWeekDateTv.text = thisweekList[6]

        dataList = arrayListOf(
            ChartCommitData(weekList[0],58),
            ChartCommitData(weekList[1],56),
            ChartCommitData(weekList[2],55),
            ChartCommitData(weekList[3],57),
            ChartCommitData(weekList[4],56),
            ChartCommitData(weekList[5],57),
            ChartCommitData(weekList[6],55)
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

    private fun getWeekDateList(): List<String> {
        val dateList = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val datethisFormat = SimpleDateFormat("mm.dd", Locale.getDefault())

        for (i in 0 until 7) { // 7개의 주 생성
            // 현재 주의 마지막 날짜를 구함
            val endOfWeek = dateFormat.format(calendar.time)

            // 현재 주의 첫 날짜를 구함 (7일 전부터 시작하므로 -6을 해줌)
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val startOfWeek = dateFormat.format(calendar.time)

            // 현재 주의 기간을 리스트에 추가
            dateList.add("$startOfWeek-$endOfWeek")

            // 다음 주로 이동
            calendar.add(Calendar.DAY_OF_YEAR, -7)
        }

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
        dateList.reverse()

        return dateList
    }

    private fun getWeekthisDateList(): List<String> {
        val this_week_date_list = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        val datethisFormat = SimpleDateFormat("MM.dd", Locale.getDefault())

        for (i in 0 until 7) { // 7개의 주 생성
            // 현재 주의 마지막 날짜를 구함
            val endOfthisWeek = datethisFormat.format(calendar.time)

            // 현재 주의 첫 날짜를 구함 (7일 전부터 시작하므로 -6을 해줌)
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val startOfthisWeek = datethisFormat.format(calendar.time)

            // 현재 주의 기간을 리스트에 추가
            this_week_date_list.add("$startOfthisWeek ~ $endOfthisWeek")

            // 다음 주로 이동
            calendar.add(Calendar.DAY_OF_YEAR, -7)
        }

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
        this_week_date_list.reverse()

        return this_week_date_list
    }

    class XAxisCustomFormatter(val xAxisData : List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }
}