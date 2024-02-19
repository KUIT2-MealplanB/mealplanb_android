package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentMenuRecommendWhatMenuBinding

class MenuRecommendWhatMenuFragment : Fragment() {

    lateinit var binding : FragmentMenuRecommendWhatMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendWhatMenuBinding.inflate(layoutInflater)

        // 클릭 이벤트 설정
        binding.menuRecommWhatMenuCheatButtonLv.setOnClickListener {
            // 클릭 시 WhatMenuFragment로 교체
            val cheatdayFragment = MenuRecommendCheatdayFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, cheatdayFragment)
                .commit()
        }

        return binding.root
    }

}