package com.example.mealplanb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.mealplanb.databinding.ActivityHiddenPageBinding

class HiddenPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHiddenPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHiddenPageBinding.inflate(layoutInflater)

        // <- 아이콘 누르면 뒤로가기 실행
        binding.hiddenBackIv.setOnClickListener {
            finish()
        }

        //아바타 설정 레이아웃을 누르면 페이지 전환
        binding.hiddenAvartarLl.setOnClickListener {
            val intent = Intent(this, AvatarMotifActivity::class.java)
            startActivity(intent)
        }

        //로그아웃 레이아웃을 누르면 대화상자 뜸
        binding.hiddenLogoutLl.setOnClickListener {

            val customDialog = HiddenLogoutDialogFragment(this)
            //배경 어둡게 설정
            binding.hiddenDarkLl.visibility = View.VISIBLE
            customDialog.setCanceledOnTouchOutside(false) // 외부 터치를 취소하지 않음
            customDialog.show()

            //대화상자의 각 View에 접근
            val confirmButton = customDialog.findViewById<Button>(R.id.hidden_logout_logout_btn)
            val cancelButton = customDialog.findViewById<Button>(R.id.hidden_logout_cancle_btn)

            confirmButton.setOnClickListener {
                binding.hiddenDarkLl.visibility = View.INVISIBLE
                finish()
            }
            cancelButton.setOnClickListener {
                binding.hiddenDarkLl.visibility = View.INVISIBLE
                customDialog.dismiss() // 다이얼로그를 닫음
            }
        }

        setContentView(binding.root)
    }

}