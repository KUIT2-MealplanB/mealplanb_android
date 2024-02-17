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
import com.example.mealplanb.remote.SignupView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener, SignupView {
    lateinit var binding : FragmentHomeBinding
    lateinit var dayMealList : ArrayList<MealMainInfo>
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
    private var weight_check_date : String? = null
    private var weight_check_kg : Float? = null

    // 날짜와 체중 데이터를 저장할 맵
    private val weightDataMap: MutableMap<String, Float> = mutableMapOf()


    override fun onResume() {
        super.onResume()

        // SharedPreferences 객체 생성
        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences에서 값 가져오기
        val nickname = sharedPref.getString("nickname", "꿀꿀")
        val avatarImageID = sharedPref.getInt("avatar",3)

        val gson = Gson()
        var json = sharedPref.getString("dayMealList",null)
        dayMealList = gson.fromJson(json,object : TypeToken<ArrayList<MealMainInfo>>() {}.type) ?: arrayListOf(
            MealMainInfo(false,1,0.0,0,"", 0.0)
        )

        binding.mainMeallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter = DayMealAdapter(dayMealList,requireContext())
        binding.mainMeallistRv.adapter = adapter

//        binding.mainMeallistRv.isNestedScrollingEnabled = false
//        binding.mainMeallistRv.overScrollMode = View.OVER_SCROLL_ALWAYS
//        binding.mainMeallistSv.isFillViewport = true

        // 가져온 값 사용
        binding.mainTitleNicknameTv.text = nickname
        when(avatarImageID) {
            1 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }

        //끼니 slot 추가
        binding.mainDaymealAddCv.setOnClickListener {
            if(dayMealList.size < 10) {
                dayMealList.add(MealMainInfo(false,dayMealList.size+1,0.0,R.drawable.item_hamburger_img,"", 0.0))
                var newItemIdx = dayMealList.size-1
                adapter?.notifyItemInserted(newItemIdx)

                val editor = sharedPref.edit()
                val newJson = gson.toJson(dayMealList)
                editor.putString("dayMealList",newJson)
                editor.apply()
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
        dayMealList.addAll(
            arrayListOf(
                MealMainInfo(false,1,0.0,R.drawable.item_hamburger_img,"", 0.0)))

        //캐릭터 tab하면 상세 영양소 페이지로 연결
        binding.mainCharacterIv.setOnClickListener{
            val intent = Intent(requireContext(), NutrientdetailActivity::class.java)
            startActivity(intent)
        }
        //햄버거 아이콘 tab하면 히든 페이지로 연결
        binding.mainMenuIv.setOnClickListener {
            val intent = Intent(requireContext(), HiddenPageActivity::class.java)
            startActivity(intent)
        }

        //오늘의 체중 정보
        val sharedPref = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val todayweight = sharedPref?.getFloat("startWeight",0.0f)
        //binding.mainWeightTv.text="$todayweight"

        //오늘의 무게 클릭했을 때 돌아가는 애니메이션
        binding.mainDayweightContentCv.setOnClickListener {
            val rotateAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_180)
            val rotateAnimOp = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_180_opposite)
            binding.todayWeightIv1.startAnimation(rotateAnim)
            binding.todayWeightIv2.startAnimation(rotateAnimOp)
        }

        //API관련
        val authService = AuthService(requireContext())
        authService.setSignupView(this)
        authService.weightpost(32.5F, "2024-01-09")
        authService.weightpatch(32.5F, "2024-01-010")
        authService.weightcheck()


        binding.mainAlarmIv.setOnClickListener {
            val intent = Intent(requireContext(), AlarmActivity::class.java)
            startActivity(intent)
        }

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

        val dayDiff = TimeUnit.MILLISECONDS.toDays(cal1.timeInMillis - cal2.timeInMillis)
        if(dayDiff >= 0) {
            binding.mainTitleContent1Tv.text = "님의 " + (dayDiff + 1).toString() + "일차"
        } else {
            binding.mainTitleContent1Tv.text = "님의 0일차"
        }
    }

    fun setHomeData() {
        binding.mainDateTv.text = setDayText()
        setProgress()
        setNutData()
        // 체중 UI 업데이트
        updateWeightOnSelectedDate()
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

    fun setNutData() {
        //일일 총 영양소 섭취량과 실 영양소 섭취량에 대한 더미 데이터 설정
        saccDayTot = 500
        proteinDayTot = 700
        fatDayTot = 300
        saccToday = 15 * cal.get(Calendar.DAY_OF_MONTH)
        proteinToday = 20 * cal.get(Calendar.DAY_OF_MONTH)
        fatToday = 9 * cal.get(Calendar.DAY_OF_MONTH)

        binding.mainSaccSizeTv.text = saccToday.toString()
        binding.mainProteinSizeTv.text = proteinToday.toString()
        binding.mainFatSizeTv.text = fatToday.toString()
        binding.mainSaccTotalTv.text = "/" + saccDayTot.toString() + "g"
        binding.mainProteinTotalTv.text = "/" + proteinDayTot.toString() + "g"
        binding.mainFatTotalTv.text = "/" + fatDayTot.toString() + "g"

        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences.Editor 객체를 얻어서 값을 저장
        val editor = sharedPref.edit()
        editor.putInt("saccDayTot",saccDayTot!!)
        editor.putInt("proteinDayTot",proteinDayTot!!)
        editor.putInt("fatDayTot",fatDayTot!!)
        editor.putInt("saccToday",saccToday!!)
        editor.putInt("proteinToday",proteinToday!!)
        editor.putInt("fatToday",fatToday!!)
        editor.apply()
    }

    fun setProgress() {
        //progress 값 및 남은 칼로리 설정
        goalCal = binding.mainProgressbgPb.getText()?.toInt()
        nowCal = adapter!!.totCal()
        var progress = nowCal!! * 100 / goalCal!!
        binding.mainProgressPb.updateProgress(progress)
        binding.mainProgressPb.setText(nowCal.toString())
        binding.mainTitleCalTv.text = (goalCal!! - nowCal!!).toString() + "kcal"

        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences.Editor 객체를 얻어서 값을 저장
        val editor = sharedPref.edit()
        editor.putInt("goalCal",goalCal!!)
        editor.putInt("nowCal",nowCal!!)
        editor.apply()
    }

    override fun SignupLoading() {
    }

    override fun SignupSuccess() {
    }

    override fun WeightcheckSuccess(weight: Float, date: String) {

        // 오늘 날짜 날짜를 가져옴
        val Today = setDayText()

        //입력받은 날짜 형식 바꿔주기
        val convertedDate = convertDateFormat(date)

        //받은 날짜가 오늘이면
        if(Today == convertedDate){
            binding.mainWeightTv.text = date
        }

        // 날짜와 체중을 맵에 저장
        weightDataMap[convertedDate] = weight
        Log.d("받은 weight", weight.toString())
        Log.d("받은 날짜", setDayText())

    }

    // 선택한 날짜에 대한 체중 정보를 UI에 업데이트하는 함수
    private fun updateWeightOnSelectedDate() {
        // UI에 표시할 날짜를 가져옴
        val selectDay = setDayText()
        Log.d("선택한 날짜", selectDay)

        // 선택한 날짜에 해당하는 체중을 맵에서 찾음
        val weightForSelectedDate = weightDataMap[selectDay]
        Log.d("찾은 weight", weightForSelectedDate.toString())

        // 찾은 체중이 null이 아니라면 UI에 업데이트
        weightForSelectedDate?.let {
            binding.mainWeightTv.text = it.toString()
        } ?: run {
            // null이면 toast 메시지 표시
            //Toast.makeText(requireContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show()

            //null 값일때 default 값으로 넣기
            binding.mainWeightTv.text = 0.0f.toString()
        }
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
        Toast.makeText(requireContext(),"weight정보를 받아오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
    }
}