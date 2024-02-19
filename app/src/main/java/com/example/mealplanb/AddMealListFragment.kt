package com.example.mealplanb

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentAddMealListBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.Food
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jp.wasabeef.blurry.Blurry
import java.lang.reflect.Array

class AddMealListFragment : Fragment() {
    lateinit var binding : FragmentAddMealListBinding
    lateinit var mealList : ArrayList<Meal>
    private var addFoodList : ArrayList<Meal> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMealListBinding.inflate(layoutInflater)
        mealList = arrayListOf()

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE)
        val gson = Gson()
        val selectedMealNum = sharedPreferences.getInt("selectedMealNum",1)
        val foodListID = "addFoodList" + selectedMealNum.toString()
        var json = sharedPreferences.getString(foodListID,null)
        var foodListEmptyFlag : Boolean
        addFoodList = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()
        if(addFoodList.size > 0) {
            foodListEmptyFlag = false
        } else {
            foodListEmptyFlag = true
        }

        for(item in addFoodList) {
            mealList.add(
                Meal(
                    item.meal_name,
                    item.meal_weight.toDouble(),
                    item.meal_cal.toDouble(),
                    item.sacc_gram.toDouble(),
                    item.protein_gram.toDouble(),
                    item.fat_gram.toDouble()
                )
            )
        }

        binding.meallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.meallistRv.adapter = AddMealListAdapter(mealList)

        //세트 저장 버튼을 눌렀을 때
        binding.addmeallistSetSaveLl.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val sheetView = layoutInflater.inflate(R.layout.ui_set_save,null)
            val layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                283.dpToPx() // dp를 pixel로 변환
            )

            layoutParams.behavior = BottomSheetBehavior<ConstraintLayout>().apply {
                peekHeight = 283.dpToPx()
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

            sheetView.layoutParams = layoutParams

            //닫는 버튼 누르면 닫기
            val changeAvatarCancel : ImageView = sheetView.findViewById(R.id.set_save_cancel)
            changeAvatarCancel.setOnClickListener{
                bottomSheetDialog.dismiss()
            }

            //완료 버튼 색깔변경
            val setsaveCompleteCv : CardView = sheetView.findViewById(R.id.set_save_complete_cv)

            val nameEt : EditText = sheetView.findViewById(R.id.set_save_name_et)

            nameEt.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    true
                } else {
                    false
                }
            }

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (nameEt.text.isNotEmpty()) {
                        setsaveCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                        setsaveCompleteCv.setOnClickListener{ // 완료 버튼 눌렀을 때
                            //나의 식단 저장하는 코드
                            json = sharedPreferences.getString("myMadeList", null)

                            val myMadeList: ArrayList<Meal> = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()

                            //음식별로 food_id 저장하는 내용이 필요 -> Meal class 수정 필요
                            var totalWeight = 0.0
                            var totalCal = 0.0
                            var totalCarb = 0.0
                            var totalProtein = 0.0
                            var totalFat = 0.0

                            for (meal in mealList) {
                                totalWeight += meal.meal_weight
                                totalCal += meal.meal_cal
                                totalCarb += meal.sacc_gram
                                totalProtein += meal.protein_gram
                                totalFat += meal.fat_gram

                            }

                            val mealName = nameEt.text.toString().trim { it <= '\n' }
                            myMadeList.add(Meal(mealName, totalWeight, totalCal, totalCarb, totalProtein, totalFat))

                            val editor = sharedPreferences.edit()
                            val newJson = gson.toJson(myMadeList)
                            editor.putString("myMadeList", newJson)
                            editor.apply()

                            bottomSheetDialog.dismiss()

                            //API관련
                            val authService = AuthService(requireContext())
                        }
                    } else {
                        setsaveCompleteCv.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            }

            nameEt.addTextChangedListener(textWatcher)

            bottomSheetDialog.setContentView(sheetView)


            // BottomSheetDialog가 보여질 때 블러 처리 적용
            bottomSheetDialog.setOnShowListener {
                binding.grayBlurFl.visibility = View.VISIBLE
            }

            // BottomSheetDialog가 사라질 때 블러 처리 제거
            bottomSheetDialog.setOnDismissListener {
                binding.grayBlurFl.visibility = View.GONE
            }

            bottomSheetDialog.show()

        }

        binding.addmeallistAddmoreCv.setOnClickListener {
            val editor = sharedPreferences.edit()
            val newJson = gson.toJson(addFoodList)
            editor.putString(foodListID,newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
        }
        binding.addmeallistEndCv.setOnClickListener {
            var tot_cal = 0.0

            for(i in mealList) {
                tot_cal += i.meal_cal
            }

            val editor = sharedPreferences.edit()

            // 이미지 리스트를 생성하고 랜덤하게 하나를 선택
            val imageList = listOf(R.drawable.item_hamburger_img,
                R.drawable.item_salad_img,
                R.drawable.item_rice_img,
                R.drawable.item_meal_img,
                R.drawable.item_egg_img,
                R.drawable.item_pudding_img,
                R.drawable.item_sugar_img)
            val randomImage = imageList.random()

//            val json = MealMainInfo(true, 1, tot_cal, randomImage)
//            val newJson = gson.toJson(json)
//            editor.putString("MealMainInfo", newJson)
//            editor.apply()

            //home 화면에 표시될 끼니 정보 갱신
            var json = sharedPreferences.getString("dayMealList",null)
            var dayMealList = gson.fromJson(json,object : TypeToken<ArrayList<MealMainInfo>>() {}.type) ?: arrayListOf(
                MealMainInfo(false,1,0.0,0, "", 0.0)
            )
            dayMealList.set(selectedMealNum-1, MealMainInfo(true,selectedMealNum,tot_cal,randomImage,"", 0.0))
            val newJson = gson.toJson(dayMealList)
            editor.putString("dayMealList",newJson)
            editor.apply()

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
        }
        binding.addmeallistBackmenuCl.setOnClickListener {
            if(foodListEmptyFlag) {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
            } else {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
            }
        }

        return binding.root
    }
    //dp를 pixel로 변환
    fun Int.dpToPx(): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density).toInt()
    }
}