package com.example.mealplanb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.mealplanb.databinding.ActivityRecommendCategoryBinding

class RecommendCategoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRecommendCategoryBinding

    private var selectedCategory: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryClickListener = View.OnClickListener { view ->
            if (selectedCategory == view) {
                deselectCategory()
            } else {
                selectCategory(view)
            }
        }

        binding.recommendCategory1Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory2Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory3Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory4Cv.setOnClickListener(categoryClickListener)
        binding.recommendCategory5Cv.setOnClickListener(categoryClickListener)

        //다음 버튼
        binding.privacyCompleteCv.setOnClickListener {
            if (selectedCategory != null) {
                val intent = Intent(this, StartAvatarActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "1개 이상의 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        //뒤로가기
        binding.recommendBackIv.setOnClickListener {
            finish()
        }
    }

    //선택
    private fun selectCategory(view: View) {
        (selectedCategory as? CardView)?.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        selectedCategory = view
        (view as? CardView)?.setCardBackgroundColor(Color.parseColor("#8D8D8D"))
        binding.privacyCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
    }

    //취소
    private fun deselectCategory() {
        (selectedCategory as? CardView)?.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        selectedCategory = null
        binding.privacyCompleteCv.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
    }

}