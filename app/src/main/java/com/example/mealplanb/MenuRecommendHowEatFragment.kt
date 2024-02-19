package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentMenuRecommendHowEatBinding

class MenuRecommendHowEatFragment : Fragment() {
    lateinit var binding: FragmentMenuRecommendHowEatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendHowEatBinding.inflate(layoutInflater)



        return binding.root
    }
}