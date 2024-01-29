package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chartWeightAreaVp.adapter = ChartSliderVPAdapter(requireActivity())

        TabLayoutMediator(binding.chartWeightTab,binding.chartWeightAreaVp) { tab, position ->
            tab.text = items[position]
        }.attach()
    }
}