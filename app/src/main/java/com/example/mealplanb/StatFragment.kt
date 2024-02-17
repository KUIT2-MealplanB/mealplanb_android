package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class StatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stat, container, false)
        val button = view.findViewById<AppCompatButton>(R.id.stat_to_goal_btn)
        // 버튼을 눌렀을 때
        button.setOnClickListener {
            val anotherFragment = GoalMotifFragment()
            val bundle = Bundle()
            bundle.putString("source", "StatFragment") // 출처 적어두기
            anotherFragment.arguments = bundle

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.main_frm, anotherFragment)
            fragmentTransaction?.commit()
        }

        return view
    }
}