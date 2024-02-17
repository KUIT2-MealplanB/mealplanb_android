package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentMenuRecommendInitBinding
import com.example.mealplanb.databinding.FragmentMenuRecommendSelectBinding
import java.util.Calendar

class MenuRecommendSelectFragment : Fragment() {
    lateinit var binding: FragmentMenuRecommendSelectBinding
    private var listener: OnFragmentInteractionListener? = null
    private var addToRecommendRv : AddToRecommendRv? = null
    private var cal: Calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendSelectBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

        // 클릭 이벤트 설정
        binding.menuRecommSelectBtnCl.setOnClickListener {
            // 클릭 시 WhatMenuFragment로 교체
            val whatMenuFragment = MenuRecommendWhatMenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, whatMenuFragment)
                .commit()
        }
        binding.menuRecommSelectBtnSeeCl.setOnClickListener {
//            listener?.onExitRequested()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
        }

        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val recommendMenu = sharedPref.getString("recommendMenu", null)
        val recommendMenuSacc = sharedPref.getInt("recommendMenuSacc", 0)
        val recommendMenuProtein = sharedPref.getInt("recommendMenuProtein", 0)
        val recommendMenuFat = sharedPref.getInt("recommendMenuFat", 0)
        menuRecommendFragment?.addWhatMenuFragmentItems(
            MenuRecommItem.UserItem("이 음식", "으로 먹을래요", 2),
            MenuRecommItem.SystemUpdateMenuItem(recommendMenu!!,7)
        )
        menuRecommendFragment?.addItemToRecyclerView(RecommendMenu(cal,"첫 끼 : " + recommendMenu,recommendMenuSacc,recommendMenuProtein,recommendMenuFat))
        //addItemToActivityRecyclerView(RecommendMenu(cal,"첫 끼 : " + recommendMenu,recommendMenuSacc,recommendMenuProtein,recommendMenuFat))

        // 다른 초기화 작업 등을 수행할 수 있음
    }

    //Activity 종료 관장 함수들
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
//        if (context is AddToRecommendRv) {
//            addToRecommendRv = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//    private fun addItemToActivityRecyclerView(item: RecommendMenu) {
//        addToRecommendRv?.addItemToRecyclerView(item)
//    }
}