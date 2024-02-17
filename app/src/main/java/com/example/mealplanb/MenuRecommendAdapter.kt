package com.example.mealplanb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuRecommendAdapter(
    private val context: Context,
    private val menuRecommItems: List<MenuRecommItem>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DATE = 0
        private const val VIEW_TYPE_SYSTEM = 1
        private const val VIEW_TYPE_USER = 2
        private const val VIEW_TYPE_SYSTEM_KCAL = 3
        private const val VIEW_TYPE_USER_CHEAT = 4
        private const val VIEW_TYPE_SYSTEM_MENU = 5
        private const val VIEW_TYPE_SYSTEM_HOWMANY = 6
        private const val VIEW_TYPE_SYSTEM_UPDATE = 7
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view:View?
        return when (viewType) {
            VIEW_TYPE_SYSTEM -> {
                view = inflater.inflate(R.layout.item_menu_recommend_system, parent, false)
                MenuRecommSystemViewHolder(view)
            }VIEW_TYPE_USER ->{
                view = inflater.inflate(R.layout.item_menu_recommend_user, parent, false)
                MenuRecommUserViewHolder(view)
            }VIEW_TYPE_SYSTEM_KCAL ->{
                view = inflater.inflate(R.layout.item_menu_recommend_system_kcal, parent, false)
                MenuRecommSystemKcalViewHolder(view)
            }VIEW_TYPE_SYSTEM_MENU ->{
                view = inflater.inflate(R.layout.item_menu_recommend_system_menu, parent, false)
                MenuRecommSystemMenuViewHolder(view)
            }VIEW_TYPE_USER_CHEAT ->{
                view = inflater.inflate(R.layout.item_menu_recommend_user_cheat_day, parent, false)
                MenuRecommUserCheatViewHolder(view)
            }VIEW_TYPE_SYSTEM_HOWMANY ->{
                view = inflater.inflate(R.layout.item_menu_recommend_system_amount_menu, parent, false)
                MenuRecommSystemHowManyViewHolder(view)
            }VIEW_TYPE_SYSTEM_UPDATE ->{
                view = inflater.inflate(R.layout.item_menu_recommend_system_update, parent, false)
                MenuRecommSystemUpdateMenuViewHolder(view)
            }
            else -> {
                view = inflater.inflate(R.layout.item_menu_recommend_date, parent, false)
                MenuRecommDateViewHolder(view)
            }

        }
    }


    override fun getItemCount(): Int {
        return menuRecommItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (menuRecommItems[position]) {
            is MenuRecommItem.SystemItem -> 1
            is MenuRecommItem.DateItem -> 0
            is MenuRecommItem.UserItem -> 2
            is MenuRecommItem.SystemKcalItem -> 3
            is MenuRecommItem.UserCheatItem -> 4
            is MenuRecommItem.SystemMenuItem -> 5
            is MenuRecommItem.SystemHowManyItem -> 6
            is MenuRecommItem.SystemUpdateMenuItem -> 7
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_SYSTEM -> {
                val systemItem = menuRecommItems[position] as MenuRecommItem.SystemItem
                (holder as MenuRecommSystemViewHolder).bind(systemItem)
                holder.setIsRecyclable(false)
            }
            VIEW_TYPE_USER -> {
                val userItem = menuRecommItems[position] as MenuRecommItem.UserItem
                (holder as MenuRecommUserViewHolder).bind(userItem)
                holder.setIsRecyclable(false)
            }
            VIEW_TYPE_SYSTEM_KCAL -> {
                val systemKcalItem = menuRecommItems[position] as MenuRecommItem.SystemKcalItem
                (holder as MenuRecommSystemKcalViewHolder).bind(systemKcalItem)
                holder.setIsRecyclable(false)
            }
            VIEW_TYPE_SYSTEM_MENU -> {
                val systemMenuItem = menuRecommItems[position] as MenuRecommItem.SystemMenuItem
                (holder as MenuRecommSystemMenuViewHolder).bind(systemMenuItem)
                holder.setIsRecyclable(false)
            }
            VIEW_TYPE_USER_CHEAT -> {
                val userCheatItem = menuRecommItems[position] as MenuRecommItem.UserCheatItem
                (holder as MenuRecommUserCheatViewHolder).bind(userCheatItem)
                holder.setIsRecyclable(false)
            }
            VIEW_TYPE_SYSTEM_HOWMANY -> {
                val systemHowManyItem = menuRecommItems[position] as MenuRecommItem.SystemHowManyItem
                (holder as MenuRecommSystemHowManyViewHolder).bind(systemHowManyItem)
                holder.setIsRecyclable(false)
            }
            VIEW_TYPE_SYSTEM_UPDATE -> {
                val systemUpdateMenuItem = menuRecommItems[position] as MenuRecommItem.SystemUpdateMenuItem
                (holder as MenuRecommSystemUpdateMenuViewHolder).bind(systemUpdateMenuItem)
                holder.setIsRecyclable(false)
            }
            else -> {
                val dateItem = menuRecommItems[position] as MenuRecommItem.DateItem
                (holder as MenuRecommDateViewHolder).bind(dateItem)
                holder.setIsRecyclable(false)
            }
        }
    }

    // ViewHolder for MenuRecommSystemData
    inner class MenuRecommSystemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val boldTextSystemView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_how_tv)
        private val regularTextSystemView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_how_recommend_tv)

        fun bind(data: MenuRecommItem.SystemItem) {
            // Bind data to views
            boldTextSystemView.text = data.menu_recomm_system_bold
            regularTextSystemView.text = data.menu_recomm_system_regural
        }
    }

    inner class MenuRecommUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val boldTextUserView: TextView = itemView.findViewById(R.id.item_menu_recomm_user_select_this)
        private val regularTextUserView: TextView = itemView.findViewById(R.id.item_menu_recomm_user_select_eat)

        fun bind(data: MenuRecommItem.UserItem) {
            // Bind data to views
            boldTextUserView.text = data.menu_recomm_user_bold
            regularTextUserView.text = data.menu_recomm_user_regural
        }
    }

    inner class MenuRecommUserCheatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val boldUserCheatTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_user_cheat_bold)
        private val regularUserCheatTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_user_cheat_regular)

        fun bind(data: MenuRecommItem.UserCheatItem) {
            // Bind data to views
            boldUserCheatTextView.text = data.menu_recomm_user_cheat_bold
            regularUserCheatTextView.text = data.menu_recomm_user_cheat_regural
        }
    }

    inner class MenuRecommSystemKcalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val SystemKcalNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_kcal_num_tv)
        private val SystemKcalSaccNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_kcal_nutrient_sacc_num_tv)
        private val SystemKcalProteinNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_kcal_nutrient_protein_num_tv)
        private val SystemKcalFatNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_kcal_nutrient_fat_num_tv)

        fun bind(data: MenuRecommItem.SystemKcalItem) {
            // Bind data to views
            SystemKcalNumTextView.text = data.menu_recomm_system_kcal_num.toString()
            SystemKcalSaccNumTextView.text = data.menu_recomm_system_kcal_sacc_num.toString()
            SystemKcalProteinNumTextView.text = data.menu_recomm_system_kcal_protein_num.toString()
            SystemKcalFatNumTextView.text = data.menu_recomm_system_kcal_fat_num.toString()
        }
    }

    inner class MenuRecommSystemMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val SystemMenuNameTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_menu_name_tv)
        private val SystemMenuSaccNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_menu_nutrient_sacc_num_tv)
        private val SystemMenuProteinNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_menu_nutrient_protein_num_tv)
        private val SystemMenuFatNumTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_menu_nutrient_fat_num_tv)

        fun bind(data: MenuRecommItem.SystemMenuItem) {
            // Bind data to views
            SystemMenuNameTextView.text = data.menu_recomm_system_menu_name.toString()
            SystemMenuSaccNumTextView.text = data.menu_recomm_system_menu_sacc_num.toString()
            SystemMenuProteinNumTextView.text = data.menu_recomm_system_menu_protein_num.toString()
            SystemMenuFatNumTextView.text = data.menu_recomm_system_menu_fat_num.toString()
        }
    }

    inner class MenuRecommSystemHowManyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val SystemHowManyTitleTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_amount_title_bold_tv)
        private val SystemHowManyRemainCalTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_amount_remain_content_tv)
        private val SystemHowManyMenuNameTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_amount_menu_title_tv)
        private val SystemHowManyMenuPieceTitleTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_amount_total_title_tv)
        private val SystemHowManyMenuCalTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_amount_menu_content_tv)
        private val SystemHowManyMenuPieceTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_amount_total_content_tv)

        fun bind(data: MenuRecommItem.SystemHowManyItem) {
            // Bind data to views
            SystemHowManyRemainCalTextView.text = data.menu_recomm_system_howmany_remain_cal.toString() + "kcal"
            SystemHowManyMenuNameTextView.text = data.menu_recomm_system_howmany_name + " 1인분"
            SystemHowManyMenuPieceTitleTextView.text = data.menu_recomm_system_howmany_name + " 조각"
            SystemHowManyMenuCalTextView.text = data.menu_recomm_system_howmany_menu_cal.toString() + "kcal"
            SystemHowManyMenuPieceTextView.text = data.menu_recomm_system_howmany_menu_piece.toString() + "개"
            var calAns = data.menu_recomm_system_howmany_remain_cal / (data.menu_recomm_system_howmany_menu_cal / data.menu_recomm_system_howmany_menu_piece)
            SystemHowManyTitleTextView.text = "약 " + calAns.toString() + "조각"
        }
    }

    inner class MenuRecommSystemUpdateMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val SystemUpdateMenuNameTextView: TextView = itemView.findViewById(R.id.item_menu_recomm_system_menu_update_tv)

        fun bind(data: MenuRecommItem.SystemUpdateMenuItem) {
            // Bind data to views
            SystemUpdateMenuNameTextView.text = data.menu_recomm_system_update_menu_name
        }
    }

    // ViewHolder for MenuRecommDateData
    inner class MenuRecommDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val MonthTextView : TextView = itemView.findViewById(R.id.menu_recomm_date_month_tv)
        private val DayTextView : TextView = itemView.findViewById(R.id.menu_recomm_date_day_tv)
        private val WeekTextView : TextView = itemView.findViewById(R.id.menu_recomm_date_week_tv)

        fun bind(data: MenuRecommItem.DateItem) {
            // Bind data to views
            MonthTextView.text = data.menu_recomm_month.toString()
            DayTextView.text = data.menu_recomm_day.toString()
            WeekTextView.text = data.menu_recomm_week
        }
    }


}