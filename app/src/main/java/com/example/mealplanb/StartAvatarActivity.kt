package com.example.mealplanb

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mealplanb.databinding.ActivityStartAvatarBinding

class StartAvatarActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartAvatarBinding
    private var avatarImageID : Int = 3
    private var isFirstClick = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        var avatarImageID = sharedPref.getInt("avatar",3)

        //초기 컬러 Circle 셋팅
        binding.startAvatarWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
        binding.startAvatarPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
        binding.startAvatarPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
        binding.startAvatarBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
        binding.startAvatarGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)

        when(avatarImageID) {
            1 -> binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }

        //보라색 Circle 눌렸을 때
        binding.startAvatarPurpleCircleIv.setOnClickListener{
            binding.startAvatarPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_check_img)
            binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_purple_img)
            binding.startAvatarPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
            binding.startAvatarBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
            binding.startAvatarGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)
            binding.startAvatarWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)

            avatarImageID = 3
        }

        //핑크색 Circle 눌렸을 때
        binding.startAvatarPickCircleIv.setOnClickListener{
            binding.startAvatarPickCircleIv.setImageResource(R.drawable.pick_color_circle_check_img)
            binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_pink_img)
            binding.startAvatarBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
            binding.startAvatarGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)
            binding.startAvatarWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
            binding.startAvatarPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)

            avatarImageID = 1
        }

        //검정색 Circle 눌렸을 때
        binding.startAvatarBlackCircleIv.setOnClickListener{
            binding.startAvatarBlackCircleIv.setImageResource(R.drawable.black_color_circle_check_img)
            binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_black_img)
            binding.startAvatarGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)
            binding.startAvatarWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
            binding.startAvatarPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
            binding.startAvatarPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)

            avatarImageID = 4
        }

        //회색 Circle 눌렸을 때
        binding.startAvatarGrayCircleIv.setOnClickListener{
            binding.startAvatarGrayCircleIv.setImageResource(R.drawable.gray_color_circle_check_img)
            binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_gray_img)
            binding.startAvatarWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
            binding.startAvatarPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
            binding.startAvatarPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
            binding.startAvatarBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)

            avatarImageID = 5
        }

        //흰색 Circle 눌렸을 때
        binding.startAvatarWhiteCircleIv.setOnClickListener{
            binding.startAvatarWhiteCircleIv.setImageResource(R.drawable.white_color_circle_check_img)
            binding.startAvatarAvatarIv.setImageResource(R.drawable.avartar_basic_white_img)
            binding.startAvatarPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
            binding.startAvatarPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
            binding.startAvatarBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
            binding.startAvatarGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)

            avatarImageID = 2
        }

        //뒤로 가기
        binding.startAvatarBackIv.setOnClickListener {
            finish()
        }

        // et 초기 안보이게
        binding.startAvatarEt.visibility = View.GONE

        binding.startAvatarCompleteCv.setOnClickListener {
            if(isFirstClick) { // 첫 번째 클릭인 경우
                binding.startAvatarTitle1.text = "새로운 텍스트" // TextView 텍스트 변경

                // ImageView와 Shadow ImageView의 margin top 값 100 증가
                val avatarLayoutParams = binding.startAvatarAvatarIv.layoutParams as ConstraintLayout.LayoutParams
                avatarLayoutParams.topMargin += 100
                binding.startAvatarAvatarIv.layoutParams = avatarLayoutParams

                val shadowLayoutParams = binding.startAvatarShadowIv.layoutParams as ConstraintLayout.LayoutParams
                shadowLayoutParams.topMargin += 100
                binding.startAvatarShadowIv.layoutParams = shadowLayoutParams

                binding.startAvatarColorCircleLl.visibility = View.GONE // LinearLayout 숨기기
                binding.startAvatarEt.visibility = View.VISIBLE // EditText 보이게 하기

                // CardView 색깔 변경
                binding.startAvatarCompleteCv.setCardBackgroundColor(Color.GRAY) // 색깔은 원하는 값으로 변경

                isFirstClick = false // 다음 클릭을 위해 변수 값 변경
            } else { // 두 번째 클릭인 경우
                if(binding.startAvatarEt.text.isNotEmpty()) { // EditText에 값이 입력되었는지 확인

                    val editor = sharedPref.edit()
                    editor.putInt("avatar",avatarImageID)
                    editor.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.startAvatarEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()) { // EditText에 값이 입력되면
                    // CardView 색깔 원래대로 변경
                    binding.startAvatarCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                }
            }
        })

        //nickname ET 눌렀을 때
        binding.startAvatarEt.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                actionId == EditorInfo.IME_ACTION_GO ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // EditText에 입력된 텍스트 가져오기
                val nickNameValue = binding.startAvatarEt.text.toString()

                // EditText에 입력된 텍스트 반영
                binding.startAvatarEt.setText(nickNameValue)

                // 포커스를 다른 뷰로 이동시켜서 커서를 감추기
                binding.startAvatarEt.clearFocus()

                // Hide the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.startAvatarEt.windowToken, 0)

                // SharedPreferences.Editor 객체를 얻어서 값을 저장
                val editor = sharedPref.edit()
                editor.putString("nickname", nickNameValue)
                editor.apply()

                Toast.makeText(this, "nickNameValue apply", Toast.LENGTH_SHORT).show()

                // true를 반환하면 이벤트가 소비됨을 나타냅니다.
                return@setOnEditorActionListener true
            }
            // Return false if the action is not consumed
            false
        }


    }

}