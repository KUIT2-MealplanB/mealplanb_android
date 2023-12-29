package com.example.mealplanb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.ActivityAddMealListBinding

class AddMealListActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddMealListBinding
    lateinit var mealList : ArrayList<Meal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealListBinding.inflate(layoutInflater)
        mealList = arrayListOf()
        mealList.addAll(
            arrayListOf(
                Meal("크림파스타",100,365),
                Meal("식빵",30,70),
                Meal("샐러드",50,80)))

        binding.meallistRv.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.meallistRv.adapter = AddMealListAdapter(mealList)

        binding.addmeallistBackmenuCl.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}