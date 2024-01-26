package com.example.mealplanb

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.ActivityNutrientdetailBinding

class NutrientdetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityNutrientdetailBinding

    override fun onResume() {
        super.onResume()

        // SharedPreferences 객체 생성
        val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences에서 값 가져오기
        val avatarImageID = sharedPref.getInt("avatar",3)
        val goalCal = sharedPref.getInt("goalCal",1000)
        val nowCal = sharedPref.getInt("nowCal",0)
        // 가져온 값 사용
        when(avatarImageID) {
            1 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }
        var progress = nowCal!! * 100 / goalCal!!
        binding.nutrientDetailProgressPb.updateProgress(progress)
        binding.nutrientDetailProgressPb.setText(nowCal.toString())
        binding.nutrientDetailTitleCalTv.text = (goalCal - nowCal).toString() + "kcal"

        setData()

        //progress 값 및 남은 칼로리 설정
//        goalCal = binding.mainProgressbgPb.getText()?.toInt()
//        nowCal = adapter!!.totCal()
//        var progress = nowCal!! * 100 / goalCal!!
//        binding.mainProgressPb.updateProgress(progress)
//        binding.mainProgressPb.setText(nowCal.toString())
//        binding.mainTitleCalTv.text = (goalCal!! - nowCal!!).toString() + "kcal"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNutrientdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()

        val nutrients = listOf(
            Nutrient("당", "0g"),
            Nutrient("대체 감미료", "0g"),
            Nutrient("식이섬유", "0g"),
            Nutrient("포화지방", "0g"),
            Nutrient("트랜스지방", "0g"),
            Nutrient("불포화지방", "0g"),
            Nutrient("콜레스테롤", "0g"),
            Nutrient("나트륨", "0g"),
            Nutrient("칼륨", "0g")
        )

        val adapter = NutrientAdapter(nutrients)
        binding.nutrientDetailNutRv.layoutManager = LinearLayoutManager(this)
        binding.nutrientDetailNutRv.adapter = adapter

        setData()

        //아바타 설정 변경하는 페이지 연결
        binding.nutrientDetailCharacterIv.setOnClickListener{
            val intent = Intent(applicationContext, AvatarMotifActivity::class.java)
            startActivity(intent)
        }

        binding.nutrientDetailBackIv.setOnClickListener{
            finish()
        }
    }

    fun setData() {
        // SharedPreferences 객체 생성
        val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences에서 값 가져오기
        val avatarImageID = sharedPref.getInt("avatar",3)
        val goalCal = sharedPref.getInt("goalCal",1000)
        val nowCal = sharedPref.getInt("nowCal",0)
        val saccDayTot = sharedPref.getInt("saccDayTot",1)
        val proteinDayTot = sharedPref.getInt("proteinDayTot",1)
        val fatDayTot = sharedPref.getInt("fatDayTot",1)
        val saccToday = sharedPref.getInt("saccToday",0)
        val proteinToday = sharedPref.getInt("proteinToday",0)
        val fatToday = sharedPref.getInt("fatToday",0)
        // 가져온 값 사용
        when(avatarImageID) {
            1 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.nutrientDetailCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }
        var progress = nowCal!! * 100 / goalCal!!
        binding.nutrientDetailProgressPb.updateProgress(progress)
        binding.nutrientDetailTitleCalTv.text = (goalCal - nowCal).toString() + "kcal"
        binding.nutrientDetailSaccTotalTv.text = "/" + saccDayTot.toString() + "g"
        binding.nutrientDetailProteinTotalTv.text = "/" + proteinDayTot.toString() + "g"
        binding.nutrientDetailFatTotalTv.text = "/" + fatDayTot.toString() + "g"
        binding.nutrientDetailSaccSizeTv.text = saccToday.toString()
        binding.nutrientDetailProteinSizeTv.text = proteinToday.toString()
        binding.nutrientDetailFatSizeTv.text = fatToday.toString()
    }
}