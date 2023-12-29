package com.example.mealplanb

import MyAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.search_list.databinding.ActivityMainBinding
import android.text.Editable
import android.text.TextWatcher
import com.example.mealplanb.databinding.ActivitySearchMealBinding

class SearchMealActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchMealBinding
    var items = arrayListOf(
        Meal("크림파스타", 100, 200),
        Meal("크림리조또", 120, 250),
        Meal("식빵", 30, 70),
        Meal("크림스프", 150, 180),
        Meal("샐러드", 50, 80)
    ) // 예시 데이터

    lateinit var adapter: MyAdapter // RecyclerView에 사용할 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        adapter = MyAdapter(items) { item->
            val intent = Intent(applicationContext,FoodDetailActivity::class.java)
            intent.putExtra("Key",item)
            startActivity(intent)
        }
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.myRecyclerView.adapter = adapter

        // 검색창에 텍스트가 변경될 때마다 실행될 리스너 설정
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

//        binding.myRecyclerView.setOnClickListener {
//            val intent = Intent(applicationContext,AddMealListActivity::class.java)
//            startActivity(intent)
//        }

        binding.out.setOnClickListener {
            finish()
        }
    }

    // 입력된 텍스트에 따라 리스트를 필터링하는 함수
    fun filterList(filterItem: String){
        var tempList: ArrayList<Meal> = ArrayList()

        // 리스트의 모든 아이템을 검사해서 입력된 텍스트가 포함되어 있으면 임시 리스트에 추가
        for(item in items){
            if(item.meal_name.toLowerCase().contains(filterItem.toLowerCase())){
                tempList.add(item)
            }
        }

        // 임시 리스트로 어댑터를 업데이트
        adapter.updateList(tempList)
    }

}
