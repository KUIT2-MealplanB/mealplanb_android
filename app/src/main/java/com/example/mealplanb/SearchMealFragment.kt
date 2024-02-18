package com.example.mealplanb

import FavoriteFoodAdapter
import SearchCategoryAdapter
import SearchMealAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mealplanb.databinding.FragmentSearchMealBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.FavoriteFoodResponse
import com.example.mealplanb.remote.Food
import com.example.mealplanb.remote.FoodSearchResponse
import com.example.mealplanb.remote.SearchFoodView
import com.example.mealplanb.remote.SignupView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchMealFragment : Fragment(), SignupView, SearchFoodView {
    lateinit var binding: FragmentSearchMealBinding

    //즐겨찾기에 추가한 Meal 리사이클러뷰에 나타날 데이터 리스트 변수
    lateinit var mealList: ArrayList<Meal>
    private var oftenFoodList: ArrayList<Food> = arrayListOf()
    private var myMadeList: ArrayList<Meal> = arrayListOf()

    lateinit var adapter: SearchMealAdapter // RecyclerView에 사용할 어댑터
    lateinit var oftenadapter: SearchCategoryAdapter
    lateinit var favoritefoodadapter: FavoriteFoodAdapter //즐겨찾기 RecyclerView에 사용할 어댑터

    var lastClickedButton: String? = null // '자주 먹는', '내가 만든' 버튼 중 어떤 것이 눌렸는지 저장하는 변수 추가

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMealBinding.inflate(layoutInflater)
        //mealList lateinit 초기화
        mealList = arrayListOf()

        lateinit var adapter: SearchMealAdapter // RecyclerView에 사용할 어댑터

        //API관련
        val authService = AuthService(requireContext())
        authService.setSignupView(this)
        authService.setSearchFoodView(this)

        //초기 상태 설정
        binding.searchMealMyLl.visibility = View.VISIBLE
        binding.searchMealAllRv.visibility = View.GONE
        binding.searchMealMyRv.visibility = View.GONE

        //'X'버튼 눌렀을 때
        binding.searchMealInitOutIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()).commit()
        }

        //자주먹는 버튼을 눌렀을 때
        binding.searchMealBtnOftenLl.setOnClickListener {

            //API관련
            authService.favoriteFoodGET()

            //없어지고 보이고
            binding.searchMealMyLl.visibility = View.GONE
            binding.searchMealAllRv.visibility = View.GONE
            binding.searchMealMyRv.visibility = View.VISIBLE

            //뒤로 가기 버튼 등장
            binding.searchMealOutIv.visibility = View.VISIBLE

            //검색창 hint
            binding.searchMealInputEt.hint = "자주 먹는 식사"
            binding.searchMealInputEt.setHintTextColor(Color.parseColor("#7C5CF8"))

            // 즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
            binding.searchMealMyRv.adapter = favoritefoodadapter
            binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())

            // 버튼이 눌린 것을 저장
            lastClickedButton = "btnOften"

            //'X'버튼 눌리면
            binding.searchMealOutIv.setOnClickListener {
                binding.searchMealAllRv.visibility = View.GONE
                binding.searchMealMyRv.visibility = View.GONE
                binding.searchMealMyLl.visibility = View.VISIBLE

                //검색창 hint
                binding.searchMealInputEt.hint = "식사 이름을 입력해주세요."
                binding.searchMealInputEt.setHintTextColor(Color.GRAY)

                binding.searchMealInitOutIv.visibility = View.VISIBLE

                binding.searchMealOutIv.visibility = View.GONE


                // 호출 시점에 hideKeyboard 함수를 호출하여 키보드를 숨깁니다.
                hideKeyboard()

                //검색 커서 깜빡임 제어
                binding.searchMealInputEt.clearFocus()

                lastClickedButton = " "

            }
            binding.searchMealInitOutIv.setOnClickListener {
                lastClickedButton = " "
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment()).commit()
            }
        }

        //내가만든 버튼을 눌렀을 때
        binding.searchMealBtnMadeLl.setOnClickListener {

            // 버튼이 눌린 것을 저장
            lastClickedButton = "btnMade"

            //뒤로 가기 버튼 등장
            binding.searchMealOutIv.visibility = View.VISIBLE
            binding.searchMealAllRv.visibility = View.GONE
            binding.searchMealMyRv.visibility = View.VISIBLE

            //없어지고 보이고
            binding.searchMealMyLl.visibility = View.GONE

            //검색창 hint
            binding.searchMealInputEt.hint = "내가 만든 식사"
            binding.searchMealInputEt.setHintTextColor(Color.parseColor("#7C5CF8"))

            // SharedPreferences에서 oftenFoodList 데이터 읽어오기
            val sharedPreferences =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("myMadeList", null)

            // Gson을 사용하여 JSON 문자열을 역직렬화하여 oftenFoodList에 할당
            val type = object : TypeToken<ArrayList<Meal>>() {}.type
            myMadeList = gson.fromJson(json, type) ?: arrayListOf()

            Log.d("logcat", myMadeList.size.toString())

            oftenadapter = SearchCategoryAdapter(myMadeList) { item ->
                val editor = sharedPreferences.edit()
                var newJson = gson.toJson(item)
                editor.putString("Key", newJson)
                editor.apply()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, FoodDetailFragment()).commit()
                requireActivity().overridePendingTransition(
                    androidx.appcompat.R.anim.abc_slide_in_bottom,
                    androidx.appcompat.R.anim.abc_slide_out_top
                )
                binding.searchMealInputEt.clearFocus()
            }

            //즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
            binding.searchMealMyRv.adapter = oftenadapter
            binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())

            //'X'버튼 눌리면
            binding.searchMealOutIv.setOnClickListener {
                binding.searchMealAllRv.visibility = View.GONE
                binding.searchMealMyRv.visibility = View.GONE
                binding.searchMealMyLl.visibility = View.VISIBLE

                //검색창 hint
                binding.searchMealInputEt.hint = "식사 이름을 입력해주세요."
                binding.searchMealInputEt.setHintTextColor(Color.GRAY)

                binding.searchMealInitOutIv.visibility = View.VISIBLE

                binding.searchMealOutIv.visibility = View.GONE

                // 호출 시점에 hideKeyboard 함수를 호출하여 키보드를 숨깁니다.
                hideKeyboard()

                //검색 커서 깜빡임 제어
                binding.searchMealInputEt.clearFocus()

                lastClickedButton = " "

            }
            binding.searchMealInitOutIv.setOnClickListener {
                lastClickedButton = " "
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment()).commit()

            }
        }

        oftenadapter = SearchCategoryAdapter(mealList) { item ->
            val sharedPreferences =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(item)
            editor.putString("Key", newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FoodDetailFragment()).commit()
            requireActivity().overridePendingTransition(
                androidx.appcompat.R.anim.abc_slide_in_bottom,
                androidx.appcompat.R.anim.abc_slide_out_top
            )
            binding.searchMealInputEt.clearFocus()
        }

        //즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
        binding.searchMealMyRv.adapter = oftenadapter
        binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView 설정
        adapter = SearchMealAdapter(ArrayList()) { item ->
            val sharedPreferences =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(item)
            editor.putString("Key", newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FoodDetailFragment()).commit()
            requireActivity().overridePendingTransition(
                androidx.appcompat.R.anim.abc_slide_in_bottom,
                androidx.appcompat.R.anim.abc_slide_out_top
            )
            binding.searchMealInputEt.clearFocus()
        }

        // Initialize favoritefoodadapter
        favoritefoodadapter = FavoriteFoodAdapter(ArrayList()) { item ->
            val sharedPreferences =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(item)
            editor.putString("Key", newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FoodDetailFragment()).commit()
            requireActivity().overridePendingTransition(
                androidx.appcompat.R.anim.abc_slide_in_bottom,
                androidx.appcompat.R.anim.abc_slide_out_top
            )
            binding.searchMealInputEt.clearFocus()
        }


        // 검색창에 텍스트가 변경될 때마다 실행될 리스너 설정
        binding.searchMealInputEt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                //API관련
                authService.searchfood(s.toString(), 0)
                Log.d("보낸 쿼리", s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //검색창 누르면 linearlayout 숨기기, recyclerview 보이기 + 반대
        binding.searchMealInputEt.setOnFocusChangeListener { _, hasFocus ->
            authService.searchfood("", 0)

            binding.searchMealOutIv.visibility = View.VISIBLE

            if (hasFocus) {
                binding.searchMealMyLl.visibility = View.GONE
                binding.searchMealOutIv.setOnClickListener {
                    binding.searchMealAllRv.visibility = View.GONE
                    binding.searchMealMyRv.visibility = View.GONE
                    binding.searchMealMyLl.visibility = View.VISIBLE

                    //검색창 hint
                    binding.searchMealInputEt.hint = "식사 이름을 입력해주세요."
                    binding.searchMealInputEt.setHintTextColor(Color.GRAY)

                    // 호출 시점에 hideKeyboard 함수를 호출하여 키보드를 숨깁니다.
                    hideKeyboard()

                    //검색 커서 깜빡임 제어
                    binding.searchMealInputEt.clearFocus()
                }
                // 마지막으로 눌린 버튼에 따라 보여줄 뷰를 결정
                when (lastClickedButton) {
                    "btnOften" -> {
                        lastClickedButton = " "
                    }
                    "btnMade" -> {
                        lastClickedButton = " "
                    }
                    else -> {
                        binding.searchMealAllRv.visibility = View.VISIBLE
                        binding.searchMealMyRv.visibility = View.GONE

                        //즐겨찾기에 추가한 데이터를 관리하는 리사이클러뷰
                        binding.searchMealAllRv.adapter = adapter
                        binding.searchMealAllRv.layoutManager = LinearLayoutManager(requireContext())
                        lastClickedButton = " "
                    }
                }
            } else {
                binding.searchMealMyLl.visibility = View.VISIBLE
                binding.searchMealAllRv.visibility = View.GONE
                binding.searchMealMyRv.visibility = View.GONE

                binding.searchMealOutIv.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment()).commit()
                }


            }
        }

        //자유 입력 버튼 누르면 실행
        binding.searchMealFreeInputLl.setOnClickListener {
            val bottomSheetFragment = SeachMealFreeInputFragment()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                bottomSheetFragment.tag
            )
        }

        return binding.root
    }

    // 키보드를 숨기는 함수
    fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = view?.findFocus()

        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
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

    override fun SignupLoading() {
        Toast.makeText(requireContext(), "loading 중", Toast.LENGTH_SHORT).show()
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
        favoriteFoodResponse?.let { response ->
            val foods: List<Food>? = response.foods

            if (foods != null) {
                // Ensure foods is of type ArrayList<Food>
                if (foods is ArrayList<*>) {
                    // Cast to ArrayList<Food>
                    val foodList = foods as ArrayList<Food>

                    favoritefoodadapter = FavoriteFoodAdapter(foodList) { item ->
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, FoodDetailFragment()).commit()
                        requireActivity().overridePendingTransition(
                            androidx.appcompat.R.anim.abc_slide_in_bottom,
                            androidx.appcompat.R.anim.abc_slide_out_top
                        )
                    }

                    // Set the adapter for the RecyclerView
                    binding.searchMealMyRv.adapter = favoritefoodadapter
                    binding.searchMealMyRv.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    Toast.makeText(requireContext(), "Invalid data type for foods", Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(requireContext(), "Favorite food list is null", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Favorite food response is null", Toast.LENGTH_SHORT).show()
        }
    }

    override fun SearchFoodSuccess(foodSearchResponse: FoodSearchResponse) {
        foodSearchResponse?.foods?.let { foods ->
            for (food in foods) {
                Log.d("search Food Item in fragment", "Food ID: ${food.foodId}, Food Name: ${food.foodName}, Kcal: ${food.kcal}")
            }
        }

        val items = foodSearchResponse.foods

        adapter = SearchMealAdapter(items) { item ->
            val sharedPreferences =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val gson = Gson()
            val editor = sharedPreferences.edit()
            var newJson = gson.toJson(item.foodId)
            editor.putString("Key", newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FoodDetailFragment()).commit()
            requireActivity().overridePendingTransition(
                androidx.appcompat.R.anim.abc_slide_in_bottom,
                androidx.appcompat.R.anim.abc_slide_out_top
            )
            binding.searchMealInputEt.clearFocus()
        }

        // Set the adapter for the RecyclerView
        binding.searchMealAllRv.adapter = adapter
        binding.searchMealAllRv.layoutManager = LinearLayoutManager(requireContext())

    }

}