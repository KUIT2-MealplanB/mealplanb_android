package com.example.mealplanb

import SearchCategoryAdapter
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mealplanb.databinding.FragmentFooddetailBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.FavoriteFoodResponse
import com.example.mealplanb.remote.SignupView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FoodDetailFragment : Fragment(), SignupView {
    private lateinit var binding: FragmentFooddetailBinding
    private lateinit var meal: Meal
    private lateinit var food: FoodItem
    private var addFoodList : ArrayList<Meal> = arrayListOf()
    private var addFoodListOften : ArrayList<Meal> = arrayListOf()

    // Initial values for food properties
    private val originSacc: Double = 50.0
    private val originProtein: Double = 15.0
    private val originFat: Double = 35.0
    private val originkcal: Double = 362.0
    private val foodName: String = "로제파스타"

    lateinit var oftenadapter: SearchCategoryAdapter // 즐겨찾기 RecyclerView에 사용할 어댑터

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFooddetailBinding.inflate(layoutInflater)

        // Replace this with the actual food name
        //val foodName = "로제파스타"
        // Initialize food details using the data class
        food = FoodItem(foodName, originSacc, originProtein, originFat, originkcal)

        // Set initial UI values based on food details
        binding.detailFoodNameTv.text = food.name
        binding.detailFoodSaccSizeTv.text = food.saccSize.toInt().toString()
        binding.detailFoodProteinSizeTv.text = food.proteinSize.toInt().toString()
        binding.detailFoodFatSizeTv.text = food.fatSize.toInt().toString()
        binding.detailFoodKcalNumTv.text = food.kcal.toInt().toString()

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE)
        var gson = Gson()
        var json = sharedPreferences.getString("Key", null)
        val data1 = gson.fromJson(json, object : TypeToken<Meal>() {}.type) ?: Meal("null",0.0,0.0, 0.0,0.0,0.0)

//        if(intent.getSerializableExtra("addFoodListOften") as? ArrayList<Meal> != null) {
//            addFoodListOften = (intent.getSerializableExtra("addFoodListOften") as? ArrayList<Meal>)!!
//        }
        binding.detailFoodNameTv.text = data1.meal_name
        binding.detailFoodSaccSizeTv.text = data1.sacc_gram.toInt().toString()
        binding.detailFoodProteinSizeTv.text = data1.protein_gram.toInt().toString()
        binding.detailFoodFatSizeTv.text = data1.fat_gram.toInt().toString()
        binding.detailFoodKcalNumTv.text = data1.meal_cal.toInt().toString()
        binding.detailFoodMealWeightEt.setText(data1.meal_weight.toInt().toString())

        // Initial values for food properties
        val originSacc =  data1.sacc_gram
        val originMealWeight = data1.meal_weight
        val originProtein = data1.protein_gram
        val originFat = data1.fat_gram
        val originkcal = data1.meal_cal
        val foodName = data1.meal_name

        // Initialize food details using the data class
        meal = Meal(foodName, originMealWeight,originkcal, originSacc, originProtein, originFat)

        // Minus button click listener
        binding.detailFoodMinusTv.setOnClickListener {
            if(meal.meal_weight > 0){
                meal.meal_weight -= originMealWeight
                meal.sacc_gram -= originSacc
                meal.protein_gram -= originProtein
                meal.fat_gram -= originFat
                meal.meal_cal -= originkcal

                // Update UI with new values
                updateUI(meal.meal_weight)
            }else {
                Toast.makeText(requireContext(), "0보다 작을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.detailFoodAddCv.setOnClickListener {
            val selectedMealNum = sharedPreferences.getInt("selectedMealNum",1)
            val foodListID = "addFoodList" + selectedMealNum.toString()
            var json = sharedPreferences.getString(foodListID, null)
            val addFoodList = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf<Meal>()
            var checkItemFlag = false
            for(item in addFoodList) {
                if(item.meal_name == meal.meal_name) {
                    checkItemFlag = true
                    item.meal_weight += meal.meal_weight
                    item.meal_cal += meal.meal_cal
                    item.sacc_gram += meal.sacc_gram
                    item.protein_gram += meal.protein_gram
                    item.fat_gram += meal.fat_gram
                }
            }
            if(!checkItemFlag) {
                addFoodList.add(meal)
            }
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(addFoodList)
            editor.putString(foodListID,newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, AddMealListFragment()).commit()
        }

        // Plus button click listener
        binding.detailFoodPlusTv.setOnClickListener {
            // Update food properties when increasing quantity
            meal.meal_weight += originMealWeight
            meal.sacc_gram += originSacc
            meal.protein_gram += originProtein
            meal.fat_gram += originFat
            meal.meal_cal += originkcal

            // Update UI with new values
            updateUI(meal.meal_weight)
        }

        //X눌렀을 때
        binding.detailFoodCancelIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
            requireActivity().overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_top,androidx.appcompat.R.anim.abc_slide_out_bottom)
        }

        // favoriteIv를 클릭했을 때 SharedPreferences를 통해 데이터 저장
