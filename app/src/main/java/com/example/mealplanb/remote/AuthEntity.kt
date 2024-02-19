package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    //키 값 지정
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("sex") val sex : String,
    @SerializedName("age") val age : Int,
    @SerializedName("height") val height : Int,
    @SerializedName("initial_weight") val initial_weight : Float,
    @SerializedName("target_weight") val target_weight : Float,
    @SerializedName("diet_type") val diet_type : String,
    @SerializedName("avatar_color") val avatar_color : String,
    @SerializedName("nickname") val nickname : String,
)

data class SignupResponse(
    @SerializedName("member_id") val member_id : Int,
    @SerializedName("jwt") val jwt : String,
)

data class LoginRequest(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String
)

data class LoginResponse(
    @SerializedName("memberId") val memberId : Int,
    @SerializedName("jwt") val jwt : String
)

data class Plan(
    @SerializedName("initial_weight") val initial_weight: Double,
    @SerializedName("target_weight") val target_weight: Double,
    @SerializedName("recommended_kcal") val recommended_kcal: Int,
    @SerializedName("diet_type") val diet_type: String,
    @SerializedName("carbohydrate_rate") val carbohydrate_rate: Int,
    @SerializedName("protein_rate") val protein_rate: Int,
    @SerializedName("fat_rate") val fat_rate: Int,
    @SerializedName("target_kcal") val target_kcal: Int
)

data class PlanUpdateRequest(
    @SerializedName("initial_weight") val initial_weight: Double,
    @SerializedName("target_weight") val target_weight: Double,
    @SerializedName("diet_type") val diet_type: String,
    @SerializedName("carbohydrate_rate") val carbohydrate_rate: Int,
    @SerializedName("protein_rate") val protein_rate: Int,
    @SerializedName("fat_rate") val fat_rate: Int,
    @SerializedName("target_kcal") val target_kcal: Int
)

data class PlanDietTypeRequest(
    @SerializedName("diet_type") val diet_type: String
)

data class PlanDietTypeResponse(
    @SerializedName("diet_type") val diet_type: String,
    @SerializedName("carbohydrate_rate") val carbohydrate_rate: Int,
    @SerializedName("protein_rate") val protein_rate: Int,
    @SerializedName("fat_rate") val fat_rate: Int
)

data class PlanRecommKcalResponse(
    @SerializedName("recommended_kcal") val recommended_kcal: Int
)

data class UserProfileResponse(
    @SerializedName("date") val date: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("elapsed_days") val elapsed_days: Int,
    @SerializedName("remaining_kcal") val remaining_kcal: Int,
    @SerializedName("avatar_color") val avatar_color: String,
    @SerializedName("avatar_appearance") val avatar_appearance: String,
    @SerializedName("goal") val goal: UserProfileResponseGoal,
    @SerializedName("intake") val intake: UserProfileResponseIntake
)

data class UserProfileResponseGoal(
    @SerializedName("target_kcal") val target_kcal: Int,
    @SerializedName("target_carbohydrate") val target_carbohydrate: Int,
    @SerializedName("target_protein") val target_protein: Int,
    @SerializedName("target_fat") val target_fat: Int
)

data class UserProfileResponseIntake(
    @SerializedName("kcal") val kcal: Int,
    @SerializedName("carbohydrate") val carbohydrate: Int,
    @SerializedName("protein") val protein: Int,
    @SerializedName("fat") val fat: Int,
    @SerializedName("sodium") val sodium: Int,
    @SerializedName("sugar") val sugar: Int,
    @SerializedName("saturated_fat") val saturated_fat: Int,
    @SerializedName("trans_fat") val trans_fat: Int,
    @SerializedName("cholesterol") val cholesterol: Int
)

data class MealListDateResponse(
    @SerializedName("meal_date") val meal_date: String,
    @SerializedName("meals") val meals: List<MealListDateResponseMeals>
)

data class MealListDateResponseMeals(
    @SerializedName("meal_id") val meal_id: Int,
    @SerializedName("meal_type") val meal_type: String,
    @SerializedName("meal_kcal") val meal_kcal: Double
)

data class Weight(
    @SerializedName("weight") val weight : Float,
    @SerializedName("date") val date : String,
)

data class MealAddRequest(
    @SerializedName("meal_type") val meal_type: Int,
    @SerializedName("meal_date") val meal_date: String
)

data class MealAddResponse(
    @SerializedName("meal_id") val meal_id: Int
)

data class FavoriteFoodRequest(
    @SerializedName("food_id") val food_id : Int,
)

data class FavoriteFoodResponse(
    @SerializedName("foods") val foods: List<Food>
)

data class Food(
    @SerializedName("food_id") val foodId: Int,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("kcal") val kcal: Int,
)

data class FoodSearchResponse(
    @SerializedName("current_page") val current_page : Int,
    @SerializedName("last_page") val last_page : Int,
    @SerializedName("foods") val foods: List<Food>,
    @SerializedName("member_created") val member_created: Boolean

)

data class ChatRecommendMeal(
    @SerializedName("food_id") val food_id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("offer") val offer : String,
    @SerializedName("quantity") val quantity : Int,
    @SerializedName("offer_carbohydrate") val offer_carbohydrate : Int,
    @SerializedName("offer_protein") val offer_protein : Int,
    @SerializedName("offer_fat") val offer_fat : Int
)

data class CheatDayRecommendMeal(
    @SerializedName("cheat_day_food") val cheat_day_food : List<ChatRecommendMeal>
)

data class ChatMealRequest(
    @SerializedName("food_id") val food_id: Long,
    @SerializedName("quantity") val quantity: Int
)

data class ChatMealListResponse(
    @SerializedName("date") val date : String,
    @SerializedName("meal_type") val meal_type : String,
    @SerializedName("name") val name : String,
    @SerializedName("offer_carbohydrate") val offer_carbohydrate : Int,
    @SerializedName("offer_protein") val offer_protein : Int,
    @SerializedName("offer_fat") val offer_fat : Int
)

data class ChatMealAmountResponse(
    @SerializedName("food_name") val food_name : String,
    @SerializedName("offer") val offer : String,
    @SerializedName("offer_kcal") val offer_kcal : Int,
    @SerializedName("offer_quantity") val offer_quantity : Int,
    @SerializedName("offer_carbohydrate") val offer_carbohydrate : Int,
    @SerializedName("offer_protein") val offer_protein : Int,
    @SerializedName("offer_fat") val offer_fat : Int,
    @SerializedName("remaining_kcal") val remaining_kcal: Int
)