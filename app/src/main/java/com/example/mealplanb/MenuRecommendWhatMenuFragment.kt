package com.example.mealplanb

import android.content.Context
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

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 클릭 이벤트 설정
        binding.menuRecommWhatMenuCheatButtonLv.setOnClickListener {
            // 클릭 시 WhatMenuFragment로 교체
            val cheatdayFragment = MenuRecommendCheatdayFragment()

            editor.putInt("recommSelect",1)
            editor.apply()

            val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserCheatItem("치팅데이", "먹고싶은거 아무거나 먹을래요!",4))

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, cheatdayFragment)
                .commit()
        }

        binding.menuRecommWhatMenuPopularButtonLv.setOnClickListener {
            val cheatdayFragment = MenuRecommendCheatdayFragment()

            editor.putInt("recommSelect",3)
            editor.apply()

            val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserCheatItem("인기있는","다른 사람들이 많이 먹는 걸로 할래요!",4))

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, cheatdayFragment)
                .commit()
        }

        binding.menuRecommWhatMenuOftenButtonLv.setOnClickListener {
            val cheatdayFragment = MenuRecommendCheatdayFragment()

            editor.putInt("recommSelect",2)
            editor.apply()

            val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserCheatItem("자주먹는","가장 많이 먹은 걸로 할래요!",4))

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, cheatdayFragment)
                .commit()
        }

        return binding.root
    }

}