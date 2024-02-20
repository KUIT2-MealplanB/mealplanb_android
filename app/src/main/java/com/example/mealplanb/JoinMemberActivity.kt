package com.example.mealplanb

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.mealplanb.databinding.ActivityJoinMemberBinding

class JoinMemberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinMemberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                if(binding.joinMemberIdEt.text.toString().trim().isNotEmpty() &&
                    binding.joinMemberPwEt.text.toString().trim().isNotEmpty() &&
                    binding.joinMemberPwCheckEt.text.toString().trim().isNotEmpty() &&
                    binding.joinMemberEmailEt.text.toString().trim().isNotEmpty()) {

                    binding.joinMemeberCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                    binding.joinMemeberCompleteCv.setOnClickListener {
                        if(binding.joinMemberPwEt.text.toString() != binding.joinMemberPwCheckEt.text.toString()) {
                            binding.joinMemberPwIncorrectTv.visibility = View.VISIBLE
                        } else {
                            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userID",binding.joinMemberIdEt.text.toString())
                            editor.putString("userPW",binding.joinMemberPwEt.text.toString())
                            editor.putString("userEmail",binding.joinMemberEmailEt.text.toString())
                            editor.apply()

                            val intent = Intent(this@JoinMemberActivity, PrivacyCollectActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    binding.joinMemeberCompleteCv.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
                }
            }
        }

        binding.joinMemberIdEt.addTextChangedListener(textWatcher)
        binding.joinMemberPwEt.addTextChangedListener(textWatcher)
        binding.joinMemberPwCheckEt.addTextChangedListener(textWatcher)
        binding.joinMemberEmailEt.addTextChangedListener(textWatcher)
    }
}