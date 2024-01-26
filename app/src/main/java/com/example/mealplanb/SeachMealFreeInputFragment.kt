package com.example.mealplanb

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import com.example.mealplanb.databinding.FragmentSeachMealFreeInputBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson


class SeachMealFreeInputFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentSeachMealFreeInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_seach_meal_free_input, null)
        bottomSheetDialog.setContentView(view)

        // BottomSheetDialog의 Peek height를 설정 (원하는 높이로 조절)
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        bottomSheetDialog.behavior.peekHeight = screenHeight / 1  // 예시로 화면 높이의 반만큼 설정

        // 배경 어둡게 설정
        val backgroundView = bottomSheetDialog.window?.decorView?.findViewById<View>(android.R.id.content)
        backgroundView?.setBackgroundColor(Color.parseColor("#80000000")) // 검정색, 투명도 50%

        // binding 초기화
        binding = FragmentSeachMealFreeInputBinding.bind(view)

        //칼로리, 탄, 단, 지가 입력되면 색이 바뀌는 버튼
        val submitButton = binding.searchMealFreeInputAddBtnCv

        // 입력되어야 하는 각각의 칼로리, 탄, 단, 지 EditText에 대한 TextWatcher 설정
        val editTextList = listOf(
            binding.seachMealFreeInputFoodNameEt, binding.searchMealFreeInputKcalEt, binding.searchMealFreeInputSaccEt,
            binding.searchMealFreeInputProteinEt,binding.searchMealFreeInputFatEt
        )

        for (editText in editTextList) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    // Check if all EditTexts have non-empty text
                    val allEditTextsFilled = editTextList.all { it.text.isNotEmpty() }

                    // Change button color based on the condition
                    if (allEditTextsFilled) {
                        submitButton.setBackgroundResource(R.drawable.free_input_btn) // Change to the desired color

                        binding.searchMealFreeInputAddBtnCv.setOnClickListener {
                            //추가하기 버튼이 눌리면 실행

                            //자유 입력 식단에서 EditText로 받은 데이터들을 FoodDetailActivity로 넘기도록 변수 선언
                            val foodName = binding.seachMealFreeInputFoodNameEt.text.toString()
                            val sacc = binding.searchMealFreeInputSaccEt.text.toString()
                            val protein = binding.searchMealFreeInputProteinEt.text.toString()
                            val fat = binding.searchMealFreeInputFatEt.text.toString()
                            val kcal = binding.searchMealFreeInputKcalEt.text.toString()
                            //gram은 100을 기준으로 정함.
                            val gram = 100.0

                            // Meal 객체 생성
                            val meal = Meal(
                                meal_name = foodName,
                                meal_weight = gram,
                                meal_cal = kcal.toDouble(),
                                sacc_gram = sacc.toDouble(),
                                protein_gram = protein.toDouble(),
                                fat_gram = fat.toDouble()
                            )

                            // 데이터를 첨부
                            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
                            var gson = Gson()
                            val editor = sharedPreferences.edit()
                            val newJson = gson.toJson(meal)
                            editor.putString("Key",newJson)
                            editor.apply()

//                            childFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).addToBackStack(null).commit()

                            bottomSheetDialog.dismiss()
                            // 데이터를 첨부할 Intent 생성
//                            val intent = Intent(requireContext(), FoodDetailFragment::class.java)
                            // 데이터를 첨부
//                            intent.putExtra("Key", meal)  // yourMealObject는 전달하고자 하는 Meal 객체입니다.
                            // Intent를 통해 FoodDetailActivity를 시작
//                            startActivity(intent)

                        }
                    } else {
                        submitButton.setBackgroundResource(R.drawable.free_input_btn_disabled) // Change to the original color
                    }
                }
            })
        }



        // 닫는 버튼 누르면 닫기
        binding.seachMealFreeInputCancleIv.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        return bottomSheetDialog
    }

}