package com.example.mealplanb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.mealplanb.databinding.FragmentGoalMotifBinding
import com.example.mealplanb.databinding.FragmentMenuRecommendHowMenuBinding

class GoalMotifFragment : Fragment() {
    lateinit var binding: FragmentGoalMotifBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalMotifBinding.inflate(layoutInflater)

        // startWeight와 wantWeight 가져오기
        val sharedPref = activity?.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        val startWeight = sharedPref?.getFloat("startWeight", 0.0f)
        val wantWeight = sharedPref?.getFloat("wantWeight", 0.0f)

        binding.goalMotifStartWeightEt.hint = "$startWeight"
        binding.goalMotifWantWeightEt.hint = "$wantWeight"

        //spinner 구현
        val diets = arrayOf("일반 식단","운동 식단","키토 식단","비건 식단","당뇨 식단")
        val spinner: Spinner = binding.goalMotifDietSpinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, diets)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 카테고리 번호 가져오기
        val categoryNumber = sharedPref?.getInt("selectedCategory", 0)
        if (categoryNumber != null && categoryNumber != 0) {
            spinner.setSelection(categoryNumber-1)  // 카테고리 번호를 스피너의 초기값으로 설정
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDiet = parent.getItemAtPosition(position).toString()
                // 선택된 값에 대한 처리

                // 변경된 카테고리 번호를 SharedPreferences에 저장
                val sharedPref = activity?.getSharedPreferences("myPref", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putInt("selectedCategory", position+1)
                editor?.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리
            }
        }


        //뒤로가기 버튼
        binding.goalMotifCancelIv.setOnClickListener {

            // startWeightEt와 wantWeightEt의 값을 읽어서 SharedPreference에 저장
            val startWeightInput = binding.goalMotifStartWeightEt.text.toString().toFloatOrNull()
            val wantWeightInput = binding.goalMotifWantWeightEt.text.toString().toFloatOrNull()

            val sharedPref = activity?.getSharedPreferences("myPref", Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()

            if (startWeightInput != null) {
                editor?.putFloat("startWeight", startWeightInput)
            }

            if (wantWeightInput != null) {
                editor?.putFloat("wantWeight", wantWeightInput)
            }

            editor?.apply()

            val source = activity?.intent?.getStringExtra("source") ?: arguments?.getString("source")

            when (source) {
                "StatFragment" -> {
                    // StatFragment로 이동하는 코드
                    val anotherFragment = ChartWeightFragment()
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.main_frm, anotherFragment)
                    fragmentTransaction?.commit()
                }
                "HiddenPageActivity" -> {
                    // HiddenPageActivity로 이동하는 코드
                    activity?.finish()
                }
            }

            Shared.source = "" // 초기화
        }

        return binding.root
    }
}