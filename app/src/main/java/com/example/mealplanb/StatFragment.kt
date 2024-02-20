package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentStatBinding
import com.example.mealplanb.remote.AuthService

class StatFragment : Fragment() {
    private lateinit var binding: FragmentStatBinding

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.d("statfragment 실행 확인","success")
//
//        val authService = AuthService(requireContext())
//
//        authService.statisticplan { planInfo ->
//            val startWeight = planInfo?.initial_weight?.toFloat() ?: 0f
//            val wantWeight = planInfo?.target_weight?.toFloat() ?: 0f
//            val diettype = planInfo?.diet_type
//            val diffWeight = startWeight - wantWeight
//
//            binding.statExplainMealCategoryTv.text = "$diettype"
//            binding.statExplainKgTv.text = "$diffWeight"
//
//            Log.d("statfragment 실행 확인","diettype: $diettype, diffweight: $diffWeight")
//
//            val sharedPref = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//            with(sharedPref?.edit()) {
//                this?.putFloat("startWeight", startWeight)
//                this?.putFloat("wantWeight", wantWeight)
//                this?.putString("selectedDiet", diettype)
//                this?.apply()
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatBinding.inflate(inflater, container, false)
        Log.d("statfragment 실행 확인2","success")

        binding.statToGoalBtn.setOnClickListener {
            val anotherFragment = GoalMotifFragment()
            val bundle = Bundle().apply { putString("source", "StatFragment") }
            anotherFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, anotherFragment)
                .commit()
        }

        return binding.root
    }
}
