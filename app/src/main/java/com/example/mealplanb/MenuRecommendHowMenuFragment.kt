package com.example.mealplanb

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentMenuRecommendHowMenuBinding

class MenuRecommendHowMenuFragment : Fragment() {
    lateinit var binding : FragmentMenuRecommendHowMenuBinding
    lateinit var adapter: MenuRecommendHowMenuAdapter
    var items = arrayListOf(
        Meal("크림파스타", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("크림리조또", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("식빵", 30.0, 70.0, 19.0, 9.0, 3.0),
        Meal("크림스프", 150.0, 180.0, 7.0, 0.0, 1.0),
        Meal("샐러드", 50.0, 80.0,4.0, 1.0, 0.0),
        Meal("김밥", 120.0, 250.0, 14.0, 4.0,3.0),
        Meal("김치볶음밥", 30.0, 70.0, 19.0, 9.0,3.0),
        Meal("볶음밥", 150.0, 180.0, 7.0, 0.0, 1.0),
        Meal("닭가슴살", 50.0, 80.0,4.0, 1.0, 0.0),
        Meal("닭볶음탕", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("계란", 120.0, 250.0, 14.0, 4.0,3.0),
        Meal("샐러리", 30.0, 70.0, 19.0, 9.0,3.0),
        Meal("방울토마토", 150.0, 180.0, 7.0, 0.0, 1.0),
        Meal("사과", 50.0, 80.0,4.0, 1.0, 0.0)
    ) // 예시 데이터 무작위 15개
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendHowMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuRecommendFragment = parentFragmentManager.findFragmentById(com.example.mealplanb.R.id.main_frm) as? MenuRecommendFragment

        adapter = MenuRecommendHowMenuAdapter(items) { item->
            binding.menuRecommHowMenuSearchEt.text = Editable.Factory.getInstance().newEditable(item.meal_name)

            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserItem(item.meal_name, "", 2),
                MenuRecommItem.SystemHowManyItem(item.meal_name,300,item.meal_cal.toInt(),10,6)
            )
        }

        binding.menuRecommHowMenuContentRv.layoutManager = LinearLayoutManager(context)
        binding.menuRecommHowMenuContentRv.adapter = adapter

        filterList("")

        binding.menuRecommHowMenuBackbtnCv.setOnClickListener {
            val initMenuFragment = MenuRecommendInitFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.example.mealplanb.R.id.menu_recomm_button_cv, initMenuFragment)
                .commit()
        }
        binding.menuRecommHowMenuSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun filterList(filterItem: String) {
        var tempList: ArrayList<Meal> = ArrayList()
        var oftenTempList: ArrayList<Meal> = ArrayList()

        // 리스트의 모든 아이템을 검사해서 입력된 텍스트가 포함되어 있으면 임시 리스트에 추가
        for(item in items) {
            if(item.meal_name.toLowerCase().contains(filterItem.toLowerCase())){
                tempList.add(item)
            }
        }

        //리스트를 가나다 순으로 정렬
        tempList.sortWith(compareBy { it.meal_name })

        //리스트의 크기가 10을 초과하면, 앞에서부터 10개의 요소만 남김
        if(tempList.size>10){
            tempList = ArrayList(tempList.subList(0,10))
        }

        // 임시 리스트로 어댑터를 업데이트
        adapter.updateList(tempList)
    }
}