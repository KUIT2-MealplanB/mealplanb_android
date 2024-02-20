package com.example.mealplanb

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.mealplanb.databinding.FragmentHomeWeightInputBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson


class HomeWeightInputFragment : BottomSheetDialogFragment() {

    private var weight : Float? = null
    lateinit var binding: FragmentHomeWeightInputBinding
    var weightUpdateListener: WeightUpdateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home_weight_input, null)
        bottomSheetDialog.setContentView(view)

        // 배경 어둡게 설정
        val backgroundView = bottomSheetDialog.window?.decorView?.findViewById<View>(android.R.id.content)
        backgroundView?.setBackgroundColor(Color.parseColor("#80000000")) // 검정색, 투명도 50%

        // binding 초기화
        binding = FragmentHomeWeightInputBinding.bind(view)

        //칼로리, 탄, 단, 지가 입력되면 색이 바뀌는 버튼
        val submitButton = binding.homeWeightInputAddBtnCv

        // 입력되어야 하는 각각의 칼로리, 탄, 단, 지 EditText에 대한 TextWatcher 설정
        val editTextList = listOf(
            binding.homeWeightInputWeightEt
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

                        binding.homeWeightInputAddBtnCv.setOnClickListener {

                            // Update weight variable
                            weight = s?.toString()?.toFloatOrNull()

                            val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            // SharedPreferences.Editor 객체를 얻어서 값을 저장
                            val editor = sharedPref.edit()
                            editor.putFloat("TodayWeight", weight ?: 0.0f)
                            editor.apply()

                            weightUpdateListener?.updateWeightOnUI(weight ?: 0.0f)

                            dismiss()

                        }
                    } else {
                        submitButton.setBackgroundResource(R.drawable.free_input_btn_disabled) // Change to the original color
                    }
                }
            })
        }


        // 닫는 버튼 누르면 닫기
        binding.homeWeightInputCancleIv.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        return bottomSheetDialog
    }

}