package com.example.mealplanb
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentHomeBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.Food
import com.example.mealplanb.remote.HomeMealView
import com.example.mealplanb.remote.MealFoodResponseFoodList
import com.example.mealplanb.remote.MealListDateResponseMeals
import com.example.mealplanb.remote.MyMealFoodListResponse
import com.example.mealplanb.remote.SignupView
import com.example.mealplanb.remote.mymealResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener, SignupView, WeightUpdateListener, HomeMealView {
    lateinit var binding : FragmentHomeBinding
    lateinit var dayMealList : ArrayList<MealMainInfo>
    lateinit var authService: AuthService
    private var adapter : DayMealAdapter? = null
    private val cal : Calendar = Calendar.getInstance()
    private val calToday : Calendar = Calendar.getInstance()
    private var goalCal : Int? = null
    private var nowCal : Int? = null
    private var saccDayTot : Int? = null
    private var proteinDayTot : Int? = null
    private var fatDayTot : Int? = null
    private var saccToday : Int? = null
    private var proteinToday : Int? = null
    private var fatToday : Int? = null

    //API 데이터 확인
    private val KEY_WEIGHT_DATA = "weightData"

    // 날짜와 체중 데이터를 저장할 맵
    private val weightDataMap: MutableMap<String, Float> = mutableMapOf()

    val numList : ArrayList<String> = arrayListOf("첫 끼","두 끼","세 끼","네 끼","다섯 끼",
        "여섯 끼","일곱 끼","여덟 끼","아홉 끼","열 끼")

    override fun onResume() {
        super.onResume()

        authService = AuthService(requireContext()) // AuthService 인스턴스 생성
        authService.checkAvatarInfo() // 아바타 정보 조회

        //weight 가져오기
        // 저장된 데이터 불러오기
        loadWeightData()
        // SharedPreferences 객체 생성
        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences에서 값 가져오기
        val nickname = sharedPref.getString("nickname", "낄낄")
        val avatarImageID = sharedPref.getInt("avatar",3)
        val avatarappearance = sharedPref.getInt("avatarAppearance",1)

        // SharedPreferences의 변경을 감지하는 리스너 등록
        sharedPref.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "nickname") {
                val updatedNickname = sharedPreferences.getString(key, "낄낄")
                binding.mainTitleNicknameTv.text = updatedNickname
            }
            if(key == "avatar"){
                val updatedAvatar = sharedPreferences.getInt(key, 3)
                when(updatedAvatar) {
                    1 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
                    2 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
                    3 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
                    4 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
                    5 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
                }
            }
            if(key == "avatarAppearance"){
                val updatedAvatarAppearance = sharedPreferences.getInt(key, 3)
                when (updatedAvatarAppearance) {
                    1 -> {
                        if (avatarImageID == 1) { //핑
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_pink_img)
                        } else if (avatarImageID == 2) { //흰
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_white_img)
                        } else if (avatarImageID == 3) { //보
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_purple_img)
                        } else if (avatarImageID == 4) { //검
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_black_img)
                        } else if (avatarImageID == 5) { //회
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_gray_img)
                        }
                    }

                    2 -> {
                        if (avatarImageID == 1) { //핑
                            binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
                        } else if (avatarImageID == 2) { //흰
                            binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
                        } else if (avatarImageID == 3) { //보
                            binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
                        } else if (avatarImageID == 4) { //검
                            binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
                        } else if (avatarImageID == 5) { //회
                            binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
                        }
                    }

                    3 -> {

                        if (avatarImageID == 1) { //핑
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_pink_img)
                        } else if (avatarImageID == 2) { //흰
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_white_img)
                        } else if (avatarImageID == 3) { //보
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_purple_img)
                        } else if (avatarImageID == 4) { //검
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_black_img)
                        } else if (avatarImageID == 5) { //회
                            binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_gray_img)
                        }
                    }
                }
            }
        }

//        val gson = Gson()
//        var json = sharedPref.getString("dayMealList",null)
//        dayMealList = gson.fromJson(json,object : TypeToken<ArrayList<MealMainInfo>>() {}.type) ?: arrayListOf(
//            MealMainInfo(false,1,0.0,0,"", 0.0)
//        )

        binding.mainMeallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter = DayMealAdapter(dayMealList,requireContext(),authService)
        binding.mainMeallistRv.adapter = adapter

