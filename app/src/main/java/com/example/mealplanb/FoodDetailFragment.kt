package com.example.mealplanb

import MyMealAdapter
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mealplanb.databinding.FragmentFooddetailBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.FoodSearchResponse
import com.example.mealplanb.remote.MealFoodResponseFoodList
import com.example.mealplanb.remote.SearchFoodView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FoodDetailFragment : Fragment(), SearchFoodView {
    private lateinit var binding: FragmentFooddetailBinding
    private var meal: Meal = Meal("null",0.0,0.0,0.0,0.0,0.0)
    private lateinit var food: FoodItem
    private var addFoodList : ArrayList<Meal> = arrayListOf()
    private var addFoodListOften : ArrayList<Meal> = arrayListOf()

    // Initial values for food properties
    private var originSacc: Double = 50.0
    private var originProtein: Double = 15.0
    private var originFat: Double = 35.0
    private var originkcal: Double = 362.0
    private val foodName: String = "로제파스타"

    lateinit var oftenadapter: MyMealAdapter // 즐겨찾기 RecyclerView에 사용할 어댑터

    override fun onResume() {
        super.onResume()

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE)
        val foodId = sharedPreferences.getInt("selectedFoodId", -1)

        val authService = AuthService(requireContext()) // AuthService 인스턴스 생성
        authService.setSearchFoodView(this)
        authService.checkFoodDetail(foodId)

        // 음식 정보를 SharedPreferences에서 불러오기
        val originMealWeight = sharedPreferences.getString("foodQuantity", "0")?.toDoubleOrNull() ?: 0.0

        var gson = Gson()

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

            var newAddMeal = MealFoodResponseFoodList(foodId,meal.meal_weight.toInt(),meal.meal_name,meal.meal_cal.toDouble())
            newJson = gson.toJson(newAddMeal)
            editor.putString("nowAddMeal",newJson)
            Log.d("nowAddMeal",newAddMeal.toString())
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

        // 즐겨찾기 상태를 나타내는 변수
        var isFavorite = false

        binding.detailFoodFavoriteIv.setOnClickListener {
            // 클릭할 때마다 즐겨찾기 상태를 토글
            isFavorite = !isFavorite

            //API관련
            val authService = AuthService(requireContext())
            authService.setSearchFoodView(this)


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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFooddetailBinding.inflate(layoutInflater)
        Log.d("fooddetailfragment 확인","success")

        val authService = AuthService(requireContext())

        //X눌렀을 때
        binding.detailFoodCancelIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
            requireActivity().overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_top,androidx.appcompat.R.anim.abc_slide_out_bottom)
        }

        // 즐겨찾기 상태를 나타내는 변수
        var isFavorite = false

        binding.detailFoodFavoriteIv.setOnClickListener {
            // 클릭할 때마다 즐겨찾기 상태를 토글
            isFavorite = !isFavorite

            //API관련
            authService.setSearchFoodView(this)


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
    override fun SearchFoodSuccess(foodSearchResponse: FoodSearchResponse) {
        TODO("Not yet implemented")
    }

    override fun FoodDetailSuccess(
        name: String,
        quantity: Int,
        kcal: Double,
        carbohydrate: Double,
        protein: Double,
        fat: Double,
        isFavorite: Boolean
    ) {
        meal.meal_name = name
        meal.meal_weight = quantity.toDouble()
        meal.meal_cal = kcal
        meal.sacc_gram = carbohydrate
        meal.protein_gram = protein
        meal.fat_gram = fat

        originkcal = kcal
        originSacc = carbohydrate
        originProtein = protein
        originFat = fat

        binding.detailFoodNameTv.text = name
        binding.detailFoodMealWeightEt.setText(quantity.toString())
        binding.detailFoodKcalNumTv.text = kcal.toString()
        binding.detailFoodSaccSizeTv.text = carbohydrate.toString()
        binding.detailFoodProteinSizeTv.text = protein.toString()
        binding.detailFoodFatSizeTv.text = fat.toString()
        if(isFavorite) {
            binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_full_ic)
        }else {
            binding.detailFoodFavoriteIv.setImageResource(R.drawable.star_ic)
        }
    }

}