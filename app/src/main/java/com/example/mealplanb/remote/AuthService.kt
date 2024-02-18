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
    private lateinit var searchFoodView: SearchFoodView
    fun setSignupView(signupView:SignupView) {
        this.signupView = signupView
    }

    fun setSearchFoodView(searchFoodView: SearchFoodView){
        this.searchFoodView = searchFoodView
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
                        Log.d("weight get Success", "$weight $date")
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

    //favorite food
    fun favoriteFoodGET(){
        signupView.SignupLoading()
        authService.favoriteFoodGET().enqueue(object : Callback<BaseResponse<FavoriteFoodResponse>>{
            override fun onResponse(
                call: Call<BaseResponse<FavoriteFoodResponse>>,
                response: Response<BaseResponse<FavoriteFoodResponse>>
            ) {
                val resp = response.body()
                when(resp?.code) {
                    1000 -> {
                        // 성공 시 원하는 처리
                        val favoriteFoodResponse = resp?.result
                        favoriteFoodResponse?.foods?.let { foods ->
                            for (food in foods) {
                                Log.d("Food Item", "Food ID: ${food.foodId}, Food Name: ${food.foodName}, Kcal: ${food.kcal}")
                            }
                        }
                        signupView.handleFavoriteFoodResponse(favoriteFoodResponse)

                        Log.d("favoriteFood get success", resp.toString())
                    } else -> if (resp != null) {
                    signupView.SignupFailure(resp.code,resp.message)
                }else{
                    Log.d("favoriteFood get null",resp.toString())
                }
                }
            }
            override fun onFailure(call: Call<BaseResponse<FavoriteFoodResponse>>, t: Throwable) {
                Log.d("favoriteFood get Failed", t.toString())
            }
        })
    }
    fun favoriteFoodPost(food_id : Int){
        signupView.SignupLoading()
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
    fun searchfood(query: String?, page: Int = 0) {
        authService.searchfood(query, page).enqueue(object : Callback<BaseResponse<FoodSearchResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<FoodSearchResponse>>,
                response: Response<BaseResponse<FoodSearchResponse>>
            ) {
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
            }
        })
    }


}