//        binding.mainMeallistRv.isNestedScrollingEnabled = false
//        binding.mainMeallistRv.overScrollMode = View.OVER_SCROLL_ALWAYS
//        binding.mainMeallistSv.isFillViewport = true

//        // 가져온 값 사용
        binding.mainTitleNicknameTv.text = nickname
        when(avatarImageID) {
            1 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }
        when (avatarappearance) {
            1 -> {
                if (avatarImageID == 1) { //핑
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_pink_img)
                } else if (avatarImageID == 2) { //흰
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_white_img)
                } else if (avatarImageID == 3) { //보
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_purple_img)
                } else if (avatarImageID == 4) { //검
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_black_img)
                } else if (avatarImageID == 5) { //회
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_fat_gray_img)
                }
            }

            2 -> {
                if (avatarImageID == 1) { //핑
                    binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
                } else if (avatarImageID == 2) { //흰
                    binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
                } else if (avatarImageID == 3) { //보
                    binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
                } else if (avatarImageID == 4) { //검
                    binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
                } else if (avatarImageID == 5) { //회
                    binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
                }
            }

            3 -> {

                if (avatarImageID == 1) { //핑
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_pink_img)
                } else if (avatarImageID == 2) { //흰
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_white_img)
                } else if (avatarImageID == 3) { //보
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_purple_img)
                } else if (avatarImageID == 4) { //검
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_black_img)
                } else if (avatarImageID == 5) { //회
                    binding.mainCharacterIv.setImageResource(R.drawable.avatar_muscle_gray_img)
                }
            }
        }
//        var json = sharedPref.getString("dayMealList",null)
//        dayMealList = gson.fromJson(json,object : TypeToken<ArrayList<MealMainInfo>>() {}.type) ?: arrayListOf(
//            MealMainInfo(false,1,0.0,0,"", 0.0)
//        )

//        binding.mainMeallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        adapter = DayMealAdapter(dayMealList,requireContext())
//        binding.mainMeallistRv.adapter = adapter

        // 가져온 값 사용
//        binding.mainTitleNicknameTv.text = nickname
//        when(avatarImageID) {
//            1 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
//            2 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
//            3 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
//            4 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
//            5 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
//     

        //끼니 slot 추가
        binding.mainDaymealAddCv.setOnClickListener {
            if(dayMealList.size < 10) {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                authService = AuthService(requireContext())
                authService.setHomeMealView(this)
                authService.mealAddPost(dayMealList.size+1,dateFormat.format(cal.time))
//
//                val mealNum = dayMealList.size+1
//                dayMealList.add(MealMainInfo(false,mealNum,100,numList[mealNum-1],0.0,
//                    R.drawable.item_hamburger_img,dateFormat.format(cal.time),0.0))
//                var newItemIdx = dayMealList.size-1
//                adapter?.notifyItemInserted(newItemIdx)
//
//                val editor = sharedPref.edit()
//                val newJson = gson.toJson(dayMealList)
//                editor.putString("dayMealList",newJson)
//                editor.apply()
            }
            else {
                Toast.makeText(requireContext(),"더 이상 추가할 수 없습니다!",Toast.LENGTH_SHORT).show()
            }
        }

        calToday.time = Date()
        setHomeData()

        //날짜 설정
        binding.mainDateTv.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),this,year,month,dayOfMonth)
            datePickerDialog.show()

        }
        //전 날로 이동
        binding.mainArrowLeftIv.setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH,-1)
            setHomeData()
        }
        //다음 날로 이동
        binding.mainArrowRightIv.setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH,1)
            setHomeData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        dayMealList = arrayListOf()
