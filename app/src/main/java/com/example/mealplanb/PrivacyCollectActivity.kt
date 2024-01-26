package com.example.mealplanb

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mealplanb.databinding.ActivityPrivacyCollectBinding

class PrivacyCollectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyCollectBinding
    private var womanSelected = false
    private var manSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyCollectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            //입력값 들어왔을 때 색 변환
            privacyAgeEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        privacyAgeEt.setBackgroundResource(R.drawable.et_privacy_purple)
                        privacyAgeEt.setTextColor(Color.parseColor("#FFFFFF"))
                        privacyAgeTv.setTextColor(Color.parseColor("#FFFFFF"))
                    } else {
                        privacyAgeEt.setBackgroundResource(R.drawable.et_privacy)
                        privacyAgeEt.setTextColor(Color.parseColor("#101010"))
                        privacyAgeTv.setTextColor(Color.parseColor("#101010"))
                    }
                    checkConditions()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            privacyHeightEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        privacyHeightEt.setBackgroundResource(R.drawable.et_privacy_purple)
                        privacyHeightEt.setTextColor(Color.parseColor("#FFFFFF"))
                        privacyHeightTv.setTextColor(Color.parseColor("#FFFFFF"))
                    } else {
                        privacyHeightEt.setBackgroundResource(R.drawable.et_privacy)
                        privacyHeightEt.setTextColor(Color.parseColor("#101010"))
                        privacyHeightTv.setTextColor(Color.parseColor("#101010"))
                    }
                    checkConditions()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            privacyStartWeightEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        privacyStartWeightEt.setBackgroundResource(R.drawable.et_privacy_purple)
                        privacyStartWeightEt.setTextColor(Color.parseColor("#FFFFFF"))
                        privacyStartWeightTv.setTextColor(Color.parseColor("#FFFFFF"))
                    } else {
                        privacyStartWeightEt.setBackgroundResource(R.drawable.et_privacy)
                        privacyStartWeightEt.setTextColor(Color.parseColor("#101010"))
                        privacyStartWeightTv.setTextColor(Color.parseColor("#101010"))
                    }
                    checkConditions()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            privacyWantWeightEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        privacyWantWeightEt.setBackgroundResource(R.drawable.et_privacy_purple)
                        privacyWantWeightEt.setTextColor(Color.parseColor("#FFFFFF"))
                        privacyWantWeightTv.setTextColor(Color.parseColor("#FFFFFF"))
                    } else {
                        privacyWantWeightEt.setBackgroundResource(R.drawable.et_privacy)
                        privacyWantWeightEt.setTextColor(Color.parseColor("#101010"))
                        privacyWantWeightTv.setTextColor(Color.parseColor("#101010"))
                    }
                    checkConditions()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            //여자 버튼
            privacyWomanCv.setOnClickListener {
                womanSelected = true
                manSelected = false
                privacyWomanCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                privacyWomanTv.setTextColor(Color.parseColor("#FFFFFF"))
                privacyManCv.setCardBackgroundColor(Color.parseColor("#EAEBFE"))
                privacyManTv.setTextColor(Color.parseColor("#101010"))
                checkConditions()
            }

            //남자 버튼
            privacyManCv.setOnClickListener {
                manSelected = true
                womanSelected = false
                privacyManCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                privacyManTv.setTextColor(Color.parseColor("#FFFFFF"))
                privacyWomanCv.setCardBackgroundColor(Color.parseColor("#EAEBFE"))
                privacyWomanTv.setTextColor(Color.parseColor("#101010"))
                checkConditions()
            }

            //뒤로가기
            privacyBackIv.setOnClickListener {
                finish()
            }

            //다음 버튼
            privacyCompleteCv.setOnClickListener {
                if(checkConditions()) {
                    val intent = Intent(this@PrivacyCollectActivity, RecommendCategoryActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@PrivacyCollectActivity, "아직 정보를 다 기입하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //입력이 비어있는지, 여자남자 버튼 눌려있는지 확인
    private fun checkConditions(): Boolean {
        with(binding) {
            if ((womanSelected || manSelected) &&
                privacyAgeEt.text.toString().toIntOrNull() ?: 0 > 0 &&
                privacyHeightEt.text.toString().toIntOrNull() ?: 0 > 0 &&
                privacyStartWeightEt.text.toString().toIntOrNull() ?: 0 > 0 &&
                privacyWantWeightEt.text.toString().toIntOrNull() ?: 0 > 0) {

                privacyCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                return true
            } else {
                privacyCompleteCv.setCardBackgroundColor(Color.parseColor("#EAEBFE"))
                return false
            }
        }
    }
}