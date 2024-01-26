package com.example.mealplanb

import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentMenuRecommendBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class MenuRecommendFragment : Fragment(), OnFragmentInteractionListener, AddToRecommendRv {

    lateinit var binding : FragmentMenuRecommendBinding
    private lateinit var adapter: MenuRecommendAdapter
    private lateinit var recommListAdapter : RecommendedListAdapter
    private val menuRecommItems = mutableListOf<MenuRecommItem>()
    var recomList : ArrayList<RecommendMenu> = arrayListOf()
    val cal : Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        cal.set(2024,0,18)
//        recomList.addAll(
//            arrayListOf(
//                RecommendMenu(cal,"첫 끼 : 연어초밥",550,320,400),
//                RecommendMenu(cal,"두 끼 : 샌드위치",500,220,120),
//                RecommendMenu(cal,"세 끼 : 굽네 고추바사삭",450,180,310),))

        binding = FragmentMenuRecommendBinding.inflate(layoutInflater)

        val init_fragment = MenuRecommendInitFragment()

        // 초기 상태로 MenuRecommendInitFragment를 추가
        requireActivity().supportFragmentManager.beginTransaction().replace(binding.menuRecommButtonCv.id, init_fragment)
            .commit()

        binding.menuRecommCancleIm.setOnClickListener {
            val bottomNavigationView = view?.findViewById<BottomNavigationView>(R.id.main_navigation)
            bottomNavigationView?.selectedItemId = R.id.menu_mypage
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
        }

        // 어댑터 초기화
        adapter = MenuRecommendAdapter(requireContext(), menuRecommItems)

        // 리사이클러뷰 설정
        binding.menuRecommChatRv.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecommChatRv.adapter = adapter

        // 예시 데이터 생성
        menuRecommItems.add(MenuRecommItem.DateItem(1, 5, "목", 0))
        menuRecommItems.add(MenuRecommItem.SystemItem("어떻게 식단", "을 추천해드릴까요?", 1))

        binding.menuRecommTogle.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs",MODE_PRIVATE)
                var gson = Gson()
                var json = sharedPreferences.getString("recommendList",null)
                recomList = gson.fromJson(json, object : TypeToken<ArrayList<RecommendMenu>>() {}.type) ?: arrayListOf<RecommendMenu>()

                recommListAdapter = RecommendedListAdapter(recomList,cal)

                binding.menuRecommRecommedListRv.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                binding.menuRecommRecommedListRv.adapter = recommListAdapter

                binding.menuRecommRecommedListRv.visibility = View.VISIBLE
                binding.menuRecommChatRv.visibility = View.GONE
                binding.menuRecommButtonCv.visibility = View.GONE
            } else {
                binding.menuRecommRecommedListRv.visibility = View.GONE
                binding.menuRecommChatRv.visibility = View.VISIBLE
                binding.menuRecommButtonCv.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
    fun addInitFragmentItems(vararg items: MenuRecommItem) {
        Log.d("logcat",menuRecommItems.size.toString()+"!")
        menuRecommItems.addAll(items)
        Log.d("logcat",menuRecommItems.size.toString()+"!")
        adapter.notifyDataSetChanged()
    }

    fun addWhatMenuFragmentItems(vararg items: MenuRecommItem) {
        menuRecommItems.addAll(items)
        adapter.notifyDataSetChanged()
    }

    fun addCheatMenuFragmentItems(vararg items: MenuRecommItem) {
        menuRecommItems.addAll(items)
        adapter.notifyDataSetChanged()
    }

    override fun onExitRequested() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
    }

    override fun addItemToRecyclerView(item: RecommendMenu) {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs",MODE_PRIVATE)
        var gson = Gson()
        var json = sharedPreferences.getString("recommendList",null)
        var recomList = gson.fromJson(json, object : TypeToken<ArrayList<RecommendMenu>>() {}.type) ?: arrayListOf<RecommendMenu>()

        recomList.add(item)

        val editor = sharedPreferences.edit()
        var newJson = gson.toJson(recomList)
        editor.putString("recommendList",newJson)
        editor.apply()

        recommListAdapter = RecommendedListAdapter(recomList,cal)

        binding.menuRecommRecommedListRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.menuRecommRecommedListRv.adapter = recommListAdapter

        recommListAdapter.notifyDataSetChanged()
    }

}