//        dayMealList.addAll(
//            arrayListOf(
//                MealMainInfo(false,1,0.0,R.drawable.item_hamburger_img,"", 0.0)))

        authService = AuthService(requireContext())
        authService.setHomeMealView(this)
        adapter = DayMealAdapter(dayMealList,requireContext(),authService)
        binding.mainMeallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.mainMeallistRv.adapter = adapter

        //캐릭터 tab하면 상세 영양소 페이지로 연결
        binding.mainCharacterIv.setOnClickListener{
            val intent = Intent(requireContext(), NutrientdetailActivity::class.java)
            startActivity(intent)
        }
        //햄버거 아이콘 tab하면 히든 페이지로 연결
        binding.mainMenuIv.setOnClickListener {
            //페이지 전환
            val intent = Intent(requireContext(), HiddenPageActivity::class.java)
            startActivity(intent)
        }

        //오늘의 무게 클릭했을 때 돌아가는 애니메이션
        binding.mainDayweightContentCv.setOnClickListener {
            val rotateAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_180)
            val rotateAnimOp = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_180_opposite)
            binding.todayWeightIv1.startAnimation(rotateAnim)
            binding.todayWeightIv2.startAnimation(rotateAnimOp)
        }

        //체중계 눌렀을 때 오늘의 체중 입력하고, sharedpreference에 저장 및 서버에 전송
        binding.mainDayweightContentCv.setOnClickListener {
            val bottomSheetFragment = HomeWeightInputFragment()
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
            bottomSheetFragment.weightUpdateListener = this // 리스너 설정

            //오늘의 체중 정보
            val sharedPref = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val todayweight = sharedPref?.getFloat("TodayWeight",0.2f)


            val today = binding.mainDateTv.text.toString()
            addWeightData(today,todayweight!!)

            weightDataMap[today] = todayweight

            authService.setSignupView(this)
            authService.weightpost(todayweight, today)

            saveWeightData()

        }

        //API관련
//        authService = AuthService(requireContext())
        authService.setSignupView(this)
        authService.weightcheck()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        authService.userProfileCheck(dateFormat.format(cal.time))
        authService.mealListDayCheck(dateFormat.format(cal.time))

        return binding.root
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year,month,dayOfMonth)
        val cal1 = cal
        val cal2 = calToday

        cal1.set(Calendar.HOUR_OF_DAY,0)
        cal1.set(Calendar.MINUTE,0)
        cal1.set(Calendar.SECOND,0)
        cal1.set(Calendar.MILLISECOND,0)
        cal2.set(Calendar.HOUR_OF_DAY,0)
        cal2.set(Calendar.MINUTE,0)
        cal2.set(Calendar.SECOND,0)
        cal2.set(Calendar.MILLISECOND,0)

        setHomeData()

//        val dayDiff = TimeUnit.MILLISECONDS.toDays(cal1.timeInMillis - cal2.timeInMillis)
//        if(dayDiff >= 0) {
//            binding.mainTitleContent1Tv.text = "님의 " + (dayDiff + 1).toString() + "일차"
//        } else {
//            binding.mainTitleContent1Tv.text = "님의 0일차"
//        }
    }

    fun setHomeData() {
        binding.mainDateTv.text = setDayText()
//        setProgress()
//        setNutData()
        // 체중 UI 업데이트
        updateWeightOnUI()

        authService = AuthService(requireContext())
        authService.setSignupView(this)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        authService.userProfileCheck(dateFormat.format(cal.time))
        authService.mealListDayCheck(dateFormat.format(cal.time))
    }

    fun setDayText(): String {
        val selectedMonth = cal.get(Calendar.MONTH) + 1
        val selectedDay = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = when(cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "일"
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            else -> ""
        }
        return "${String.format("%02d", selectedMonth)}. ${String.format("%02d", selectedDay)}. $dayOfWeek"
    }

//    fun setNutData() {
//        //일일 총 영양소 섭취량과 실 영양소 섭취량에 대한 더미 데이터 설정
//        saccDayTot = 500
//        proteinDayTot = 700
//        fatDayTot = 300
//        saccToday = 15 * cal.get(Calendar.DAY_OF_MONTH)
//        proteinToday = 20 * cal.get(Calendar.DAY_OF_MONTH)
//        fatToday = 9 * cal.get(Calendar.DAY_OF_MONTH)
//
//        binding.mainSaccSizeTv.text = saccToday.toString()
//        binding.mainProteinSizeTv.text = proteinToday.toString()
//        binding.mainFatSizeTv.text = fatToday.toString()
//        binding.mainSaccTotalTv.text = "/" + saccDayTot.toString() + "g"
//        binding.mainProteinTotalTv.text = "/" + proteinDayTot.toString() + "g"
//        binding.mainFatTotalTv.text = "/" + fatDayTot.toString() + "g"
//
//        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//        // SharedPreferences.Editor 객체를 얻어서 값을 저장
//        val editor = sharedPref.edit()
//        editor.putInt("saccDayTot",saccDayTot!!)
//        editor.putInt("proteinDayTot",proteinDayTot!!)
//        editor.putInt("fatDayTot",fatDayTot!!)
//        editor.putInt("saccToday",saccToday!!)
//        editor.putInt("proteinToday",proteinToday!!)
//        editor.putInt("fatToday",fatToday!!)
//        editor.apply()
//    }

