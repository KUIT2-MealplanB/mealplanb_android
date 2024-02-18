package com.example.mealplanb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat.getSystemService
import com.example.mealplanb.databinding.FragmentGoalMotifBinding
import com.example.mealplanb.databinding.FragmentMenuRecommendHowMenuBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.PlanView


class GoalMotifFragment : Fragment(), PlanView {
    lateinit var binding: FragmentGoalMotifBinding
    private var start_weight: Double = 50.5
    private var goal_weight: Double = 45.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalMotifBinding.inflate(layoutInflater)

        // startWeight와 wantWeight 가져오기
        val sharedPref = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        val startWeight = sharedPref?.getFloat("startWeight", 0.0f)
        val wantWeight = sharedPref?.getFloat("wantWeight", 0.0f)

//        binding.goalMotifStartWeightEt.hint = "$startWeight"
//        binding.goalMotifWantWeightEt.hint = "$wantWeight"

        //et 포커스 이동
//        binding.goalMotifStartWeightEt.setOnEditorActionListener { v, actionId, _ ->
//            if(actionId == EditorInfo.IME_ACTION_DONE){
//                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(v.windowToken, 0)
//
//                //goalMotifStartWeightEt를 변수에 저장
//                goal_weight = binding.goalMotifStartWeightEt.toString().toDoubleOrNull() ?: 0.0
//
//                return@setOnEditorActionListener true
//            }
//            false
//        }
//
//        binding.goalMotifWantWeightEt.setOnEditorActionListener { v, actionId, _ ->
//            if(actionId == EditorInfo.IME_ACTION_DONE){
//                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(v.windowToken, 0)
//
//                //goalMotifWantWeightEt를 변수에 저장
//                start_weight = binding.goalMotifWantWeightEt.toString().toDoubleOrNull() ?: 0.0
//
//                return@setOnEditorActionListener true
//            }
//            false
//        }

        //spinner 구현
        val diets = arrayOf("일반 식단","운동 식단","키토 식단","비건 식단","당뇨 식단")
        val spinner: Spinner = binding.goalMotifDietSpinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, diets)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 카테고리 번호 가져오기
