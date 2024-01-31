package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mealplanb.databinding.FragmentChartWeightDayBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class ChartWeightDayFragment : Fragment() {
    lateinit var binding : FragmentChartWeightDayBinding
    var chartEntry = arrayListOf<Entry>()
    var dataList : List<ChartCommitData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartWeightDayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxWeight = 60f
        val minWeight = 50f
        dataList = arrayListOf(
            ChartCommitData("1.24",60),
            ChartCommitData("1.25",55),
            ChartCommitData("1.26",58),
            ChartCommitData("1.27",57),
            ChartCommitData("1.28",54),
            ChartCommitData("1.29",56),
            ChartCommitData("1.30",55)
        )

        for(i in dataList.indices) { //indices : dataList의 최소 index ~ 최대 index
            chartEntry.add(Entry(i.toFloat(),dataList[i].commitNum.toFloat()))
        }

        val lineDataSet = LineDataSet(chartEntry,"chartEntry")
//        lineDataSet.color = resources.getColor(R.color.point)
//
//        binding.chartWeightDayLinechart.data = LineData(lineDataSet)
//
//        binding.chartWeightDayLinechart.xAxis.position = XAxis.XAxisPosition.BOTTOM
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
            setLabelCount(7,true)
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

    fun changeDateText(dataList : List<ChartCommitData>): List<String> {
        val dataTextList = ArrayList<String>()
        for(i in dataList.indices) {
            dataTextList.add(dataList[i].date)
        }
        return dataTextList
    }

    class XAxisCustomFormatter(val xAxisData : List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }
}