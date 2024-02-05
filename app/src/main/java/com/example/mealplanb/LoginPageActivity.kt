package com.example.mealplanb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.mealplanb.databinding.ActivityLoginPageBinding

class LoginPageActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                if(binding.loginIdEt.text.toString().trim().isNotEmpty() && binding.loginPasswordEt.text.toString().trim().isNotEmpty()) {
                    binding.loginCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                    binding.loginCompleteCv.setOnClickListener {
                        val intent = Intent(this@LoginPageActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    binding.loginCompleteCv.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
                }
            }
        }

        binding.loginIdEt.addTextChangedListener(textWatcher)
        binding.loginPasswordEt.addTextChangedListener(textWatcher)

        binding.loginAccountMakeTv.setOnClickListener {
            val intent = Intent(this, PrivacyCollectActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}