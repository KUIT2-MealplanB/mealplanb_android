package com.example.mealplanb.remote


import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.mealplanb.ApplicationClass
import com.example.mealplanb.MainActivity
import com.example.mealplanb.local.getJwt
import com.example.mealplanb.local.saveJwt
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//retrofit에 service를 요청하고 응답받는 역할을 하는 class
class AuthService(private val context: Context) {
    private val authService = ApplicationClass.retrofit.create(RetroInterface::class.java)

    private lateinit var signupView : SignupView
    private lateinit var recommendMealView: RecommendMealView
    private lateinit var planView: PlanView

    fun setSignupView(signupView: SignupView) {
        this.signupView = signupView
    }

    fun setRecommendMealView(recommendMealView: RecommendMealView) {
        this.recommendMealView = recommendMealView
    }

    fun setPlanView(planView: PlanView) {
        this.planView = planView
    }

    fun signup(email: String, password: String, sex: String, age: Int,
               height: Int, initial_weight: Float, target_weight: Float, diet_type: String,
               avatar_color: String, nickname: String) {
        signupView.SignupLoading()
        val request = SignupRequest(email,password,sex,age,height,initial_weight,target_weight,diet_type,avatar_color,nickname)
        authService.signup(request).enqueue(object : Callback<BaseResponse<SignupResponse>>{ //enqueue가 Call에서 사용하는 방식
            override fun onResponse(
                call: Call<BaseResponse<SignupResponse>>,
                response: Response<BaseResponse<SignupResponse>>
            ) {
                val resp = response.body()
                Log.d("Signup response body",resp.toString())
                saveJwt(resp!!.result.jwt)
                when(resp!!.code) {
                    1000 -> signupView.SignupSuccess()
                    else -> signupView.SignupFailure(resp.code,resp.message)
                }

                val token = getJwt()
                if (token == null) {
                    Log.d("Signup response token","getJwt 빔")
                } else {
                    Log.d("Signup response token", token)
                }
            }

            override fun onFailure(call: Call<BaseResponse<SignupResponse>>, t: Throwable) {
                Log.d("Signup Failed",t.toString())
            }

        })
    }

    fun login(email: String,pw: String){
        val request = LoginRequest(email,pw)
        authService.login(request).enqueue(object: Callback<BaseResponse<LoginResponse>>{
            override fun onResponse(
                call: Call<BaseResponse<LoginResponse>>,
                response: Response<BaseResponse<LoginResponse>>
            ) {
                Log.d("Login response",response.toString())
                if(response.isSuccessful) { // response의 성공 여부를 확인
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                    Log.d("로그인 정보",response.body().toString())

                    //token 저장
                    val token = response.body()?.result?.jwt.toString()
                    saveJwt(token)
//                    val token2 = getJwt() ?: ""
//                    Log.d("getJwt 확인",token2)

                    // 성공적으로 로그인이 되었을 경우, 다른 액티비티로 이동
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                } else {
                    // 서버에서는 응답을 했지만, 로그인 실패와 같은 이유로 성공적인 응답이 아닌 경우, Toast 메시지
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponse<LoginResponse>>() {}.type
                    val errorResponse: BaseResponse<LoginResponse>? = gson.fromJson(response.errorBody()?.charStream(), type)
                    Log.d("로그인 실패 정보", errorResponse.toString())

                    if(errorResponse?.code.toString() == "5006"){
                        Toast.makeText(context, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("로그인 실패 코드",errorResponse?.code.toString())
                }
            }

            override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                Log.d("Login Failed",t.toString())
                // 로그인 요청 자체가 실패한 경우, Toast 메시지
                Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show()
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

    //weight
    fun weightcheck(){
        signupView.SignupLoading()

        authService.weightcheck().enqueue(object : Callback<BaseResponse<Weight>> {
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                Log.d("weight get response",resp.toString())
                when (resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val weightCheckResponse =
                            resp.result // WeightCheckResponse 클래스에 따라 result에 응답 데이터가 있을 것이라 가정합니다
                        val weight = weightCheckResponse?.weight ?: 0.0 // 체중 정보 추출
                        val date = weightCheckResponse?.date ?: "" // 날짜 정보 추출

                        // HomeFragment의 SignupSuccess로 정보 전달
                        signupView.WeightcheckSuccess(weight as Float, date)
                        Log.d("weight get Success", "User Profile Success")
                    }

                    else -> if (resp != null) {
                        Log.d("weight get error", "User Profile error")
                    }else{
                        Log.d("weight get null", "User Profile null")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Weight>>, t: Throwable) {
                Log.d("weight get Failed", t.toString())
            }

        })

    }

    fun weightpost(weight: Float, date: String){
        signupView.SignupLoading()
        val request = Weight(weight, date)
        authService.weightpost(request).enqueue(object : Callback<BaseResponse<Weight>>{
            override fun onResponse(
                call: Call<BaseResponse<Weight>>,
                response: Response<BaseResponse<Weight>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> Log.d("weight post success",resp.toString()) //아직 layout이 구현안되어 있어서 Homefragment에 대신 구현
                    else -> if (resp != null) {
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
                        val offer_quantity = myfavoriteMealCheckResponse.offer_quantity ?: 0
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
                        val offer_quantity = communityfavoriteMealCheckResponse.offer_quantity ?: 0
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
}