package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.mealplanb.databinding.FragmentChartWeightBinding
import com.google.android.material.tabs.TabLayoutMediator

class ChartWeightFragment : Fragment() {
    lateinit var binding : FragmentChartWeightBinding
    private val items = arrayOf<String>("일간","주간","월간")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentChartWeightBinding.inflate(layoutInflater)

        //목표 체중 설정
        val sharedPref = activity?.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val diffWeight = sharedPref?.getFloat("diffWeight", 0.0f) ?: 0.0f
        binding.chartWeightGoalWeightTv.text = diffWeight.toString()

        // 목표 식단 설정
        val selectedDiet = sharedPref?.getString("selectedDiet", "키토 식단") ?: "Default Diet"
        binding.chartWeightGoalDietTv.text = selectedDiet.toString()


        //체중 버튼을 누르면
        binding.chartWeightBtnweightLl.setOnClickListener {
            // 칼로리 버튼의 스타일 변경
            binding.chartWeightBtncalLl.setBackgroundResource(R.drawable.chart_fragment_btn_disabled)
            // 다른 버튼의 스타일과 색을 초기화하거나 변경할 수 있으면 아래와 같이 수행
            binding.chartWeightBtnweightLl.setBackgroundResource(R.drawable.chart_fragment_btn)

            binding.chartWeightAreaVp.adapter = ChartSliderVPAdapter(requireActivity())

            TabLayoutMediator(binding.chartWeightTab,binding.chartWeightAreaVp) { tab, position ->
                tab.text = items[position]
            }.attach()

        }
        //칼로리 버튼을 누르면
        binding.chartWeightBtncalLl.setOnClickListener {

            // 칼로리 버튼의 스타일 변경
            binding.chartWeightBtncalLl.setBackgroundResource(R.drawable.chart_fragment_btn)
            // 다른 버튼의 스타일과 색을 초기화하거나 변경할 수 있으면 아래와 같이 수행
            binding.chartWeightBtnweightLl.setBackgroundResource(R.drawable.chart_fragment_btn_disabled)

            binding.chartWeightAreaVp.adapter = ChartKcalSliderVPAdapter(requireActivity())

            TabLayoutMediator(binding.chartWeightTab,binding.chartWeightAreaVp) { tab, position ->
                tab.text = items[position]
            }.attach()
        }

        binding.chartWeightGoalModifyLl.setOnClickListener {
            val anotherFragment = GoalMotifFragment()
            val bundle = Bundle()
            bundle.putString("source", "StatFragment") // 출처 적어두기
            anotherFragment.arguments = bundle

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.main_frm, anotherFragment)
            fragmentTransaction?.commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //초기 뷰페이저를 칼로리로 설정
        binding.chartWeightAreaVp.adapter = ChartKcalSliderVPAdapter(requireActivity())

        TabLayoutMediator(binding.chartWeightTab,binding.chartWeightAreaVp) { tab, position ->
            tab.text = items[position]
        }.attach()
    }

}