//    fun setProgress() {
//        //progress 값 및 남은 칼로리 설정
//        goalCal = binding.mainProgressbgPb.getText()?.toInt()
//        nowCal = adapter!!.totCal()
//        var progress = nowCal!! * 100 / goalCal!!
//        binding.mainProgressPb.updateProgress(progress)
//        binding.mainProgressPb.setText(nowCal.toString())
//        binding.mainTitleCalTv.text = (goalCal!! - nowCal!!).toString() + "kcal"
//
//        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//        // SharedPreferences.Editor 객체를 얻어서 값을 저장
//        val editor = sharedPref.edit()
//        editor.putInt("goalCal",goalCal!!)
//        editor.putInt("nowCal",nowCal!!)
//        editor.apply()
//    }

    override fun SignupLoading() {
    }

    override fun SignupSuccess() {
    }
    override fun WeightcheckSuccess(weight: Float, date: String) {

        //입력받은 날짜 형식 바꿔주기
        val convertedDate = convertDateFormat(date)

        // 날짜와 체중을 맵에 저장
        weightDataMap[convertedDate] = weight

        Log.d("get으로 받은 값이 Map에 잘 저장되었나?", weightDataMap[convertedDate].toString())

        Log.d("받은 weight", weight.toString())
        Log.d("받은 날짜", convertedDate)

    }

    override fun UserProfileCheckSuccess(
        date: String,
        nickname: String,
        elapsed_days: Int,
        remaining_kcal: Int,
        avatar_color: String,
        avatar_appearance: String,
        target_kcal: Int,
        target_carbohydrate: Int,
        target_protein: Int,
        target_fat: Int,
        kcal: Int,
        carbohydrate: Int,
        protein: Int,
        fat: Int,
        sodium: Int,
        sugar: Int,
        saturated_fat: Int,
        trans_fat: Int,
        cholesterol: Int
    ) {
        if(kcal * 100 / (kcal + remaining_kcal) > 100) {
            binding.mainProgressPb.updateProgress(100)
        } else {
            binding.mainProgressPb.updateProgress(kcal * 100 / (kcal + remaining_kcal))
        }

        binding.mainTitleNicknameTv.text = nickname
        binding.mainTitleContent1Tv.text = "님의 " + elapsed_days.toString() +"일차"
        binding.mainTitleCalTv.text = remaining_kcal.toString() + "kcal"
        binding.mainProgressbgPb.setText(target_kcal.toString())
        binding.mainSaccTotalTv.text = "/" + target_carbohydrate.toString() + "g"
        binding.mainProteinTotalTv.text = "/" + target_protein.toString() + "g"
        binding.mainFatTotalTv.text = "/" + target_fat.toString() + "g"
        binding.mainProgressPb.setText(kcal.toString())
        binding.mainSaccSizeTv.text = carbohydrate.toString()
        binding.mainProteinSizeTv.text = protein.toString()
        binding.mainFatSizeTv.text = fat.toString()
    }

    override fun mealListDayCheckSuccess(
        meal_date: String,
        meals: List<MealListDateResponseMeals>
    ) {
        var newDayMealList : ArrayList<MealMainInfo> = arrayListOf()

        val imageList = listOf(R.drawable.item_hamburger_img,
            R.drawable.item_salad_img,
            R.drawable.item_rice_img,
            R.drawable.item_meal_img,
            R.drawable.item_egg_img,
            R.drawable.item_pudding_img,
            R.drawable.item_sugar_img)

        if(meals.size == 0) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            authService = context?.let { AuthService(it) } ?: throw IllegalStateException("Context is null.")
//            val authService = context?.let { AuthService(it) }
            authService?.setHomeMealView(this)
            authService?.mealAddPost(1,dateFormat.format(cal.time))
        } else {
            for(item in meals) {
                var new_meal_no = 1
                for(i in 0 until numList.size) {
                    if(numList[i] == item.meal_type) {
                        new_meal_no = i+1
                    }
                }
                var new_meal_img = imageList.random()
                newDayMealList.add(MealMainInfo(new_meal_no,item.meal_id,item.meal_type,item.meal_kcal,new_meal_img,meal_date,1.0))
            }
        }

        dayMealList = newDayMealList

        adapter = context?.let { DayMealAdapter(dayMealList, it, authService) }
        binding.mainMeallistRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.mainMeallistRv.adapter = adapter

        Log.d("newDayMealList",dayMealList.toString())

