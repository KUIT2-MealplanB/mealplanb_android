package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import android.widget.Toast
import com.example.mealplanb.databinding.FragmentMenuRecommendCheatdayBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.RecommendMealView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class MenuRecommendCheatdayFragment : Fragment(), RecommendMealView {
    lateinit var binding: FragmentMenuRecommendCheatdayBinding
    var cal = Calendar.getInstance()
    private val numList : ArrayList<String> = arrayListOf("첫","두","세","네","다섯","여섯","일곱","여덟","아홉","열")

    private var meal_name: String = "크림파스타"
    private var fat_gram: Double = 15.0
    private var protein_gram: Double = 15.0
    private var sacc_gram: Double = 35.0
    private var meal_cal: Double = 362.0

    var chickenItems = arrayListOf(
        Meal("굽네치킨 볼케이노", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("페리카나 신호등치킨", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("교촌치킨 레드콤보", 30.0, 70.0, 19.0, 9.0, 3.0)
    )

    var pizzaItems = arrayListOf(
        Meal("베이컨포테이토피자", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("콤비네이션피자", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("이탈리안치즈피자", 30.0, 70.0, 19.0, 9.0, 3.0)
    )

    var burgerItems = arrayListOf(
        Meal("불고기버거", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("핫크리스피버거", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("새우버거", 30.0, 70.0, 19.0, 9.0, 3.0)
    )

    var noodleItems = arrayListOf(
        Meal("돈코츠 라멘", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("신라면", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("우동", 30.0, 70.0, 19.0, 9.0, 3.0)
    )
    var snackbarItems = arrayListOf(
        Meal("신전떡볶이", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("엽기떡볶이", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("불스떡볶이", 30.0, 70.0, 19.0, 9.0, 3.0)
    )

    var polkbarItems = arrayListOf(
        Meal("장충동 왕족발보쌈", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("왕족발", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("보쌈", 30.0, 70.0, 19.0, 9.0, 3.0)
    )

    var dessertbarItems = arrayListOf(
        Meal("크림치즈케이크", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("바스크치즈케이크", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("말차딸기팬케이크", 30.0, 70.0, 19.0, 9.0, 3.0)
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendCheatdayBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences",Context.MODE_PRIVATE)
        val selectRecomm = sharedPreferences.getInt("recommSelect",1)

        val authService = AuthService(requireContext())
        authService.setRecommendMealView(this)

        if(selectRecomm == 2) {
            authService.myfavoriteMealCheck()
            updateVisibility()
        } else if(selectRecomm == 3) {
            authService.communityfavoriteMealCheck()
            updateVisibility()
        }

        //뒤로 가는 아이콘 클릭시
        binding.menuRecommCheatCategoryBackCv.setOnClickListener {
            val whatMenuFragment = MenuRecommendWhatMenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, whatMenuFragment)
                .commit()
        }

        //치킨 버튼 선택시
        binding.menuRecommCheatCategoryChickenBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until chickenItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(chickenItems[randomMenuIdx].meal_name, chickenItems[randomMenuIdx].sacc_gram.toInt(), chickenItems[randomMenuIdx].protein_gram.toInt(),chickenItems[randomMenuIdx].fat_gram.toInt(),5)
            )

            //SharedPreferences로 보낼 마지막 식단을 전역 변수에 저장
            updateVariables(chickenItems[randomMenuIdx])

            //카테고리 레이아웃과 select 레이아웃 관련 visibility 코드
            updateVisibility()
        }

        //피자 버튼 클릭시
        binding.menuRecommCheatCategoryPizzaBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until pizzaItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(pizzaItems[randomMenuIdx].meal_name, pizzaItems[randomMenuIdx].sacc_gram.toInt(), pizzaItems[randomMenuIdx].protein_gram.toInt(),pizzaItems[randomMenuIdx].fat_gram.toInt(),5)
            )

            updateVariables(pizzaItems[randomMenuIdx])
            updateVisibility()
        }

        //버거 버튼 클릭시
        binding.menuRecommCheatCategoryBurgerBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until burgerItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(burgerItems[randomMenuIdx].meal_name, burgerItems[randomMenuIdx].sacc_gram.toInt(), burgerItems[randomMenuIdx].protein_gram.toInt(),burgerItems[randomMenuIdx].fat_gram.toInt(),5)
            )

            updateVariables(burgerItems[randomMenuIdx])
            updateVisibility()
        }

        //면류 버튼 클릭시
        binding.menuRecommCheatCategoryNoodleBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until noodleItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(noodleItems[randomMenuIdx].meal_name, noodleItems[randomMenuIdx].sacc_gram.toInt(), noodleItems[randomMenuIdx].protein_gram.toInt(),noodleItems[randomMenuIdx].fat_gram.toInt(),5)
            )

            updateVariables(noodleItems[randomMenuIdx])
            updateVisibility()
        }

        //분식 버튼 클릭시
        binding.menuRecommCheatCategorySnackbarBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until snackbarItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(snackbarItems[randomMenuIdx].meal_name, snackbarItems[randomMenuIdx].sacc_gram.toInt(), snackbarItems[randomMenuIdx].protein_gram.toInt(),snackbarItems[randomMenuIdx].fat_gram.toInt(),5)
            )

            updateVariables(snackbarItems[randomMenuIdx])
            updateVisibility()
        }

        //족발/보쌈 버튼 클릭시
        binding.menuRecommCheatCategoryPorkBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until polkbarItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(polkbarItems[randomMenuIdx].meal_name, polkbarItems[randomMenuIdx].sacc_gram.toInt(), polkbarItems[randomMenuIdx].protein_gram.toInt(),polkbarItems[randomMenuIdx].fat_gram.toInt(),5)
            )

            updateVariables(polkbarItems[randomMenuIdx])
            updateVisibility()
        }

        //디저트 버튼 클릭시
        binding.menuRecommCheatCategoryDessertBtn.setOnClickListener {
            // Add data directly using the activity's public method
            var randomMenuIdx = (0 until dessertbarItems.size).random()
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.SystemMenuItem(dessertbarItems[randomMenuIdx].meal_name, dessertbarItems[randomMenuIdx].sacc_gram.toInt(), dessertbarItems[randomMenuIdx].protein_gram.toInt(),dessertbarItems[randomMenuIdx].fat_gram.toInt(),5)
            )
            updateVariables(dessertbarItems[randomMenuIdx])
            updateVisibility()
        }

        // 클릭 이벤트 설정
        binding.menuRecommCheatdayBtnSelectCl.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            // SharedPreferences.Editor 객체를 얻어서 값을 저장
            //전역변수로 선언한 변수에 home 화면으로 전달할 데이터 넣어줌
            val editor = sharedPref.edit()
            editor.putString("recommendMenu",meal_name)
            editor.putInt("recommendMenuCal",meal_cal.toInt())
            editor.putInt("recommendMenuSacc",sacc_gram.toInt())
            editor.putInt("recommendMenuProtein",protein_gram.toInt())
            editor.putInt("recommendMenuFat",fat_gram.toInt())

            val gson = Gson()
//            var newJson = gson.toJson(MealMainInfo(true,1,items[randomMenuIdx].meal_cal,R.drawable.item_hamburger_img))
//            var newJson = gson.toJson(MealMainInfo(true,1, meal_cal,R.drawable.item_hamburger_img, "", 0.0)) new!!!
//            editor.putString("MealMainInfo",newJson)
//            editor.apply()
            var json = sharedPref.getString("dayMealList",null)
            var dayMealList = gson.fromJson(json,object : TypeToken<ArrayList<MealMainInfo>>() {}.type) ?: arrayListOf(
                MealMainInfo(true,1, meal_cal,R.drawable.item_hamburger_img, "", 0.0)
            )
            var mealEmptyFlag = false
            var addMealNum : Int
            for(i in 0 until dayMealList.size) {
                if(!dayMealList[i].meal_active) {
                    mealEmptyFlag = true
                    addMealNum = i+1
                    dayMealList.set(addMealNum-1, MealMainInfo(true,addMealNum,meal_cal,R.drawable.item_hamburger_img,"",0.0))
                    cal.time = Date()
                    menuRecommendFragment?.addItemToRecyclerView(
                        RecommendMenu(cal,numList[addMealNum-1] + " 끼 : " + meal_name,
                            sacc_gram.toInt(),
                            protein_gram.toInt(),
                            fat_gram.toInt()))
                    val newJson = gson.toJson(dayMealList)
                    val editor = sharedPref.edit()
                    editor.putString("dayMealList",newJson)
                    editor.apply()
                    break
                }
            }
            if(!mealEmptyFlag) {
                if(dayMealList.size < 10) {
                    addMealNum = dayMealList.size+1
                    dayMealList.add(MealMainInfo(true,addMealNum,meal_cal,R.drawable.item_hamburger_img,"",0.0))
                    cal.time = Date()
                    menuRecommendFragment?.addItemToRecyclerView(
                        RecommendMenu(cal,numList[addMealNum-1] + " 끼 : " + meal_name,
                            sacc_gram.toInt(),
                            protein_gram.toInt(),
                            fat_gram.toInt()))
                    val newJson = gson.toJson(dayMealList)
                    val editor = sharedPref.edit()
                    editor.putString("dayMealList",newJson)
                    editor.apply()
                }
                else {
                    Toast.makeText(requireContext(),"더 이상 메뉴를 추가할 수 없습니다! 잠시 후 홈으로 이동됩니다.",Toast.LENGTH_SHORT).show()
                }
            }

            // 클릭 시 WhatMenuFragment로 교체
//            val selectFragment = MenuRecommendSelectFragment()
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.menu_recomm_button_cv, selectFragment)
//                .commit()
            menuRecommendFragment?.addWhatMenuFragmentItems(
                MenuRecommItem.UserItem("이 음식", "으로 먹을래요", 2),
                MenuRecommItem.SystemUpdateMenuItem(meal_name!!,7)
            )
//            cal.time = Date()
//            menuRecommendFragment?.addItemToRecyclerView(
//                RecommendMenu(cal,"첫 끼 : " + items[randomMenuIdx].meal_name,
//                    items[randomMenuIdx].sacc_gram.toInt(),
//                    items[randomMenuIdx].protein_gram.toInt(),
//                    items[randomMenuIdx].fat_gram.toInt()))
            val selectFragment = activity?.findViewById<View>(R.id.menu_recomm_button_cv)
            selectFragment?.visibility = View.GONE

            //5초의 delay 후 home으로 이동
            CoroutineScope(Dispatchers.Main).launch {
                delay(5000)
                withContext(Dispatchers.Main) {
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frm, HomeFragment())
                    transaction.commit()

                    // 아이콘 변경 코드
                    val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_navigation)
                    bottomNavigationView.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.navi_mypage_active_ic)
                    bottomNavigationView.menu.findItem(R.id.menu_food).setIcon(R.drawable.navi_food_inactive_ic)
                }
            }
        }

        //다시 식단을 고를래요 선택 시
        binding.menuRecommCheatdayBtnCl.setOnClickListener {
            menuRecommendFragment?.addWhatMenuFragmentItems(MenuRecommItem.UserItem("다시 ", "식단을 고를래요", 2))

            //카테고리 프레그먼트가 다시 뜰 수 있도록함
            if(selectRecomm == 1) {
                binding.menuRecommCheatCategoryLv.visibility = view.visibility
                binding.menuRecommCheatdayButtonLv.visibility = View.INVISIBLE
            } else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.menu_recomm_button_cv, MenuRecommendWhatMenuFragment())
                    .commit()
            }
        }

        // 다른 초기화 작업 등을 수행할 수 있음
    }

    // 변수 업데이트 함수
    private fun updateVariables(item: Meal) {
        meal_name = item.meal_name
        sacc_gram = item.sacc_gram
        protein_gram = item.protein_gram
        fat_gram = item.fat_gram
        meal_cal = item.meal_cal
    }

    // 가시성 업데이트 함수
    private fun updateVisibility() {
        binding.menuRecommCheatdayButtonLv.visibility = View.VISIBLE
        binding.menuRecommCheatCategoryLv.visibility = View.INVISIBLE
    }

    override fun myFavoriteMealCheckSuccess(
        food_id: Long,
        name: String,
        offer: String,
        offer_carbohydrate: Int,
        offer_protein: Int,
        offer_fat: Int
    ) {
        meal_name = name
        sacc_gram = offer_carbohydrate.toDouble()
        protein_gram = offer_protein.toDouble()
        fat_gram = offer_fat.toDouble()

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment
        menuRecommendFragment?.addCheatMenuFragmentItems(
            MenuRecommItem.SystemMenuItem(meal_name, sacc_gram.toInt(), protein_gram.toInt(),fat_gram.toInt(),5)
        )
    }

    override fun communityFavoiriteMealCheckSuccess(
        food_id: Long,
        name: String,
        offer: String,
        offer_carbohydrate: Int,
        offer_protein: Int,
        offer_fat: Int
    ) {
        meal_name = name
        sacc_gram = offer_carbohydrate.toDouble()
        protein_gram = offer_protein.toDouble()
        fat_gram = offer_fat.toDouble()

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment
        menuRecommendFragment?.addCheatMenuFragmentItems(
            MenuRecommItem.SystemMenuItem(meal_name, sacc_gram.toInt(), protein_gram.toInt(),fat_gram.toInt(),5)
        )
    }
}