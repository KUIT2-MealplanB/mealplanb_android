package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentMenuRecommendHowEatBinding
import com.example.mealplanb.databinding.FragmentMenuRecommendInitBinding

class MenuRecommendInitFragment : Fragment() {
    lateinit var binding: FragmentMenuRecommendInitBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendInitBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

        // 클릭 이벤트 설정
        binding.menuRecommInitBtnWhatCl.setOnClickListener {
            // 클릭 시 WhatMenuFragment로 교체
            val whatMenuFragment = MenuRecommendWhatMenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, whatMenuFragment)
                .commit()

            // Add data directly using the activity's public method
            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserItem("어떤 음식", "을 먹을까요?", 2),
                MenuRecommItem.SystemKcalItem(500, 300,200,100, 3)
            )
        }

        binding.menuRecommInitBtnHowCl.setOnClickListener {
            val howMenuFragment = MenuRecommendHowMenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, howMenuFragment)
                .commit()

            // Add data directly using the activity's public method
            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserItem("얼마나", " 먹을까요?", 2),
                MenuRecommItem.SystemItem("어떤 음식","을 먹고 싶으세요?",3)
            )
        }

        // 다른 초기화 작업 등을 수행할 수 있음
    }
}