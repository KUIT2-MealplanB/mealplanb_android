package com.example.mealplanb
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.ActivityMainBinding
import com.example.mealplanb.databinding.FragmentHomeBinding
import com.example.mealplanb.databinding.FragmentMenuRecommendHowMenuBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener {
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

    override fun onResume() {
        super.onResume()

        // SharedPreferences 객체 생성
        val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        // SharedPreferences에서 값 가져오기
        val nickname = sharedPref.getString("nickname", "꿀꿀")
        val avatarImageID = sharedPref.getInt("avatar",3)
        val recommendMenu = sharedPref.getString("recommendMenu", null)
//        val recommendMenuCal = sharedPref.getInt("recommendMenuCal",0)

//        val gson = Gson()
//        val json = sharedPref.getString("MealMainInfo",null)
//        val data: MealMainInfo = gson.fromJson(json, object : TypeToken<MealMainInfo>() {}.type) ?: MealMainInfo(false,-1,0.0,0)

//        Log.d("logcat",data.total_cal.toString()+"!")
//        if(data.meal_active) {
//            dayMealList.set(data.meal_no-1,MealMainInfo(data.meal_active,data.meal_no,data.total_cal,data.meal_img))
//        }

        // 가져온 값 사용
        binding.mainTitleNicknameTv.text = nickname
        when(avatarImageID) {
            1 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.mainCharacterIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }
//        if(recommendMenu != null) {
//            dayMealList.set(0,MealMainInfo(true,1,recommendMenuCal.toDouble(),R.drawable.item_hamburger_img))
//        }
//        binding.mainMeallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        adapter = DayMealAdapter(dayMealList,requireContext())
//        binding.mainMeallistRv.adapter = adapter
//        setProgress()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        dayMealList = arrayListOf()
        dayMealList.addAll(
            arrayListOf(
                MealMainInfo(false,1,0.0,R.drawable.item_hamburger_img)))

        //binding.mainCharacterIv.bringToFront()

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", AppCompatActivity.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("MealMainInfo",null)
        val data: MealMainInfo = gson.fromJson(json, object : TypeToken<MealMainInfo>() {}.type) ?: MealMainInfo(false,-1,0.0,0)

//        val data= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getSerializableExtra("MealMainInfo",MealMainInfo::class.java)
//        } else {
//            intent.getSerializableExtra("MealMainInfo") as MealMainInfo
//        } ?: MealMainInfo(false,-1,-1.0,0)

        if(data.meal_active) {
            dayMealList.set(data.meal_no-1,MealMainInfo(data.meal_active,data.meal_no,data.total_cal,data.meal_img))
        }

        binding.mainMeallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter = DayMealAdapter(dayMealList,requireContext())
        binding.mainMeallistRv.adapter = adapter

        //닉네임 초기값 설정
        binding.mainTitleNicknameTv.text = "꿀꿀"

        calToday.time = Date()
        setDayText()
        setProgress()
        setNutData()

        binding.mainDaymealAddCv.setOnClickListener {
            if(dayMealList.size < 10) {
                dayMealList.add(MealMainInfo(false,dayMealList.size+1,0.0,R.drawable.item_hamburger_img))
                //adapter.notifyItemInserted(dayMealList.size)
                var newItemIdx = dayMealList.size-1
                adapter?.notifyItemInserted(newItemIdx)
            }
            else {
                Toast.makeText(requireContext(),"더 이상 추가할 수 없습니다!",Toast.LENGTH_SHORT).show()
            }
        }
        binding.mainDateTv.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),this,year,month,dayOfMonth)
            datePickerDialog.show()
        }
        binding.mainArrowLeftIv.setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH,-1)
            setDayText()
            setProgress()
            setNutData()
        }
        binding.mainArrowRightIv.setOnClickListener {
            cal.add(Calendar.DAY_OF_MONTH,1)
            setDayText()
            setProgress()
            setNutData()
        }

        binding.mainCharacterIv.setOnClickListener{
            val intent = Intent(requireContext(), NutrientdetailActivity::class.java)
            startActivity(intent)
        }

//        binding.mainScaleCv.setOnClickListener {
//            val intent = Intent(requireActivity(), MenuRecommendActivity::class.java)
//            startActivity(intent)
//        }

        binding.mainMenuIv.setOnClickListener {
            val intent = Intent(requireContext(), HiddenPageActivity::class.java)
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

        setDayText()
        val dayDiff = TimeUnit.MILLISECONDS.toDays(cal1.timeInMillis - cal2.timeInMillis)
        if(dayDiff >= 0) {
            binding.mainTitleContent1Tv.text = "님의 " + (dayDiff + 1).toString() + "일차"
        } else {
            binding.mainTitleContent1Tv.text = "님의 0일차"
        }
    }

    fun setDayText() {
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
        binding.mainDateTv.text = selectedMonth.toString() + ". " + selectedDay.toString() + ". " + dayOfWeek

        setProgress()
        setNutData()
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
}