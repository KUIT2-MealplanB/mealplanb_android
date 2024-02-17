package com.example.mealplanb

sealed class MenuRecommItem {
    data class SystemItem(
        val menu_recomm_system_bold: String,
        val menu_recomm_system_regural: String,
        val type : Int
    ) : MenuRecommItem()

    data class SystemKcalItem(
        val menu_recomm_system_kcal_num: Int,
        val menu_recomm_system_kcal_sacc_num: Int,
        val menu_recomm_system_kcal_protein_num: Int,
        val menu_recomm_system_kcal_fat_num: Int,
        val type : Int
    ) : MenuRecommItem()

    data class SystemMenuItem(
        val menu_recomm_system_menu_name: String,
        val menu_recomm_system_menu_sacc_num: Int,
        val menu_recomm_system_menu_protein_num: Int,
        val menu_recomm_system_menu_fat_num: Int,
        val type : Int
    ) : MenuRecommItem()

    data class SystemHowManyItem(
        val menu_recomm_system_howmany_name: String,
        val menu_recomm_system_howmany_remain_cal : Int,
        val menu_recomm_system_howmany_menu_cal: Int,
        val menu_recomm_system_howmany_menu_piece: Int,
        val type: Int
    ) : MenuRecommItem()

    data class SystemUpdateMenuItem(
        val menu_recomm_system_update_menu_name: String,
        val type: Int
    ) : MenuRecommItem()

    data class UserItem(
        val menu_recomm_user_bold: String,
        val menu_recomm_user_regural: String,
        val type : Int
    ) : MenuRecommItem()

    data class UserCheatItem(
        val menu_recomm_user_cheat_bold: String,
        val menu_recomm_user_cheat_regural: String,
        val type : Int
    ) : MenuRecommItem()

    data class DateItem(
        val menu_recomm_month: Int,
        val menu_recomm_day: Int,
        val menu_recomm_week: String,
        val type : Int
    ) : MenuRecommItem()
}