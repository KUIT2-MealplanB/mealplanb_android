package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentChartKcalWeekBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartKcalWeekFragment : Fragment() {

    lateinit var binding : FragmentChartKcalWeekBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChartKcalWeekBinding.inflate(inflater, container, false)
        return binding. root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //날짜 리사이클러뷰 관련 코드
        //LinearLayoutManager for horizontal scrolling
        val weekdateLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.chartKcalWeekDateRv.layoutManager = weekdateLayoutManager

        //date list
        val weekdateList = getWeekDateList()

        //이번 주 날짜 설정
        val weekthisdate = getWeekthisDateList()
        binding.chartKcalWeekDateTv.text = weekthisdate[6]

        // Create and set the adapter
        val weekdateAdapter = DateAdapter(weekdateList)
        binding.chartKcalWeekDateRv.adapter = weekdateAdapter

        //스택 바 리사이클러뷰 관련 코드
        val stackLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.chartKcalWeekRv.layoutManager = stackLayoutManager

        // Example data for the chart items (you can replace this with your actual data)
        val chartItems =  listOf<StackChartItem>(
            StackChartItem(500, 500, 500),
            StackChartItem(500, 100, 500),
            StackChartItem(500, 1000, 500),
            StackChartItem(1000, 500, 500),
            StackChartItem(1000, 1000, 200),
            StackChartItem(100, 500, 300),
            StackChartItem(1000, 500, 800)
        )

        // Get the StackChartItem at the desired index
        val selectedItem = chartItems[6]

        // Set the values to the corresponding TextViews
        binding.chartKcalWeekSaccTv.text = selectedItem.carboValue.toString()
        binding.chartKcalWeekProteinTv.text = selectedItem.proteinValue.toString()
        binding.chartKcalWeekFatTv.text = selectedItem.fatValue.toString()

        // Create and set the adapter
        val stackBarAdapter = StackBarChartAdapter(chartItems)
        binding.chartKcalWeekRv.adapter = stackBarAdapter

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



}