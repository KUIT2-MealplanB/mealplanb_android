package com.example.mealplanb

import SearchCategoryAdapter
import SearchMealAdapter
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealplanb.databinding.FragmentSearchMealBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchMealFragment : Fragment() {
    lateinit var binding: FragmentSearchMealBinding

    //즐겨찾기에 추가한 Meal 리사이클러뷰에 나타날 데이터 리스트 변수
    lateinit var mealList: ArrayList<Meal>
    private var oftenFoodList: ArrayList<Meal> = arrayListOf()
    private var myMadeList : ArrayList<Meal> = arrayListOf()

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

    lateinit var adapter: SearchMealAdapter // RecyclerView에 사용할 어댑터
    lateinit var oftenadapter: SearchCategoryAdapter // 즐겨찾기 RecyclerView에 사용할 어댑터

    var lastClickedButton: String? = null // '자주 먹는', '내가 만든' 버튼 중 어떤 것이 눌렸는지 저장하는 변수 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMealBinding.inflate(layoutInflater)
        //mealList lateinit 초기화
        mealList = arrayListOf()

        //초기 상태 설정
        binding.searchMealMyLl.visibility = View.VISIBLE
        binding.searchMealAllRv.visibility = View.GONE
        binding.searchMealMyRv.visibility=View.GONE

        //자주먹는 버튼을 눌렀을 때
        binding.searchMealBtnOftenLl.setOnClickListener{
            //없어지고 보이고
            binding.searchMealMyLl.visibility=View.GONE
            binding.searchMealAllRv.visibility=View.GONE
            binding.searchMealMyRv.visibility=View.VISIBLE

            //검색창 hint
            binding.searchMealInputEt.hint = "자주 먹는 식사"
            binding.searchMealInputEt.setHintTextColor(Color.parseColor("#7C5CF8"))

            // SharedPreferences에서 oftenFoodList 데이터 읽어오기
            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("oftenFoodList", null)

            // Gson을 사용하여 JSON 문자열을 역직렬화하여 oftenFoodList에 할당
            val type = object : TypeToken<ArrayList<Meal>>() {}.type
            oftenFoodList = gson.fromJson(json, type) ?: arrayListOf()

            Log.d("logcat",oftenFoodList.size.toString())

            // mealList에 oftenFoodList의 데이터를 추가
//            oftenFoodList?.let {
//                //Toast.makeText(this, "add data", Toast.LENGTH_SHORT).show()
//                for (Meal in oftenFoodList) {
//                    mealList.add(
//                        Meal(
//                            Meal.meal_name,
//                            Meal.meal_weight.toDouble(),
//                            Meal.meal_cal.toDouble(),
//                            Meal.sacc_gram.toDouble(),
//                            Meal.protein_gram.toDouble(),
//                            Meal.fat_gram.toDouble()
//                        )
//                    )
//                }
//            }

            oftenadapter = SearchCategoryAdapter(oftenFoodList) { item->
                val editor = sharedPreferences.edit()
                var newJson = gson.toJson(item)
                editor.putString("Key",newJson)
                editor.apply()
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).commit()
                requireActivity().overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_top)
                binding.searchMealInputEt.clearFocus()
            }

            //즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
            binding.searchMealMyRv.adapter = oftenadapter
            binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())

            // 버튼이 눌린 것을 저장
            lastClickedButton = "btnOften"
        }

        //내가만든 버튼을 눌렀을 때
        binding.searchMealBtnMadeLl.setOnClickListener{
            //없어지고 보이고
            binding.searchMealMyLl.visibility=View.GONE
            binding.searchMealAllRv.visibility=View.GONE
            binding.searchMealMyRv.visibility=View.VISIBLE

            //검색창 hint
            binding.searchMealInputEt.hint = "내가 만든 식사"
            binding.searchMealInputEt.setHintTextColor(Color.parseColor("#7C5CF8"))

            // SharedPreferences에서 oftenFoodList 데이터 읽어오기
            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("myMadeList", null)

            // Gson을 사용하여 JSON 문자열을 역직렬화하여 oftenFoodList에 할당
            val type = object : TypeToken<ArrayList<Meal>>() {}.type
            myMadeList = gson.fromJson(json, type) ?: arrayListOf()

            Log.d("logcat",myMadeList.size.toString())

//            myMadeList?.let {
//                //Toast.makeText(this, "add data", Toast.LENGTH_SHORT).show()
//                for (Meal in myMadeList) {
//                    mealList.add(
//                        Meal(
//                            Meal.meal_name,
//                            Meal.meal_weight.toDouble(),
//                            Meal.meal_cal.toDouble(),
//                            Meal.sacc_gram.toDouble(),
//                            Meal.protein_gram.toDouble(),
//                            Meal.fat_gram.toDouble()
//                        )
//                    )
//                }
//            }

            oftenadapter = SearchCategoryAdapter(myMadeList) { item->
                val editor = sharedPreferences.edit()
                var newJson = gson.toJson(item)
                editor.putString("Key",newJson)
                editor.apply()
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).commit()
                requireActivity().overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_top)
                binding.searchMealInputEt.clearFocus()
            }

            //즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
            binding.searchMealMyRv.adapter = oftenadapter
            binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())

            // 버튼이 눌린 것을 저장
            lastClickedButton = "btnMade"
        }

        // RecyclerView 설정
        adapter = SearchMealAdapter(items) { item->
            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(item)
            editor.putString("Key",newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).commit()
            requireActivity().overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_top)
            binding.searchMealInputEt.clearFocus()
        }

        binding.searchMealAllRv.layoutManager = LinearLayoutManager(requireContext())
        binding.searchMealAllRv.adapter = adapter

        oftenadapter = SearchCategoryAdapter(mealList) { item->
            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(item)
            editor.putString("Key",newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).commit()
            requireActivity().overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_slide_out_top)
            binding.searchMealInputEt.clearFocus()
        }

        //즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
        binding.searchMealMyRv.adapter = oftenadapter
        binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())

        Log.d("logcat",oftenadapter.itemCount.toString()+"!"+mealList.size.toString())

        //첫 화면에서 RecyclerView에 가나다 순으로 정렬된 식사 목록의 처음 10개 항목을 표시
        filterList("")

        // 검색창에 텍스트가 변경될 때마다 실행될 리스너 설정
        binding.searchMealInputEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //검색창 누르면 linearlayout 숨기기, recyclerview 보이기 + 반대
        binding.searchMealInputEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchMealMyLl.visibility = View.GONE
                //binding.searchMealFreeInputLl.visibility = View.GONE

                // 마지막으로 눌린 버튼에 따라 보여줄 뷰를 결정
                when (lastClickedButton) {
                    "btnOften" -> {
                        binding.searchMealAllRv.visibility = View.GONE
                        binding.searchMealMyRv.visibility = View.VISIBLE
                    }
                    "btnMade" -> {
                        binding.searchMealAllRv.visibility = View.GONE
                        binding.searchMealMyRv.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.searchMealAllRv.visibility = View.VISIBLE
                        binding.searchMealMyRv.visibility = View.GONE
                    }
                }
            } else {
                binding.searchMealMyLl.visibility = View.VISIBLE
                binding.searchMealAllRv.visibility = View.GONE
                binding.searchMealMyRv.visibility = View.GONE
            }
        }

        //자유 입력 버튼 누르면 실행
        binding.searchMealFreeInputLl.setOnClickListener {
            //Toast.makeText(this, "free input", Toast.LENGTH_SHORT).show()
            val bottomSheetFragment = SeachMealFreeInputFragment()
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).commit()
        }

        binding.searchMealOutIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
        }
        return binding.root
    }

    // 입력된 텍스트에 따라 리스트를 필터링하는 함수
    fun filterList(filterItem: String) {
        var tempList: ArrayList<Meal> = ArrayList()
        var oftenTempList: ArrayList<Meal> = ArrayList()

        // 리스트의 모든 아이템을 검사해서 입력된 텍스트가 포함되어 있으면 임시 리스트에 추가
        for(item in items) {
            if(item.meal_name.toLowerCase().contains(filterItem.toLowerCase())){
                tempList.add(item)
            }
        }

        //oftenFoodList의 모든 아이템을 검사해서 입력된 텍스트가 포함되어 있으면 oftenTempList에 추가
        for(item in oftenFoodList){
            if(item.meal_name.toLowerCase().contains(filterItem.toLowerCase())){
                oftenTempList.add(item)
            }
        }

        //리스트를 가나다 순으로 정렬
        tempList.sortWith(compareBy { it.meal_name })
        oftenTempList.sortWith(compareBy { it.meal_name })

        //리스트의 크기가 10을 초과하면, 앞에서부터 10개의 요소만 남김
        if(tempList.size>10){
            tempList = ArrayList(tempList.subList(0,10))
        }

        if(oftenTempList.size>10){
            oftenTempList = ArrayList(oftenTempList.subList(0,10))
        }

        // 임시 리스트로 어댑터를 업데이트
        adapter.updateList(tempList)
        oftenadapter.updateList(oftenTempList)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 현재 Fragment가 호스팅되는 Activity를 찾기
        val activity = context as? MainActivity

        // Activity의 백 버튼 이벤트를 현재 Fragment로 전달
        activity?.setOnBackPressedListener(object : OnBackPressedListener {
            override fun onBackPressed() {
                if (binding.searchMealInputEt.hasFocus()) {
                    binding.searchMealInputEt.clearFocus()
                } else {
//                    super.onBackPressed()
                }
            }
        })
    }
}