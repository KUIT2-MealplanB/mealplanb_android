import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.Meal
import com.example.mealplanb.databinding.ItemMeallistBinding
import com.example.mealplanb.remote.Food

class SearchMealAdapter(var items: List<Food>, val onClick: (Food)->(Unit)) : RecyclerView.Adapter<SearchMealAdapter.ViewHolder>() {

    // 뷰홀더 클래스
    inner class ViewHolder(var binding: ItemMeallistBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(myMeal: Food) {
            binding.itemMeallistTitleTv.text = myMeal.foodName
            binding.itemMeallistSubtitleTv.text = "100g · " + myMeal.kcal.toInt().toString() + "kcal"

            binding.root.setOnClickListener {
                onClick(myMeal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMeallistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // 리스트를 업데이트하는 함수 추가
    fun updateList(newItems: ArrayList<Food>) {
        items = newItems
        notifyDataSetChanged() // RecyclerView에 데이터가 변경되었음을 알림
    }
}