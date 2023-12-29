import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.Item
import com.example.mealplanb.Meal
import com.example.mealplanb.databinding.ItemLayoutBinding

class MyAdapter(var items: ArrayList<Meal>, val onClick: (Meal)->(Unit)) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // 뷰홀더 클래스
    inner class ViewHolder(var binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        var tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvItemDetails: TextView = itemView.findViewById(R.id.tv_item_details)
        var tvPlus: TextView = itemView.findViewById(R.id.tv_plus)
        fun bind(myMeal: Meal) {
            binding.tvItemName.text = myMeal.meal_name
            binding.tvItemDetails.text = myMeal.meal_weight.toString() + "g · " + myMeal.meal_cal.toString() + "kcal"

            binding.root.setOnClickListener {
                onClick(myMeal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.tvItemName.text = items[position].meal_name
        //holder.tvItemDetails.text = "${items[position].gram}g · ${items[position].calorie}kcal" // 그램과 칼로리를 표시
        holder.bind(items[position])
    }

    // 리스트를 업데이트하는 함수 추가
    fun updateList(newItems: ArrayList<Meal>) {
        items = newItems
        notifyDataSetChanged() // RecyclerView에 데이터가 변경되었음을 알림
    }
}