/*            binding.detailFoodFavoriteIv.setOnClickListener {
            //말풍선 "즐겨찾기에 추가되었어요"
            binding.detailFoodFavoriteCv.visibility = View.VISIBLE
            var json = sharedPreferences.getString("addFoodListOften", null)
            addFoodListOften = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf<Meal>()

            // 이미 추가된 경우 중복 방지
            if (!addFoodListOften.contains(meal)) {
                addFoodListOften.add(meal)
                binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_full_ic)

                // SharedPreferences에서 기존 데이터 불러오기
                json = sharedPreferences.getString("oftenFoodList", null)

                // 기존 데이터가 있다면 불러와서 ArrayList로 변환
                var oftenFoodList: ArrayList<Meal> = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()

                // 중복 체크
                if (!oftenFoodList.contains(meal)) {
                    // 새로운 데이터를 oftenFoodList에 추가
                    oftenFoodList.add(meal)

                    // SharedPreferences에 새로운 리스트를 다시 저장
                    val editor = sharedPreferences.edit()
                    val newJson = gson.toJson(oftenFoodList)
                    editor.putString("oftenFoodList", newJson)
                    editor.apply()

                } else {
                    // 이미 즐겨찾기된 경우 Toast 메시지 표시
                    Toast.makeText(requireContext(), "이미 즐겨찾기 되어있습니다.", Toast.LENGTH_SHORT).show()
                    binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_full_ic)
                }
            } else {
                // 이미 추가된 경우 Toast 메시지 표시
                Toast.makeText(requireContext(), "이미 즐겨찾기 되어있습니다.", Toast.LENGTH_SHORT).show()
                binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_full_ic)
            }
        }*/

        // 즐겨찾기 상태를 나타내는 변수
        var isFavorite = false

        binding.detailFoodFavoriteIv.setOnClickListener {
            // 클릭할 때마다 즐겨찾기 상태를 토글
            isFavorite = !isFavorite

            //API관련
            val authService = AuthService(requireContext())
            authService.setSignupView(this)


            // 즐겨찾기 상태에 따라 이미지 변경 또는 다른 작업 수행
            if (isFavorite) {
                // 즐겨찾기 등록 로직
                binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_full_ic)
                authService.favoriteFoodPost(3)
            } else {
                // 즐겨찾기 해제 로직
                binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_ic)
                authService.favoriteFoodPatch(3)
            }

        }


        binding.detailFoodMealWeightEt.setOnLongClickListener {
            // Enable editing by focusing and showing the keyboard
            binding.detailFoodMealWeightEt.isFocusableInTouchMode = true
            binding.detailFoodMealWeightEt.requestFocus()

            // Show the keyboard
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.detailFoodMealWeightEt, InputMethodManager.SHOW_IMPLICIT)
            true

        }


        // Editor action listener for handling completion
        binding.detailFoodMealWeightEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val enteredValue = binding.detailFoodMealWeightEt.text.toString()

                // Hide the keyboard
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.detailFoodMealWeightEt.windowToken, 0)
                true

                // Update food properties when increasing quantity
                meal.meal_weight = enteredValue.toString().toDouble()
                meal.sacc_gram = String.format("%.2f", originSacc / 100.0 * enteredValue.toDouble()).toDouble()
                meal.protein_gram = String.format("%.2f", originProtein / 100.0 * enteredValue.toDouble()).toDouble()
                meal.fat_gram = String.format("%.2f", originFat / 100.0 * enteredValue.toDouble()).toDouble()
                meal.meal_cal = String.format("%.2f", originkcal / 100.0 * enteredValue.toDouble()).toDouble()

                // Update UI with new values
                updateUI(meal.meal_weight)

                //Toast.makeText(this,enteredValue, Toast.LENGTH_SHORT).show()
                true // Return true to consume the action
            } else {
                false // Return false if the action is not consumed
            }
        }
        return binding.root
    }

    // Function to update the UI with new values
    private fun updateUI(currentValue: Double) {
        binding.detailFoodSaccSizeTv.text = meal.sacc_gram.toInt().toString()
        binding.detailFoodProteinSizeTv.text = meal.protein_gram.toInt().toString()
        binding.detailFoodFatSizeTv.text = meal.fat_gram.toInt().toString()
        binding.detailFoodKcalNumTv.text = meal.meal_cal.toInt().toString()
        binding.detailFoodMealWeightEt.setText(meal.meal_weight.toInt().toString())
    }

    override fun SignupLoading() {
        TODO("Not yet implemented")
    }

    override fun SignupSuccess() {
        TODO("Not yet implemented")
    }

    override fun SignupFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun WeightcheckSuccess(weight: Float, date: String) {
        TODO("Not yet implemented")
    }

    override fun handleFavoriteFoodResponse(favoriteFoodResponse: FavoriteFoodResponse?) {
        TODO("Not yet implemented")
    }
}