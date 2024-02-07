package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentChartKcalDayBinding
import java.text.SimpleDateFormat
import java.util.*

class ChartKcalDayFragment : Fragment() {

    lateinit var binding: FragmentChartKcalDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentChartKcalDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //날짜 리사이클러뷰 관련 코드
        //LinearLayoutManager for horizontal scrolling
        val daydateLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.chartKcalDayDateRv.layoutManager = daydateLayoutManager

        //date list
        val dateList = getDateList()

        //오늘 날짜 textview 설정

        val thisdateList = getthisDateList()
        // Set the formatted date to the TextView
        binding.chartKcalDayDateTv.text = thisdateList[6]

        // Create and set the adapter
        val dateAdapter = DateAdapter(dateList)
        binding.chartKcalDayDateRv.adapter = dateAdapter

        //스택 바 리사이클러뷰 관련 코드
        val stackLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.chartKcalDayRv.layoutManager = stackLayoutManager

        // Example data for the chart items (you can replace this with your actual data)
        val chartItems = listOf<StackChartItem>(
            StackChartItem(300, 1100, 200),
            StackChartItem(1500, 100, 500),
            StackChartItem(500, 400, 300),
            StackChartItem(200, 500, 500),
            StackChartItem(1000, 600, 400),
            StackChartItem(100, 500, 300),
            StackChartItem(500, 500, 1000)
        )

        // Get the StackChartItem at the desired index
        val selectedItem = chartItems[6]

        // Set the values to the corresponding TextViews
        binding.chartKcalDaySaccTv.text = selectedItem.carboValue.toString()
        binding.chartKcalDayProteinTv.text = selectedItem.proteinValue.toString()
        binding.chartKcalDayFatTv.text = selectedItem.fatValue.toString()


        // Create and set the adapter
        val stackBarAdapter = StackBarChartAdapter(chartItems)
        binding.chartKcalDayRv.adapter = stackBarAdapter

    }
    // This method generates a list of dates from 7 days ago to today
    private fun getDateList(): List<String> {
        val dateList = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        for (i in 0 until 7) {
            val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
            dateList.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, -1) // 1일씩 이전 날짜로 이동
        }

        // 리스트를 역순으로 정렬하여 최신 날짜가 맨 앞에 오도록 함
        dateList.reverse()

        return dateList
    }

    private fun getthisDateList(): List<String> {
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
}