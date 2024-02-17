package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentChartKcalMonthBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartKcalMonthFragment : Fragment() {

    lateinit var binding : FragmentChartKcalMonthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChartKcalMonthBinding.inflate(inflater, container, false)
        return binding. root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //날짜 리사이클러뷰 관련 코드
        //LinearLayoutManager for horizontal scrolling
        val monthdateLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.chartKcalMonthDateRv.layoutManager = monthdateLayoutManager

        //date list
        val monthdateList = getMonthDateList()

        //this month date list
        val thismonthdateList = getMonththisDateList()
        binding.chartKcalMonthDateTv.text = thismonthdateList[6]

        // Create and set the adapter
        val monthdateAdapter = DateAdapter(monthdateList)
        binding.chartKcalMonthDateRv.adapter = monthdateAdapter

        //스택 바 리사이클러뷰 관련 코드
        val stackLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.chartKcalMonthRv.layoutManager = stackLayoutManager

        // Example data for the chart items (you can replace this with your actual data)
        val chartItems =  listOf<StackChartItem>(
            StackChartItem(100, 230, 300),
            StackChartItem(450, 100, 250),
            StackChartItem(750, 100, 300),
            StackChartItem(200, 1000, 500),
            StackChartItem(1000, 600, 700),
            StackChartItem(100, 200, 300),
            StackChartItem(1500, 200, 100)
        )

        // Get the StackChartItem at the desired index
        val selectedItem = chartItems[6]

        // Set the values to the corresponding TextViews
        binding.chartKcalMonthSaccTv.text = selectedItem.carboValue.toString()
        binding.chartKcalMonthProteinTv.text = selectedItem.proteinValue.toString()
        binding.chartKcalMonthFatTv.text = selectedItem.fatValue.toString()


        // Create and set the adapter
        val stackBarAdapter = StackBarChartAdapter(chartItems)
        binding.chartKcalMonthRv.adapter = stackBarAdapter

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



}