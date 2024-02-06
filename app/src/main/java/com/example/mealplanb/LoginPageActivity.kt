package com.example.mealplanb

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

        //엔터 누르면 포커스 이동
        binding.loginIdEt.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_NEXT){
                binding.loginPasswordEt.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.loginPasswordEt.setOnEditorActionListener { v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.loginAccountMakeTv.setOnClickListener {
            val intent = Intent(this, PrivacyCollectActivity::class.java)
            startActivity(intent)
        }
    }
}