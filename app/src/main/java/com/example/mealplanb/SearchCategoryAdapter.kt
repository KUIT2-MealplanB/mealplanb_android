import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.Meal
import com.example.mealplanb.databinding.ItemMeallistBinding
import com.google.gson.Gson

class SearchCategoryAdapter(var mealList: ArrayList<Meal>, val onClick: (Meal)->(Unit)) : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>() {

    // OftenActivityAdapter 내부의 ViewHolder 클래스
    inner class ViewHolder(val binding: ItemMeallistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMeal: Meal) {
            binding.itemMeallistTitleTv.text = myMeal.meal_name
            binding.itemMeallistSubtitleTv.text = myMeal.meal_weight.toInt().toString() + "g · " + myMeal.meal_cal.toInt().toString() + "kcal"

            binding.root.setOnClickListener {
                onClick(myMeal)
            }


            binding.itemMeallistDelbtnTv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mealList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged() // 변경사항을 알립니다

                    // SharedPreferences에서 데이터 삭제
                    val sharedPreferences =
                        itemView.context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()

                    val json = gson.toJson(mealList)
                    editor.putString("oftenFoodList", json)
                    editor.apply()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMeallistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mealList[position])
    }

    // 리스트를 업데이트하는 함수 추가
    fun updateList(newItems: List<Meal>) {
        mealList = newItems as ArrayList<Meal>
        notifyDataSetChanged() // RecyclerView에 데이터가 변경되었음을 알림
    }
}