package com.example.mealplanb

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mealplanb.databinding.ActivityFooddetailBinding


class FoodDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFooddetailBinding
    private lateinit var food: FoodItem

    // Initial values for food properties
    private val originSacc: Int = 500
    private val originProtein: Int = 100
    private val originFat: Int = 200
    private val originkcal: Int = 362
    private val foodName: String = "로제파스타"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFooddetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Replace this with the actual food name
        //val foodName = "로제파스타"
        // Initialize food details using the data class
        food = FoodItem(foodName, originSacc, originProtein, originFat, originkcal)

        // Set initial UI values based on food details
        binding.foodNameTv.text = food.name.toString()
        binding.saccSizeTv.text = food.saccSize.toString()
        binding.proteinSizeTv.text = food.proteinSize.toString()
        binding.fatSizeTv.text = food.fatSize.toString()
        binding.foodKcalNumTv.text = food.kcal.toString()

        val data= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Key",Meal::class.java)
        } else {
            intent.getSerializableExtra("Key") as Meal
        } ?: Meal("null",0,0)

        binding.foodNameTv.text = data.meal_name
//        binding.saccSizeTv.text = food.saccSize.toString()
//        binding.proteinSizeTv.text = food.proteinSize.toString()
//        binding.fatSizeTv.text = food.fatSize.toString()
        binding.foodKcalNumTv.text = data.meal_cal.toString()

        // Minus button click listener
        binding.minusTv.setOnClickListener {
            val text = binding.gramTv.text.toString().trim()
            val numericText = text.filter { it.isDigit() }

            if (numericText.isNotEmpty()) {
                var currentValue = numericText.toInt()

                if (currentValue >= 100) {
                    // Update food properties when reducing quantity
                    currentValue -= 100
                    food.saccSize -= originSacc
                    food.proteinSize -= originProtein
                    food.fatSize -= originFat
                    food.kcal -= originkcal

                    // Update UI with new values
                    updateUI(currentValue)
                } else {
                    Toast.makeText(this, "0보다 작을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.addCv.setOnClickListener {
            val intent = Intent(applicationContext,AddMealListActivity::class.java)
            startActivity(intent)
        }

        binding.detailFoodCancelIv.setOnClickListener {
            finish()
        }

        // Plus button click listener
        binding.plusTv.setOnClickListener {
            val text = binding.gramTv.text.toString().trim()
            val numericText = text.filter { it.isDigit() }

            if (numericText.isNotEmpty()) {
                var currentValue = numericText.toInt()
                currentValue += 100

                // Update food properties when increasing quantity
                food.saccSize += originSacc
                food.proteinSize += originProtein
                food.fatSize += originFat
                food.kcal += originkcal

                // Update UI with new values
                updateUI(currentValue)
            }
        }
    }

    // Function to update the UI with new values
    private fun updateUI(currentValue: Int) {
        binding.saccSizeTv.text = food.saccSize.toString()
        binding.proteinSizeTv.text = food.proteinSize.toString()
        binding.fatSizeTv.text = food.fatSize.toString()
        binding.foodKcalNumTv.text = food.kcal.toString()
        binding.gramTv.text = "$currentValue g"
    }
}