//        dayMealList = newDayMealList
//
//        adapter = context?.let { DayMealAdapter(dayMealList, it) }
//        binding.mainMeallistRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//        binding.mainMeallistRv.adapter = adapter
//
//        Log.d("newDayMealList",dayMealList.toString())
//
//        binding.mainMeallistRv.scrollToPosition(dayMealList.size)
//
//        adapter!!.notifyDataSetChanged()
    }

    override fun handleFavoriteFoodResponse(favoriteFoodResponse: List<Food>) {
        TODO("Not yet implemented")
    }

    override fun handleMyMealResponse(myMealResponse: List<mymealResponse>) {
        TODO("Not yet implemented")
    }

    override fun handleMyMealFoodListResponse(myMealFoodListResponse: List<MyMealFoodListResponse>) {
        TODO("Not yet implemented")
    }

    fun convertDateFormat(inputDate: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("MM. dd. EEE", Locale.getDefault())

        try {
            val date = inputDateFormat.parse(inputDate)
            return outputDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }
    override fun SignupFailure(code: Int, msg: String) {
        Toast.makeText(requireContext(),"체중 정보를 받아오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
    }


    //체중 관련
    // 데이터 저장
    fun saveWeightData() {
        val sharedPreferences = context?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()

        // 맵을 JSON 문자열로 변환하여 저장
        val weightDataJson = Gson().toJson(weightDataMap)
        editor.putString(KEY_WEIGHT_DATA, weightDataJson)

        editor.apply()
    }

    // 체중 데이터 추가
    fun addWeightData(convertedDate: String, weight: Float) {
        weightDataMap[convertedDate] = weight
        saveWeightData()
    }

    // 특정 날짜의 체중 데이터 가져오기
    fun getWeightData(convertedDate: String): Float? {
        return weightDataMap[convertedDate]
    }

    // 체중을 받아서 UI에 적용하는 함수
    fun updateWeightOnUI() {
        val select_day = binding.mainDateTv.text.toString()
        val weight = getWeightData(select_day)
        binding.mainWeightTv.text = weight?.toString() ?: "null"
    }

    // 데이터 불러오기
    private fun loadWeightData() {
        val sharedPreferences = requireActivity()!!.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val weightDataJson = sharedPreferences.getString(KEY_WEIGHT_DATA, null)

        // 저장된 데이터가 있다면 맵으로 변환
        if (!weightDataJson.isNullOrEmpty()) {
            val type: Type = object : TypeToken<MutableMap<String, Float>>() {}.type
            weightDataMap.putAll(Gson().fromJson(weightDataJson, type))
        }
    }

    override fun updateWeightOnUI(weight: Float) {
        binding.mainWeightTv.text = weight?.toString() ?: "null"
    }

    override fun MealAddSuccess(meal_id: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        val imageList = listOf(R.drawable.item_hamburger_img,
            R.drawable.item_salad_img,
            R.drawable.item_rice_img,
            R.drawable.item_meal_img,
            R.drawable.item_egg_img,
            R.drawable.item_pudding_img,
            R.drawable.item_sugar_img)

        val authService = AuthService(requireContext())
        authService.setSignupView(this)
        authService.mealListDayCheck(dateFormat.format(cal.time))
    }

    override fun MealAddFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun FoodListAddSuccess() {
        TODO("Not yet implemented")
    }

    override fun FoodListAddFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun FoodListCheckSuccess(
        meal_id: Int,
        meal_date: String,
        meal_type: Int,
        food_list: List<MealFoodResponseFoodList>
    ) {
        TODO("Not yet implemented")
    }

    override fun FoodListCheckFailure() {
        TODO("Not yet implemented")
    }

    //메모리 누수 방지
    override fun onPause() {
        super.onPause()

        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        sharedPref.unregisterOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "nickname") {
                val updatedNickname = sharedPreferences.getString(key, "낄낄")
                binding.mainTitleNicknameTv.text = updatedNickname
            }
        }
    }

}