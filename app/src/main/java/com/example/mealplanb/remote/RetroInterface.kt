package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetroInterface {

    //회원가입
    @POST("user/signup")
    fun signup(
        @Body request: SignupRequest //내부에 포함할 데이터
    ) : Call<BaseResponse<SignupResponse>> //반환할 데이터

    //로그인
    @POST("user/login")
    fun login(
        @Body request: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    //아바타 정보 조회
    @GET("user/avatar")
    fun avatarcheck(): Call<BaseResponse<AvatarCheckResponse>>

    //아바타 정보 수정
    @PATCH("user/avatar")
    fun avatarupdate(
        @Body request: AvatarData
    ):Call<BaseResponse<AvatarUpdateResponse>>
  
    @GET("user/plan")
    fun plancheck(): Call<BaseResponse<Plan>>
    @PATCH("user/plan")
    fun planupdate(
        @Body request: PlanUpdateRequest
    ): Call<BaseResponse<Plan>>
    @GET("user/plan/diet-type")
    fun planDiettypeCheck(
        //@Body request: PlanDietTypeRequest
        @Query("type") type: String
    ): Call<BaseResponse<PlanDietTypeResponse>>
    @GET("user/plan/recommended-kcal")
    fun planRecommKcalCheck(
        @Query("initial_weight") initial_weight: Double,
        @Query("target_weight") target_weight: Double
    ): Call<BaseResponse<PlanRecommKcalResponse>>

    @GET("user/profile")
    fun userProfileCheck(
        @Query("mealDate") mealDate: String
    ): Call<BaseResponse<UserProfileResponse>>

    @GET("meal")
    fun mealListDayCheck(
        @Query("mealDate") mealDate: String
    ): Call<BaseResponse<MealListDateResponse>>

    // 특정 식사 정보 조회
    @GET("food/{foodId}")
    fun checkFoodDetail(@Path("foodId") foodId: Int): Call<BaseResponse<GetFoodResponse>>

    //나의 식단 등록
    @POST("/my-meal")
    fun mymealupdate(
        @Body request: mymealData
    ):Call<BaseResponse<Unit>>

    //Weight
    @GET("weight")
    fun weightcheck(): Call<BaseResponse<Weight>>
    @POST("weight")
    fun weightpost(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>
    @PATCH("weight")
    fun weightpatch(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>

    @POST("meal")
    fun mealAddPost(
        @Body request: MealAddRequest
    ): Call<BaseResponse<MealAddResponse>>
    @POST("meal/food")
    fun foodListAddPost(
        @Body request: FoodListAddRequest
    ): Call<BaseResponse<Any>>
    @GET("meal/{mealId}/food")
    fun foodListCheck(
        @Path("mealId") mealId: String
    ): Call<BaseResponse<MealFoodResponse>>

    //favorite food
    @GET("favorite-food")
    fun favoriteFoodGET():Call<BaseResponse<FavoriteFoodResponse>>

    @POST("favorite-food")
    fun favoriteFoodPost(
        @Body request : FavoriteFoodRequest
    ):Call<BaseResponse<Unit>>

    @PATCH("favorite-food/{foodId}")
    fun favoriteFoodPatch(
        @Path("foodId") foodId: Int,
    ):Call<BaseResponse<Unit>>

    @GET("food/auto-complete")
    fun searchfood(
        @Query("query") query: String?,
        @Query("page") page: Int = 0
    ):Call<BaseResponse<FoodSearchResponse>>

//    @GET("weight/{statisticType}")
//    fun statWeightCheck(
//        @Path("statisticType") statistictype: String
//    ): Call<BaseResponse<StatWeightResponse>>
    @GET("food-history/daily")
    fun statKcalDayCheck(): Call<BaseResponse<StatKcalDayResponse>>
    @GET("food-history/weekly")
    fun statKcalWeekCheck(): Call<BaseResponse<StatKcalWeekResponse>>
    @GET("weight/daily")
    fun statWeightDayCheck(): Call<BaseResponse<StatWeightDayResponse>>
    @GET("weight/weekly")
    fun statWeightWeekCheck(): Call<BaseResponse<StatWeightWeekResponse>>
    @GET("weight/monthly")
    fun statWeightMonthCheck(): Call<BaseResponse<StatWeightMonthResponse>>
    @GET("statistic/plan")
    fun statPlanCheck(): Call<BaseResponse<StatPlanResponse>>

    @GET("chat/cheat-day")
    fun cheatMealCheck(
        @Query("category") category: String
    ): Call<BaseResponse<CheatDayRecommendMeal>>
    @GET("chat/my-favorite")
    fun myfavoriteMealCheck(): Call<BaseResponse<ChatRecommendMeal>>
    @GET("chat/community-favorite")
    fun communityfavoriteMealCheck(): Call<BaseResponse<ChatRecommendMeal>>
    @POST("chat/meal")
    fun recommendMealRegist(
        @Body request: ChatMealRequest
    ): Call<BaseResponse<Any>>
    @GET("chat/meal")
    fun recommendMealCheck(): Call<BaseResponse<List<ChatMealListResponse>>>
    @GET("chat/amount-suggestion/{foodId}")
    fun recommendMealAmountCheck(
        @Path("foodId") foodId: String
    ): Call<BaseResponse<ChatMealAmountResponse>>
}