//        val categoryNumber = sharedPref?.getInt("selectedCategory", 0)
//        if (categoryNumber != null && categoryNumber != 0) {
//            spinner.setSelection(categoryNumber-1)  // 카테고리 번호를 스피너의 초기값으로 설정
//        }

        //API 연동
        val authService = AuthService(requireContext())
        authService.setPlanView(this)
        authService.plancheck()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDiet = parent.getItemAtPosition(position).toString()
                // 선택된 값에 대한 처리
                var diet_type = ""
                when(selectedDiet) {
                    "일반 식단" -> diet_type = "일반"
                    "운동 식단" -> diet_type = "운동"
                    "키토 식단" -> diet_type = "키토"
                    "비건 식단" -> diet_type = "비건"
                    else -> diet_type = "당뇨"
                }
                Log.d("selectedDiet",diet_type)

                authService.planDietTypeCheck(diet_type)

                // 변경된 카테고리 번호를 SharedPreferences에 저장
                val sharedPref = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putInt("selectedCategory", position+1)
                editor?.putString("selectedDiet", selectedDiet)
                editor?.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 해제될 때의 처리
            }
        }


        //목표 수정 버튼
        binding.goalMotifCompleteCv.setOnClickListener {

            // startWeightEt와 wantWeightEt의 값을 읽어서 SharedPreference에 저장
            val startWeightInput = binding.goalMotifStartWeightEt.text.toString().toFloatOrNull()
            val wantWeightInput = binding.goalMotifWantWeightEt.text.toString().toFloatOrNull()
            val diffWeight = if (startWeightInput != null && wantWeightInput != null) {
                startWeightInput.toFloat() - wantWeightInput.toFloat()
            } else {
                // Handle the case where either startWeightInput or wantWeightInput is null
                0.0f // or any default value you prefer
            }

            val sharedPref = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()

            //saveToSharedPreferences()

            val initial_weight = binding.goalMotifStartWeightEt.text.toString().toDouble()
            val target_weight = binding.goalMotifWantWeightEt.text.toString().toDouble()
            val carbohydrate_rate = binding.goalMotifCarboEt.text.toString().toInt()
            val protein_rate = binding.goalMotifProteinEt.text.toString().toInt()
            val fat_rate = binding.goalMotifFatEt.text.toString().toInt()
            val target_kcal = binding.goalMotifCaloriesEt.text.toString().toInt()
            authService.planupdate(initial_weight,target_weight,carbohydrate_rate,protein_rate,fat_rate,target_kcal)

            if (startWeightInput != null) {
                editor?.putFloat("startWeight", startWeightInput)
            }

            if (wantWeightInput != null) {
                editor?.putFloat("wantWeight", wantWeightInput)
            }

            if (diffWeight != null) {
                editor?.putFloat("diffWeight", diffWeight)
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

        //뒤로 가기 버튼
        binding.goalMotifCancelIv.setOnClickListener {
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

    override fun PlanCheckSuccess(
        initial_weight: Double,
        target_weight: Double,
        recommended_kcal: Int,
        diet_type: String,
        carbohydrate_rate: Int,
        protein_rate: Int,
        fat_rate: Int,
        target_kcal: Int
    ) {
        var selectNum: Int = 0
        when(diet_type) {
            "일반" -> selectNum = 1
            "운동" -> selectNum = 2
            "키토" -> selectNum = 3
            "비건" -> selectNum = 4
            else -> selectNum = 5
        }

        binding.goalMotifStartWeightEt.hint = initial_weight.toString()
        binding.goalMotifWantWeightEt.hint = target_weight.toString()
        binding.goalMotifRecommendCalTv.text = recommended_kcal.toString()
        binding.goalMotifDietSpinner.setSelection(selectNum)
        binding.goalMotifCarboEt.hint = carbohydrate_rate.toString()
        binding.goalMotifProteinEt.hint = protein_rate.toString()
        binding.goalMotifFatEt.hint = fat_rate.toString()
        binding.goalMotifCaloriesEt.hint = target_kcal.toString()
    }

    override fun PlanUpdateSuccess(
        initial_weight: Double,
        target_weight: Double,
        recommended_kcal: Int,
        diet_type: String,
        carbohydrate_rate: Int,
        protein_rate: Int,
        fat_rate: Int,
        target_kcal: Int
    ) {
        var selectNum: Int = 0
        when(diet_type) {
            "일반" -> selectNum = 1
            "운동" -> selectNum = 2
            "키토" -> selectNum = 3
            "비건" -> selectNum = 4
            else -> selectNum = 5
        }

        binding.goalMotifStartWeightEt.hint = initial_weight.toString()
        binding.goalMotifWantWeightEt.hint = target_weight.toString()
        binding.goalMotifRecommendCalTv.text = recommended_kcal.toString()
        binding.goalMotifDietSpinner.setSelection(selectNum)
        binding.goalMotifCarboEt.hint = carbohydrate_rate.toString()
        binding.goalMotifProteinEt.hint = protein_rate.toString()
        binding.goalMotifFatEt.hint = fat_rate.toString()
        binding.goalMotifCaloriesEt.hint = target_kcal.toString()
    }

    override fun PlanDietTypeCheckSuccess(
        diet_type: String,
        carbohydrate_rate: Int,
        protein_rate: Int,
        fat_rate: Int
    ) {
        binding.goalMotifCarboEt.hint = carbohydrate_rate.toString()
        binding.goalMotifProteinEt.hint = protein_rate.toString()
        binding.goalMotifFatEt.hint = fat_rate.toString()
    }

//    // SharedPreferences에 값을 저장하는 함수
//    private fun saveToSharedPreferences() {
//        val key = "diff_weight"
//        val value = start_weight - goal_weight
//        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        editor.putString(key, value.toString())
//        editor.apply()
//    }
}