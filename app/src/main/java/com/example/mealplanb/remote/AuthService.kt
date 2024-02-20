package com.example.mealplanb.remote


import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.mealplanb.ApplicationClass
import com.example.mealplanb.ApplicationClass.Companion.mSharedPreferences
import com.example.mealplanb.HiddenPageActivity
import com.example.mealplanb.LoginPageActivity
import com.example.mealplanb.DayMealAdapter
import com.example.mealplanb.MainActivity
import com.example.mealplanb.local.getJwt
import com.example.mealplanb.local.saveJwt
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign

//retrofit에 service를 요청하고 응답받는 역할을 하는 class
class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var signupView : SignupView
    private lateinit var recommendMealView: RecommendMealView
    private lateinit var planView: PlanView
    private lateinit var homeMealView: HomeMealView
    private lateinit var mealMenuListView: HomeMealView
    private lateinit var recommendMealListView: RecommendMealListView
    private lateinit var recommendMealAmountView: RecommendMealAmountView
    private lateinit var statView: StatView
    private lateinit var statWeightView: StatWeightView
    private lateinit var statKcalView: StatKcalView

    private lateinit var searchFoodView: SearchFoodView
    fun setSignupView(signupView:SignupView) {
        this.signupView = signupView
    }

    fun setRecommendMealView(recommendMealView: RecommendMealView) {
        this.recommendMealView = recommendMealView
    }

    fun setPlanView(planView: PlanView) {
        this.planView = planView
    }

    fun setSearchFoodView(searchFoodView: SearchFoodView){
        this.searchFoodView = searchFoodView
    }

    fun setHomeMealView(homeMealView: HomeMealView) {
        this.homeMealView = homeMealView
    }

    fun setMealMenuListView(mealMenuListView: HomeMealView) {
        this.mealMenuListView = mealMenuListView
    }

    fun setRecommendMealListView(recommendMealListView: RecommendMealListView) {
        this.recommendMealListView = recommendMealListView
    }

    fun setRecommendMealAmountView(recommendMealAmountView: RecommendMealAmountView) {
        this.recommendMealAmountView = recommendMealAmountView
    }

    fun setStatWeightView(statWeightView: StatWeightView) {
        this.statWeightView = statWeightView
    }

    fun setStatView(statView: StatView) {
        this.statView = statView
    }

    fun setStatKcalView(statKcalView: StatKcalView) {
        this.statKcalView = statKcalView
    }

    fun signup(email: String, password: String, sex: String, age: Int,
               height: Int, initial_weight: Float, target_weight: Float, diet_type: String,
               avatar_color: String, nickname: String) {
        signupView.SignupLoading()
        val request = SignupRequest(
            email,
            password,
            sex,
            age,
            height,
            initial_weight,
            target_weight,
            diet_type,
            avatar_color,
            nickname
        )
        authService.signup(request)
            .enqueue(object : Callback<BaseResponse<SignupResponse>> { //enqueue가 Call에서 사용하는 방식
                override fun onResponse(
                    call: Call<BaseResponse<SignupResponse>>,
                    response: Response<BaseResponse<SignupResponse>>
                ) {
                    val resp = response.body()
                    Log.d("Signup response body", resp.toString())
                    saveJwt(resp!!.result.jwt)
                    when (resp!!.code) {
                        1000 -> {
                            signupView.SignupSuccess()
                            checkAvatarInfo()
                        }

                        else -> signupView.SignupFailure(resp.code, resp.message)
                    }

                    val token = getJwt()
                    if (token == null) {
                        Log.d("Signup response token", "getJwt 빔")
                    } else {
                        Log.d("Signup response token", token)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SignupResponse>>, t: Throwable) {
                    Log.d("Signup Failed", t.toString())
                }

            })
    }

    //계정 탈퇴
    fun signOff() {
        authService.signoff().enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                Log.d("SignOff response", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(context, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
                    Log.d("회원탈퇴 정보", response.body().toString())

                    // 회원탈퇴가 성공적으로 이루어졌을 경우, 로그인 액티비티로 이동
                    val intent = Intent(context, LoginPageActivity::class.java)
                    context.startActivity(intent)
                } else {
                    // 서버에서는 응답을 했지만, 회원탈퇴 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<Unit>>() {}.type
                    val errorResponse: BaseResponse<Unit>? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("회원탈퇴 실패 정보", errorResponse.toString())

                    Toast.makeText(context, "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
                    Log.d("회원탈퇴 실패 코드", errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.d("SignOff Failed", t.toString())
                // 회원탈퇴 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "회원탈퇴 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun login(email: String, pw: String) {
        val request = LoginRequest(email, pw)
        authService.login(request).enqueue(object : Callback<BaseResponse<LoginResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<LoginResponse>>,
                response: Response<BaseResponse<LoginResponse>>
            ) {
                Log.d("Login response", response.toString())
                if (response.isSuccessful) { // response의 성공 여부를 확인
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                    Log.d("로그인 정보", response.body().toString())

                    //token 저장
                    val token = response.body()?.result?.jwt.toString()
                    saveJwt(token)
//                    val token2 = getJwt() ?: ""
//                    Log.d("getJwt 확인",token2)

                    // 성공적으로 로그인이 되었을 경우, 다른 액티비티로 이동
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                    //아바타 정보 조회
                    checkAvatarInfo()

                } else {
                    // 서버에서는 응답을 했지만, 로그인 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<LoginResponse>>() {}.type
                    val errorResponse: BaseResponse<LoginResponse>? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("로그인 실패 정보", errorResponse.toString())

                    if (errorResponse?.code.toString() == "5006") {
                        Toast.makeText(context, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("로그인 실패 코드", errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                Log.d("Login Failed", t.toString())
                // 로그인 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun logout() {
        authService.logout().enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                Log.d("Logout response", response.toString())
                if (response.isSuccessful) { // response의 성공 여부를 확인
                    // JWT 토큰 삭제
                    saveJwt("")

                    Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    Log.d("로그아웃 정보", response.body().toString())

                    // 성공적으로 로그아웃이 되었을 경우, 로그인 액티비티로 이동
                    val intent = Intent(context, LoginPageActivity::class.java)
                    context.startActivity(intent)
                } else {
                    // 서버에서는 응답을 했지만, 로그아웃 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<Unit>>() {}.type
                    val errorResponse: BaseResponse<Unit>? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("로그아웃 실패 정보", errorResponse.toString())

                    Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                    Log.d("로그아웃 실패 코드", errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.d("Logout Failed", t.toString())
                // 로그아웃 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "로그아웃 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }


    //avatar
    // 아바타 정보 조회 + UI 반영 메서드
    fun checkAvatarInfo() {
        authService.avatarcheck().enqueue(object : Callback<BaseResponse<AvatarCheckResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<AvatarCheckResponse>>,
                response: Response<BaseResponse<AvatarCheckResponse>>
            ) {
                if (response.isSuccessful) {
                    val avatarInfo = response.body()?.result
                    var avatarImageID = 0
                    if (avatarInfo?.avatar_color == "#FFD3FA") {
                        avatarImageID = 1
                    } else if (avatarInfo?.avatar_color == "#FFFFFF") {
                        avatarImageID = 2
                    } else if (avatarInfo?.avatar_color == "#7C5CF8") {
                        avatarImageID = 3
                    } else if (avatarInfo?.avatar_color == "#220435") {
                        avatarImageID = 4
                    } else if (avatarInfo?.avatar_color == "#D9D9D9") {
                        avatarImageID = 5
                    }
                    Log.d(
                        "아바타 정보",
                        "Avatar appearance: ${avatarInfo?.avatar_appearance}, Avatar color: ${avatarInfo?.avatar_color}, Nickname: ${avatarInfo?.nickname}, ImageID:${avatarImageID}"
                    )

                    // SharedPreferences 객체 생성
                    val sharedPref =
                        context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    // SharedPreferences에 값 저장하기
                    with(sharedPref.edit()) {
                        putString("nickname", avatarInfo?.nickname)
                        putInt("avatar", avatarImageID)
                        apply()
                    }
                    // SharedPreferences에서 값 가져오기
                    val nickname = sharedPref.getString("nickname", "깔깔")
                    val avatar = sharedPref.getInt("avatar", 3)

                } else {
                    Log.e("아바타 오류 정보", "Request not successful. Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<AvatarCheckResponse>>, t: Throwable) {
                Log.e("아바타 정보 서버 오류", "API call failed. Message: ${t.message}")
            }
        })
    }

    //아바타 정보 수정
    fun updateAvatarInfo(nickname: String, avatarColor: String) {
        val avatarData = AvatarData(nickname, avatarColor)
        authService.avatarupdate(avatarData).enqueue(object : Callback<BaseResponse<AvatarUpdateResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<AvatarUpdateResponse>>,
                response: Response<BaseResponse<AvatarUpdateResponse>>
            ) {
                if (response.isSuccessful) {
                    val avatarInfo = response.body()?.result
                    Log.d(
                        "아바타 정보 수정",
                        "Member ID: ${avatarInfo?.member_id}, Nickname: ${avatarInfo?.nickname}, Avatar color: ${avatarInfo?.avatar_color}"
                    )
                } else {
                    Log.e(
                        "아바타 정보 수정 오류",
                        "Request not successful. Message: ${response.message()}"
                    )
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<AvatarUpdateResponse>>,
                t: Throwable
            ) {
                Log.e("아바타 정보 수정 서버 오류", "API call failed. Message: ${t.message}")
            }
        })
    }

    fun updateMyMeal(favorite_meal_name: String, foods: List<FoodList>) {
        val request = mymealData(favorite_meal_name, foods)
        authService.mymealupdate(request).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                Log.d("Meal update response", response.toString())
                if (response.isSuccessful) {
                    // response의 성공 여부를 확인
                    Toast.makeText(context, "식단 등록 성공", Toast.LENGTH_SHORT).show()
                    Log.d("식단 등록 정보", response.body().toString())

//         authService.avatarupdate(avatarData).enqueue(object : Callback<BaseResponse<AvatarUpdateResponse>> {
//             override fun onResponse(
//                 call: Call<BaseResponse<AvatarUpdateResponse>>,
//                 response: Response<BaseResponse<AvatarUpdateResponse>>
//             ) {
//                 if (response.isSuccessful) {
//                     val avatarInfo = response.body()?.result
//                     Log.d(
//                         "아바타 정보 수정",
//                         "Member ID: ${avatarInfo?.member_id}, Nickname: ${avatarInfo?.nickname}, Avatar color: ${avatarInfo?.avatar_color}"
//                     )
                } else {
                    // 서버에서는 응답을 했지만, 등록 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<Unit>>() {}.type
                    val errorResponse: BaseResponse<Unit>? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("식단 등록 실패 정보", errorResponse.toString())

                    // 실패 코드에 따른 메시지를 표시
                    Toast.makeText(context, "식단 등록 실패", Toast.LENGTH_SHORT).show()
                    Log.d("식단 등록 실패 코드", errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.d("Meal update Failed", t.toString())
                // 식단 등록 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "식단 등록 오류", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // 특정 식사 정보 조회
    fun checkFoodDetail(foodId: Int) {
        authService.checkFoodDetail(foodId)
            .enqueue(object : Callback<BaseResponse<GetFoodResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetFoodResponse>>,
                    response: Response<BaseResponse<GetFoodResponse>>
                ) {
                    val resp = response.body()
                    Log.d("food detail check response",resp.toString())
                    when (resp?.code) {
                        1000 -> {
                            // 성공 시 원하는 처리
                            val checkFoodDetailResponse = resp.result
                            val name = checkFoodDetailResponse.name ?: ""
                            val quantity = checkFoodDetailResponse.quantity ?: 0
                            val kcal = checkFoodDetailResponse.kcal ?: 0.0
                            val carbohydrate = checkFoodDetailResponse.carbohydrate ?: 0.0
                            val protein = checkFoodDetailResponse.protein ?: 0.0
                            val fat = checkFoodDetailResponse.fat ?: 0.0
                            val isFavorite = checkFoodDetailResponse.isFavorite ?: false

                            // 정보 전달
                            searchFoodView.FoodDetailSuccess(name,quantity,kcal,carbohydrate,protein,fat,isFavorite)
                            Log.d("food detail check Success", "User Profile Success")
                        }

                        else -> if (resp != null) {
                            Log.d("food detail check error", "User Profile error")
                        }else{
                            Log.d("food detail check null", "User Profile null")
                        }
                    }

//                    Log.d("함수 실행 확인", "success")
//                    if (response.isSuccessful) {
//                        val foodInfo = response.body()?.result
//                        Log.d(
//                            "식사 정보",
//                            "Name: ${foodInfo?.name}, Quantity: ${foodInfo?.quantity}, Kcal: ${foodInfo?.kcal}, Carbohydrate: ${foodInfo?.carbohydrates}, Protein: ${foodInfo?.protein}, Fat: ${foodInfo?.fat}, isFavorite: ${foodInfo?.isFavorite}"
//                        )
                        // 음식 정보를 SharedPreferences에 저장
//                        val sharedPreferences =
//                            context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//                        val editor = sharedPreferences.edit()
//                        val gson = Gson()
//                        val json = gson.toJson(foodInfo)
//                        editor.putString("Key", json)
//                        editor.putString("foodName", foodInfo?.name)
//                        editor.putString("foodQuantity", foodInfo?.quantity?.toString() ?: "0")
//                        editor.putString("foodKcal", foodInfo?.kcal?.toString() ?: "0")
//                        editor.putString(
//                            "foodCarbohydrates",
//                            foodInfo?.carbohydrates?.toString() ?: "0"
//                        )
//                        editor.putString("foodProtein", foodInfo?.protein?.toString() ?: "0")
//                        editor.putString("foodFat", foodInfo?.fat?.toString() ?: "0")
//
//                        // 음식 정보를 SharedPreferences에서 불러오기
//                        val updatefoodName = sharedPreferences.getString("foodName", "")
//                        val updateoriginMealWeight =
//                            sharedPreferences.getString("foodQuantity", "0")?.toDoubleOrNull()
//                                ?: 0.0
//                        val updateoriginkcal =
//                            sharedPreferences.getString("foodKcal", "0")?.toDoubleOrNull() ?: 0.0
//                        val updateoriginSacc =
//                            sharedPreferences.getString("foodCarbohydrates", "0")?.toDoubleOrNull()
//                                ?: 0.0
//                        val updateoriginProtein =
//                            sharedPreferences.getString("foodProtein", "0")?.toDoubleOrNull() ?: 0.0
//                        val updateoriginFat =
//                            sharedPreferences.getString("foodFat", "0")?.toDoubleOrNull() ?: 0.0
//
//                        editor.apply()
//
//                        val foodDeatilCheckResponse = response
//
//                        searchFoodView.FoodDetailSuccess()
//
//                    } else {
//                        Log.e("식사 오류 정보", "Request not successful. Message: ${response.message()}")
//                    }
                }

                override fun onFailure(call: Call<BaseResponse<GetFoodResponse>>, t: Throwable) {
                    Log.e("식사 정보 서버 오류", "API call failed. Message: ${t.message}")
                }
            })
    }
    fun plancheck() {
//        signupView.SignupLoading()

        authService.plancheck().enqueue(object : Callback<BaseResponse<Plan>> {
            override fun onResponse(
                call: Call<BaseResponse<Plan>>,
                response: Response<BaseResponse<Plan>>
            ) {
                val resp = response.body()
                Log.d("plan get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val planCheckResponse = resp.result
                        val initial_weight = planCheckResponse.initial_weight ?: 0.0
                        val target_weight = planCheckResponse.target_weight ?: 0.0
                        val recommended_kcal = planCheckResponse.recommended_kcal ?: 0
                        val diet_type = planCheckResponse.diet_type ?: ""
                        val carbohydrate_rate = planCheckResponse.carbohydrate_rate ?: 0
                        val protein_rate = planCheckResponse.protein_rate ?: 0
                        val fat_rate = planCheckResponse.fat_rate ?: 0
                        val target_kcal = planCheckResponse.target_kcal ?: 0

                        // 정보 전달
                        planView.PlanCheckSuccess(initial_weight,target_weight,recommended_kcal,diet_type,carbohydrate_rate,protein_rate,fat_rate,target_kcal)
                        Log.d("plan get Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("plan get error", "User Profile error")
                    }else{
                        Log.d("plan get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Plan>>, t: Throwable) {
                Log.d("plan get Failed", t.toString())
            }

        })
    }

    fun planupdate(initial_weight: Double,target_weight: Double,diet_type: String,
                   carbohydrate_rate: Int,protein_rate: Int,fat_rate: Int,target_kcal: Int) {
        val request = PlanUpdateRequest(initial_weight,target_weight,diet_type,carbohydrate_rate,protein_rate,fat_rate,target_kcal)
        authService.planupdate(request).enqueue(object : Callback<BaseResponse<Plan>> {
            override fun onResponse(
                call: Call<BaseResponse<Plan>>,
                response: Response<BaseResponse<Plan>>
            ) {
                val resp = response.body()
                Log.d("plan update response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val planUpdateResponse = resp.result
                        val initial_weight = planUpdateResponse.initial_weight ?: 0.0
                        val target_weight = planUpdateResponse.target_weight ?: 0.0
                        val recommended_kcal = planUpdateResponse.recommended_kcal ?: 0
                        val diet_type = planUpdateResponse.diet_type ?: ""
                        val carbohydrate_rate = planUpdateResponse.carbohydrate_rate ?: 0
                        val protein_rate = planUpdateResponse.protein_rate ?: 0
                        val fat_rate = planUpdateResponse.fat_rate ?: 0
                        val target_kcal = planUpdateResponse.target_kcal ?: 0

                        // 정보 전달
                        planView.PlanCheckSuccess(initial_weight,target_weight,recommended_kcal,diet_type,carbohydrate_rate,protein_rate,fat_rate,target_kcal)
                        Log.d("plan update Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("plan update error", "User Profile error")
                    }else{
                        Log.d("plan update null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Plan>>, t: Throwable) {
                Log.d("plan update Failed", "User Profile null")
            }

        })
    }

    fun planDietTypeCheck(type: String) {
//        val request = PlanDietTypeRequest(diet_type)
        authService.planDiettypeCheck(type).enqueue(object : Callback<BaseResponse<PlanDietTypeResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<PlanDietTypeResponse>>,
                response: Response<BaseResponse<PlanDietTypeResponse>>
            ) {
                val resp = response.body()
                Log.d("plan diettype get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val planDietTypeCheckResponse = resp.result
                        val diet_type = planDietTypeCheckResponse.diet_type ?: ""
                        val carbohydrate_rate = planDietTypeCheckResponse.carbohydrate_rate ?: 0
                        val protein_rate = planDietTypeCheckResponse.protein_rate ?: 0
                        val fat_rate = planDietTypeCheckResponse.fat_rate ?: 0

                        // 정보 전달
                        planView.PlanDietTypeCheckSuccess(diet_type,carbohydrate_rate,protein_rate,fat_rate)
                        Log.d("plan diettype get Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("plan diettype get error", "User Profile error")
                    }else{
                        Log.d("plan diettype get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<PlanDietTypeResponse>>, t: Throwable) {
                Log.d("plan diettype get Failed", t.toString())
            }

        })
    }

    fun planRecommKcalCheck(initial_weight: Double,target_weight: Double) {
        authService.planRecommKcalCheck(initial_weight,target_weight).enqueue(object : Callback<BaseResponse<PlanRecommKcalResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<PlanRecommKcalResponse>>,
                response: Response<BaseResponse<PlanRecommKcalResponse>>
            ) {
                val resp = response.body()
                Log.d("plan recommKcal get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val planRecommKcalCheckResponse = resp.result
                        val recommended_kcal = planRecommKcalCheckResponse.recommended_kcal

                        // 정보 전달
                        planView.PlanRecommKcalCheckSuccess(recommended_kcal)
                            Log.d("plan recommKcal get Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("plan recommKcal get error", "User Profile error")
                    }else{
                        Log.d("plan recommKcal get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<PlanRecommKcalResponse>>, t: Throwable) {
                Log.d("plan recommKcal get Failed", t.toString())
            }

        })
    }

    fun userProfileCheck(date: String) {
        authService.userProfileCheck(date).enqueue(object : Callback<BaseResponse<UserProfileResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UserProfileResponse>>,
                response: Response<BaseResponse<UserProfileResponse>>
            ) {
                val resp = response.body()
                Log.d("Home user profile get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val userProfileCheckResponse = resp.result
                        val date = userProfileCheckResponse.date ?: ""
                        val nickname = userProfileCheckResponse.nickname ?: ""
                        val elapsed_days = userProfileCheckResponse.elapsed_days ?: 0
                        val remaining_kcal = userProfileCheckResponse.remaining_kcal ?: 0
                        val avatar_color = userProfileCheckResponse.avatar_color ?: ""
                        val avatar_appearance = userProfileCheckResponse.avatar_appearance ?: ""
                        val target_kcal = userProfileCheckResponse.goal.target_kcal ?: 0
                        val target_carbohydrate = userProfileCheckResponse.goal.target_carbohydrate ?: 0
                        val target_protein = userProfileCheckResponse.goal.target_protein ?: 0
                        val target_fat = userProfileCheckResponse.goal.target_fat ?: 0
                        val kcal = userProfileCheckResponse.intake.kcal ?: 0
                        val carbohydrate = userProfileCheckResponse.intake.carbohydrate ?: 0
                        val protein = userProfileCheckResponse.intake.protein ?: 0
                        val fat = userProfileCheckResponse.intake.fat ?: 0
                        val sodium = userProfileCheckResponse.intake.sodium ?: 0
                        val sugar = userProfileCheckResponse.intake.sugar ?: 0
                        val saturated_fat = userProfileCheckResponse.intake.saturated_fat ?: 0
                        val trans_fat = userProfileCheckResponse.intake.trans_fat ?: 0
                        val cholesterol = userProfileCheckResponse.intake.cholesterol ?: 0

                        // 정보 전달
                        signupView.UserProfileCheckSuccess(date,nickname, elapsed_days, remaining_kcal,
                            avatar_color, avatar_appearance, target_kcal, target_carbohydrate,
                            target_protein, target_fat, kcal, carbohydrate, protein, fat, sodium,
                            sugar, saturated_fat, trans_fat, cholesterol)
                        Log.d("Home user profile get Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("Home user profile get error", "User Profile error")
                    }else{
                        Log.d("Home user profile get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<UserProfileResponse>>, t: Throwable) {
                Log.d("Home user profile get Failed", t.toString())
            }

        })
    }

    fun mealListDayCheck(mealDate: String) {
        authService.mealListDayCheck(mealDate).enqueue(object : Callback<BaseResponse<MealListDateResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MealListDateResponse>>,
                response: Response<BaseResponse<MealListDateResponse>>
            ) {
                val resp = response.body()
                Log.d("Home user meal get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val mealListDayCheckResponse = resp.result
                        val meal_date = mealListDayCheckResponse.meal_date ?: ""
                        val meals = mealListDayCheckResponse.meals

                        // 정보 전달
                        signupView.mealListDayCheckSuccess(meal_date,meals)
                        Log.d("Home user meal get Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("Home user meal get error", "User Profile error")
                    }else{
                        Log.d("Home user meal get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<MealListDateResponse>>, t: Throwable) {
                Log.d("Home user meal get Failed", t.toString())
            }
        })
    }

    //weight
    fun weightcheck() {
        signupView.SignupLoading()

        authService.weightcheck().enqueue(object : Callback<BaseResponse<Weight>> {
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                Log.d("weight get response", resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val weightCheckResponse =
                            resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val weight = weightCheckResponse?.weight ?: 0.0 // 체중 정보 추출
                        val date = weightCheckResponse?.date ?: "" // 날짜 정보 추출

                        // HomeFragment의 SignupSuccess로 정보 전달
                        signupView.WeightcheckSuccess(weight as Float, date)
                        Log.d("weight get Success", "$weight $date")
                    }

                    else -> if (resp != null) {
                        Log.d("weight get error", "User Profile error")
                    } else {
                        Log.d("weight get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight get Failed", t.toString())
            }

        })

    }

    fun weightpost(weight: Float, date: String) {
        signupView.SignupLoading()
        val request = Weight(weight, date)
        authService.weightpost(request).enqueue(object : Callback<BaseResponse<Weight>> {
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val weightCheckResponse =
                            resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val weight = weightCheckResponse?.weight ?: 0.0 // 체중 정보 추출
                        val date = weightCheckResponse?.date ?: "" // 날짜 정보 추출

                        // HomeFragment의 SignupSuccess로 정보 전달
                        signupView.WeightcheckSuccess(weight as Float, date)
                        Log.d("weight post success", resp.toString())
                    }else -> if (resp != null) {
                        signupView.SignupFailure(resp.code,resp.message)
                    }else{
                        Log.d("weight post null",resp.toString())
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight post Failed", t.toString())
            }


        })
    }

    //favorite food
//     fun favoriteFoodGET() {
//         signupView.SignupLoading()
//         authService.favoriteFoodGET()
//             .enqueue(object : Callback<BaseResponse<FavoriteFoodResponse>> {
//                 override fun onResponse(
//                     call: Call<BaseResponse<FavoriteFoodResponse>>,
//                     response: Response<BaseResponse<FavoriteFoodResponse>>
//                 ) {
//                     val resp = response.body()
//                     when (resp?.code) {
//                         1000 -> {
//                             // 성공 시 원하는 처리
//                             val favoriteFoodResponse = resp?.result
//                             favoriteFoodResponse?.foods?.let { foods ->
//                                 for (food in foods) {
//                                     Log.d(
//                                         "Food Item",
//                                         "Food ID: ${food.foodId}, Food Name: ${food.foodName}, Kcal: ${food.kcal}"
//                                     )
//                                 }
//                             }
//                             signupView.handleFavoriteFoodResponse(favoriteFoodResponse)

//                             Log.d("favoriteFood get success", resp.toString())
//                         }

//                         else -> if (resp != null) {
//                             signupView.SignupFailure(resp.code, resp.message)
//                         } else {
//                             Log.d("favoriteFood get null", resp.toString())
//                         }
//                     }
//                 }

//                 override fun onFailure(
//                     call: Call<BaseResponse<FavoriteFoodResponse>>,
//                     t: Throwable
//                 ) {
//                     Log.d("favoriteFood get Failed", t.toString())
//                 }
//             })
//     }

//     fun favoriteFoodPost(food_id: Int) {
//         signupView.SignupLoading()
//         val request = FavoriteFoodRequest(food_id)
//         authService.favoriteFoodPost(request).enqueue(object : Callback<BaseResponse<Unit>> {
    fun weightpatch(weight: Float, date: String){
        signupView.SignupLoading()
        val request = Weight(weight, date)
        authService.weightpatch(request).enqueue(object : Callback<BaseResponse<Weight>>{
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val weightCheckResponse =
                            resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val weight = weightCheckResponse?.weight ?: 0.0 // 체중 정보 추출
                        val date = weightCheckResponse?.date ?: "" // 날짜 정보 추출

                        // HomeFragment의 SignupSuccess로 정보 전달
                        signupView.WeightcheckSuccess(weight as Float, date)
                        Log.d("weight post success", resp.toString())
                    } else -> if (resp != null) {
                    signupView.SignupFailure(resp.code,resp.message)
                }else{
                    Log.d("weight patcht null",resp.toString())
                }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight patcht Failed", t.toString())
            }


        })
    }

    fun mealAddPost(meal_type: Int,meal_date: String){
        val request = MealAddRequest(meal_type,meal_date)
        authService.mealAddPost(request).enqueue(object : Callback<BaseResponse<MealAddResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MealAddResponse>>,
                response: Response<BaseResponse<MealAddResponse>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val mealAddPostResponse = resp.result
                        val meal_id = mealAddPostResponse.meal_id ?: 0

                        homeMealView.MealAddSuccess(meal_id)
                        Log.d("meal add post success", resp.toString())
                    }else -> if (resp != null) {
                        homeMealView.MealAddFailure(resp.code,resp.message)
                    }else{
                        Log.d("meal add post null",resp.toString())
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<MealAddResponse>>, t: Throwable) {
                Log.d("meal add post Failed", t.toString())
            }

        })
    }

    fun foodListAddPost(mealId: Long, foods: List<FoodListAddRequestFoodItem>) {
        val request = FoodListAddRequest(mealId,foods)
        authService.foodListAddPost(request).enqueue(object : Callback<BaseResponse<Any>> {
            override fun onResponse(
                call: Call<BaseResponse<Any>>,
                response: Response<BaseResponse<Any>>
            ) {
                Log.d("foodListAdd request",request.toString())
                Log.d("foodListAdd response",response.toString())
                if(response.isSuccessful) { // response의 성공 여부를 확인
                    Toast.makeText(context, "식단 등록 완료! 홈으로 이동합니다.", Toast.LENGTH_SHORT).show()
                    Log.d("foodListAdd info",response.body().toString())

                } else {
                    // 서버에서는 응답을 했지만, 로그인 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<Any>>() {}.type
                    val errorResponse: BaseResponse<Any>? = gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("foodListAdd info2", errorResponse.toString())

                    if(errorResponse?.code.toString() == "7001" || errorResponse?.code.toString() == "7004"){
                        Toast.makeText(context, errorResponse?.message, Toast.LENGTH_SHORT).show()
                    }
                    Log.d("foodListAdd Failed code",errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                Log.d("foodListAdd Failed", t.toString())
            }

        })
    }
    
    fun foodListCheck(mealId: String) {

        Log.d("Request URL", authService.foodListCheck(mealId).request().url().toString())

        authService.foodListCheck(mealId).enqueue(object : Callback<BaseResponse<MealFoodResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MealFoodResponse>>,
                response: Response<BaseResponse<MealFoodResponse>>
            ) {
                val resp = response.body()
                Log.d("recommend amount get response",response.toString())
                Log.d("recommend amount get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val foodListCheckResponse = resp.result
                        val meal_id = foodListCheckResponse.meal_id ?: 0
                        val meal_date = foodListCheckResponse.meal_date ?: ""
                        val meal_type = foodListCheckResponse.meal_type ?: 0
                        val food_list = foodListCheckResponse.food_list ?: arrayListOf()

                        homeMealView.FoodListCheckSuccess(meal_id,meal_date,meal_type,food_list)
                        Log.d("recommend amount get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("recommend amount get error", "myfavorite meal get error")
                    }else{
                        homeMealView.FoodListCheckFailure()
                        Log.d("recommend amount get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<MealFoodResponse>>, t: Throwable) {
                Log.d("MealfoodList get Failed", t.toString())
            }

        })
    }

    //favorite food
    fun favoriteFoodGET(){
        signupView.SignupLoading()
        authService.favoriteFoodGET().enqueue(object : Callback<BaseResponse<List<Food>>>{
            override fun onResponse(
                call: Call<BaseResponse<List<Food>>>,
                response: Response<BaseResponse<List<Food>>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val favoriteFoodResponse = resp.result
                        signupView.handleFavoriteFoodResponse(favoriteFoodResponse)

                        Log.d("favoriteFood get success", resp.toString())
                    } else -> if (resp != null) {
                    signupView.SignupFailure(resp.code,resp.message)
                }else{
                    Log.d("favoriteFood get null",resp.toString())
                }
                }
            }
            override fun onFailure(call: Call<BaseResponse<List<Food>>>, t: Throwable) {
                Log.d("favoriteFood get Failed", t.toString())
            }
        })
    }
    fun favoriteFoodPost(food_id : Int){
        val request = FavoriteFoodRequest(food_id)
        authService.favoriteFoodPost(request).enqueue(object : Callback<BaseResponse<Unit>>{
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                val resp = response.body()
                Log.d("favoriteFood post success", resp.toString())
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.d("favoriteFood post Failed", t.toString())
            }
        })
    }

    fun favoriteFoodPatch(food_id: Int) {
        // 서버에 PATCH 요청 보내기
        authService.favoriteFoodPatch(food_id).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                val resp = response.body()
                Log.d("favoriteFood patch success", resp.toString())
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.d("favoriteFood patch Failed", t.toString())
            }
        })
    }

    fun searchfood(query: String?, page: Int = 0, callback: (BaseResponse<FoodSearchResponse>?) -> Unit) {
        authService.searchfood(query, page).enqueue(object : Callback<BaseResponse<FoodSearchResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<FoodSearchResponse>>,
                response: Response<BaseResponse<FoodSearchResponse>>
            ) {
                callback(response.body())
                 val resp = response.body()
                 Log.d("searchfood success", resp.toString())
                 // 받아온 데이터 처리
                 when(resp?.code) {
                     1000 -> {
                         Log.d("SearchFoodResponse", "Current Page")
                         val searchFoodResponse = resp?.result
                         searchFoodResponse?.foods?.let { foods ->
                             for (food in foods) {
                                 Log.d("search Food Item", "Food ID: ${food.foodId}, Food Name: ${food.foodName}, Kcal: ${food.kcal}")
                             }
                         }
                         if (searchFoodResponse != null) {
                             searchFoodView.SearchFoodSuccess(searchFoodResponse)

                             // 추가: 서버 응답을 확인하고 로그로 출력
                             Log.d("SearchFoodResponse", "Current Page: ${searchFoodResponse.current_page}, Last Page: ${searchFoodResponse.last_page}")

                             // 응답에 포함된 Food 리스트 출력
                             searchFoodResponse.foods.forEachIndexed { index, food ->
                                 Log.d("SearchFoodResponse", "Food[$index] - Food ID: ${food.foodId}, Food Name: ${food.foodName}, Kcal: ${food.kcal}")
                             }
                         }

                         Log.d("searchFood get success", resp.toString())
                     } else -> if (resp != null) {
                     Log.d("searchFood get error",resp.toString())
                 }else{
                     Log.d("searchFood get null",resp.toString())
                 }
                 }
            }

            override fun onFailure(call: Call<BaseResponse<FoodSearchResponse>>, t: Throwable) {
                Log.d("searchFood Failed", t.toString())
                callback(null)
            }
        })
    }

    fun statKcalDayCheck() {
        authService.statKcalDayCheck().enqueue(object : Callback<BaseResponse<StatKcalDayResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatKcalDayResponse>>,
                response: Response<BaseResponse<StatKcalDayResponse>>
            ) {
                val resp = response.body()
                Log.d("stat weight response",response.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statKcalCheckResponse = resp.result
                        val statistic_type = statKcalCheckResponse.statisticType
                        val kcals = statKcalCheckResponse.kcals
                        statKcalView.StatKcalDayCheckSuccess(statistic_type,kcals)
                        Log.d("stat weight get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat weight get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat weight get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatKcalDayResponse>>, t: Throwable) {
                Log.d("stat weight get Failed", t.toString())
            }

        })
    }

    fun statKcalWeekCheck() {
        authService.statKcalWeekCheck().enqueue(object : Callback<BaseResponse<StatKcalWeekResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatKcalWeekResponse>>,
                response: Response<BaseResponse<StatKcalWeekResponse>>
            ) {
                val resp = response.body()
                Log.d("stat weight response",response.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statKcalCheckResponse = resp.result
                        val statistic_type = statKcalCheckResponse.statisticType
                        val kcals = statKcalCheckResponse.kcals
                        statKcalView.StatKcalWeekCheckSuccess(statistic_type,kcals)
                        Log.d("stat weight get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat weight get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat weight get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatKcalWeekResponse>>, t: Throwable) {
                Log.d("stat weight get Failed", t.toString())
            }

        })
    }

    fun statKcalMonthCheck() {
        authService.statKcalMonthCheck().enqueue(object : Callback<BaseResponse<StatKcalMonthResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatKcalMonthResponse>>,
                response: Response<BaseResponse<StatKcalMonthResponse>>
            ) {
                val resp = response.body()
                Log.d("stat weight response",response.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statKcalCheckResponse = resp.result
                        val statistic_type = statKcalCheckResponse.statisticType
                        val kcals = statKcalCheckResponse.kcals
                        statKcalView.StatKcalMonthCheckSuccess(statistic_type,kcals)
                        Log.d("stat weight get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat weight get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat weight get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatKcalMonthResponse>>, t: Throwable) {
                Log.d("stat weight get Failed", t.toString())
            }

        })
    }

    fun statWeightDayCheck() {
        authService.statWeightDayCheck().enqueue(object : Callback<BaseResponse<StatWeightDayResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatWeightDayResponse>>,
                response: Response<BaseResponse<StatWeightDayResponse>>
            ) {
                val resp = response.body()
                Log.d("stat weight response",response.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statWeightCheckResponse = resp.result
                        val statistic_type = statWeightCheckResponse.statisticType
                        val weights = statWeightCheckResponse.weights
                        statWeightView.StatWeightDayCheckSuccess(statWeightCheckResponse.statisticType,weights)
                        Log.d("stat weight get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat weight get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat weight get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatWeightDayResponse>>, t: Throwable) {
                Log.d("stat weight get Failed", t.toString())
            }

        })
    }

    fun statWeightWeekCheck() {
        authService.statWeightWeekCheck().enqueue(object : Callback<BaseResponse<StatWeightWeekResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatWeightWeekResponse>>,
                response: Response<BaseResponse<StatWeightWeekResponse>>
            ) {
                val resp = response.body()
                Log.d("stat weight response",response.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statWeightCheckResponse = resp.result
                        val statistic_type = statWeightCheckResponse.statisticType
                        val weights = statWeightCheckResponse.weights
                        statWeightView.StatWeightWeekCheckSuccess(statWeightCheckResponse.statisticType,weights)
                        Log.d("stat weight get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat weight get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat weight get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatWeightWeekResponse>>, t: Throwable) {
                Log.d("stat weight get Failed", t.toString())
            }

        })
    }

    fun statWeightMonthCheck() {
        authService.statWeightMonthCheck().enqueue(object : Callback<BaseResponse<StatWeightMonthResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatWeightMonthResponse>>,
                response: Response<BaseResponse<StatWeightMonthResponse>>
            ) {
                val resp = response.body()
                Log.d("stat weight response",response.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statWeightCheckResponse = resp.result
                        val statistic_type = statWeightCheckResponse.statisticType
                        val weights = statWeightCheckResponse.weights
                        statWeightView.StatWeightMonthCheckSuccess(statWeightCheckResponse.statisticType,weights)
                        Log.d("stat weight get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat weight get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat weight get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatWeightMonthResponse>>, t: Throwable) {
                Log.d("stat weight get Failed", t.toString())
            }

        })
    }

    fun statPlanCheck() {
        authService.statPlanCheck().enqueue(object : Callback<BaseResponse<StatPlanResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatPlanResponse>>,
                response: Response<BaseResponse<StatPlanResponse>>
            ) {
                val resp = response.body()
                Log.d("stat plan response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val statPlanCheckResponse = resp.result
                        val initial_weight = statPlanCheckResponse.initial_weight ?: 0.0
                        val target_weight = statPlanCheckResponse.target_weight ?: 0.0
                        val diet_type = statPlanCheckResponse.diet_type ?: ""

                        statView.StatPlanCheckSuccess(initial_weight,target_weight,diet_type)
                        Log.d("stat plan get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("stat plan get error", "myfavorite meal get error")
                    }else{
                        Log.d("stat plan get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatPlanResponse>>, t: Throwable) {
                Log.d("stat plan get Failed", t.toString())
            }

        })
    }

    fun cheatMealCheck(category: String) {
        authService.cheatMealCheck(category).enqueue(object : Callback<BaseResponse<CheatDayRecommendMeal>> {
            override fun onResponse(
                call: Call<BaseResponse<CheatDayRecommendMeal>>,
                response: Response<BaseResponse<CheatDayRecommendMeal>>
            ) {
                val resp = response.body()
                Log.d("cheat get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val cheatMealCheckResponse = resp.result
                        val cheat_day_food = cheatMealCheckResponse.cheat_day_food ?: arrayListOf()

                        recommendMealView.cheatMealCheckSuccess(cheat_day_food)
                        Log.d("cheat meal get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("cheat meal get error", "myfavorite meal get error")
                    }else{
                        Log.d("cheat meal get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<CheatDayRecommendMeal>>,
                t: Throwable
            ) {
                Log.d("cheat meal get Failed", t.toString())
            }

        })
    }

    fun myfavoriteMealCheck() {
        authService.myfavoriteMealCheck().enqueue(object : Callback<BaseResponse<ChatRecommendMeal>>{
            override fun onResponse(
                call: Call<BaseResponse<ChatRecommendMeal>>,
                response: Response<BaseResponse<ChatRecommendMeal>>
            ) {
                val resp = response.body()
                Log.d("weight get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val myfavoriteMealCheckResponse = resp.result
                        val food_id = myfavoriteMealCheckResponse?.food_id ?: 0
                        val name = myfavoriteMealCheckResponse?.name ?: ""
                        val offer = myfavoriteMealCheckResponse?.offer ?: ""
                        val offer_quantity = myfavoriteMealCheckResponse.quantity ?: 0
                        val offer_carbohydrate = myfavoriteMealCheckResponse?.offer_carbohydrate ?: 0
                        val offer_protein = myfavoriteMealCheckResponse?.offer_protein ?: 0
                        val offer_fat = myfavoriteMealCheckResponse?.offer_fat ?: 0

                        recommendMealView.myFavoriteMealCheckSuccess(food_id, name, offer, offer_quantity, offer_carbohydrate, offer_protein, offer_fat)
                        Log.d("myfavorite meal get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("myfavorite meal get error", "myfavorite meal get error")
                    }else{
                        Log.d("myfavorite meal get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<ChatRecommendMeal>>, t: Throwable) {
                Log.d("myfavorite meal get Failed", t.toString())
            }

        })

    }

    fun communityfavoriteMealCheck() {
        authService.communityfavoriteMealCheck().enqueue(object : Callback<BaseResponse<ChatRecommendMeal>>{
            override fun onResponse(
                call: Call<BaseResponse<ChatRecommendMeal>>,
                response: Response<BaseResponse<ChatRecommendMeal>>
            ) {
                val resp = response.body()
                Log.d("communityfavorite meal get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val communityfavoriteMealCheckResponse = resp.result
                        val food_id = communityfavoriteMealCheckResponse?.food_id ?: 0
                        val name = communityfavoriteMealCheckResponse?.name ?: ""
                        val offer = communityfavoriteMealCheckResponse?.offer ?: ""
                        val offer_quantity = communityfavoriteMealCheckResponse.quantity ?: 0
                        val offer_carbohydrate = communityfavoriteMealCheckResponse?.offer_carbohydrate ?: 0
                        val offer_protein = communityfavoriteMealCheckResponse?.offer_protein ?: 0
                        val offer_fat = communityfavoriteMealCheckResponse?.offer_fat ?: 0

                        recommendMealView.communityFavoiriteMealCheckSuccess(food_id, name, offer, offer_quantity, offer_carbohydrate, offer_protein, offer_fat)
                        Log.d("communityfavorite meal get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("communityfavorite meal get error", "myfavorite meal get error")
                    }else{
                        Log.d("communityfavorite meal get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<ChatRecommendMeal>>, t: Throwable) {
                Log.d("communityfavorite meal get Failed", t.toString())
            }

        })

    }

    fun recommendMealRegist(food_id: Long, quantity: Int) {
        val request = ChatMealRequest(food_id,quantity)
        authService.recommendMealRegist(request).enqueue(object : Callback<BaseResponse<Any>> {
            override fun onResponse(
                call: Call<BaseResponse<Any>>,
                response: Response<BaseResponse<Any>>
            ) {
                Log.d("recommendMeal regist response",response.toString())
                if(response.isSuccessful) { // response의 성공 여부를 확인
                    Toast.makeText(context, "식단 등록 완료! 5초 후 홈으로 이동합니다.", Toast.LENGTH_SHORT).show()
                    Log.d("recommendMeal regist info",response.body().toString())

                } else {
                    // 서버에서는 응답을 했지만, 로그인 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<Any>>() {}.type
                    val errorResponse: BaseResponse<Any>? = gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("recommendMeal regist info2", errorResponse.toString())

                    if(errorResponse?.code.toString() == "7006"){
                        Toast.makeText(context, errorResponse?.message, Toast.LENGTH_SHORT).show()
                    }
                    Log.d("recommendMeal regist Failed code",errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                Log.d("recommendMeal regist Failed", t.toString())
            }

        })
    }

    fun recommendMealCheck() {
        authService.recommendMealCheck().enqueue(object : Callback<BaseResponse<List<ChatMealListResponse>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<ChatMealListResponse>>>,
                response: Response<BaseResponse<List<ChatMealListResponse>>>
            ) {
                val resp = response.body()
                Log.d("recommend meal get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val recommendMealCheckResponse = resp.result

                        recommendMealListView.recommendMealListCheckSuccess(recommendMealCheckResponse)
                        Log.d("recommend meal get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("recommend meal get error", "myfavorite meal get error")
                    }else{
                        Log.d("recommend meal get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<ChatMealListResponse>>>,
                t: Throwable
            ) {
                Log.d("recommend meal get Failed", t.toString())
            }

        })
    }

    fun recommendMealAmountCheck(foodId: String) {
        authService.recommendMealAmountCheck(foodId).enqueue(object : Callback<BaseResponse<ChatMealAmountResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<ChatMealAmountResponse>>,
                response: Response<BaseResponse<ChatMealAmountResponse>>
            ) {
                val resp = response.body()
                Log.d("recommend amount get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val recommendMealCheckResponse = resp.result
                        val food_name = recommendMealCheckResponse.food_name ?: ""
                        val offer = recommendMealCheckResponse.offer ?: ""
                        val offer_kcal = recommendMealCheckResponse.offer_kcal ?: 0
                        val offer_quantity = recommendMealCheckResponse.offer_quantity ?: 0
                        val offer_carbohydrate = recommendMealCheckResponse.offer_carbohydrate ?: 0
                        val offer_protein = recommendMealCheckResponse.offer_protein ?: 0
                        val offer_fat = recommendMealCheckResponse.offer_fat ?: 0
                        val remaining_kcal = recommendMealCheckResponse.remaining_kcal ?: 0

                        recommendMealAmountView.mealAmountCheckSuccess(food_name, offer, offer_kcal, offer_quantity, offer_carbohydrate, offer_protein, offer_fat, remaining_kcal)
                        Log.d("recommend amount get Success", "myfavorite meal get Success")
                    }

                    else -> if (resp != null) {
                        Log.d("recommend amount get error", "myfavorite meal get error")
                    }else{
                        Log.d("recommend amount get null", "myfavorite meal get null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<ChatMealAmountResponse>>, t: Throwable) {
                Log.d("recommend amount get Failed", t.toString())
            }

        })
    }

}