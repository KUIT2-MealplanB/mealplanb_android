package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.YearMonth

//로그인 request
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

//로그인 response
data class LoginResponse(
    @SerializedName("memberId") val memberId : Int,
    @SerializedName("jwt") val jwt : String
)

data class AvatarCheckResponse(
    @SerializedName("avatar_appearance") val avatar_appearance : String,
    @SerializedName("avatar_color") val avatar_color: String,
    @SerializedName("nickname") val nickname: String
)

data class AvatarData(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("avatar_color") val avatar_color: String
)

data class AvatarUpdateResponse(
    @SerializedName("member_id") val member_id: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatar_color") val avatar_color: String
)


data class GetFoodResponse(
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity : Int,
    @SerializedName("kcal") val kcal : Double,
    @SerializedName("carbohydrate") val carbohydrate : Double,
    @SerializedName("protein") val protein : Double,
    @SerializedName("fat") val fat : Double,
    @SerializedName("isFavorite") val isFavorite : Boolean
)


data class mymealData(
    @SerializedName("favorite_meal_name") val favorite_meal_name:String,
    @SerializedName("foods") val foods: List<FoodList>
)

data class FoodList(
    @SerializedName("food_id") val food_id: Int,
    @SerializedName("quantity") val quantity: Int
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

data class MealFoodResponse(
    @SerializedName("meal_id") val meal_id: Int,
    @SerializedName("meal_date") val meal_date: String,
    @SerializedName("meal_type") val meal_type: Int,
    @SerializedName("food_list") val food_list: List<MealFoodResponseFoodList>
)

data class MealFoodResponseFoodList(
    @SerializedName("food_id") val foodId: Int,
    @SerializedName("quantity") val quantity : Int,
    @SerializedName("name") val name: String,
    @SerializedName("kcal") val kcal: Double,
)

data class FoodListAddRequest(
    @SerializedName("meal_id") val meal_id: Long,
    @SerializedName("foods") val foods: List<FoodListAddRequestFoodItem>
)

data class FoodListAddRequestFoodItem(
    @SerializedName("food_id") val food_id: Int,
    @SerializedName("quantity") val quantity : Int
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

data class StatKcalDayResponse(
    @SerializedName("statistic_type") val statisticType: String,
    @SerializedName("kcals") val kcals: List<DailyKcal>
)

data class DailyKcal(
    @SerializedName("date") val date: String,
    @SerializedName("kcal") val kcal: Int,
    @SerializedName("carbohydrate") val carbohydrate : Int,
    @SerializedName("protein") val protein : Int,
    @SerializedName("fat") val fat : Int,
)

data class WeeklyKcal(
    @SerializedName("date") val date: String,
    @SerializedName("kcal") val kcal: Int,
    @SerializedName("carbohydrate") val carbohydrate : Int,
    @SerializedName("protein") val protein : Int,
    @SerializedName("fat") val fat : Int,
)

data class MonthKcal(
    @SerializedName("date") val date: String,
    @SerializedName("kcal") val kcal: Int,
    @SerializedName("carbohydrate") val carbohydrate : Int,
    @SerializedName("protein") val protein : Int,
    @SerializedName("fat") val fat : Int,
)

data class StatWeightDayResponse(
    @SerializedName("statistic_type") val statisticType: String,
    @SerializedName("weights") val weights: List<WeightResponse>
)

data class WeightResponse(
    @SerializedName("weight") val weight: Double,
    @SerializedName("date") val date: String
)

data class StatWeightWeekResponse(
    @SerializedName("statistic_type") val statisticType: String,
    @SerializedName("weights") val weights: List<WeeklyWeight>
)

data class WeeklyWeight(
    @SerializedName("week_average_weight") val weekAverageWeight: Double,
    @SerializedName("week_start_date") val weekStartDate: String,
    @SerializedName("week_end_date") val weekEndDate: String
)

data class StatWeightMonthResponse(
    @SerializedName("statistic_type") val statisticType: String,
    @SerializedName("weights") val weights: List<MonthlyWeight>
)

data class MonthlyWeight(
    @SerializedName("month_average_weight") val monthAverageWeight: Double,
    @SerializedName("month") val month: String
)

data class StatPlanResponse(
    @SerializedName("initial_weight") val initial_weight: Double,
    @SerializedName("target_weight") val target_weight: Double,
    @SerializedName("diet_type") val diet_type: String
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