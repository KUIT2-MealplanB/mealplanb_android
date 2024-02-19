package com.example.mealplanb

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.mealplanb.databinding.ActivityAvatarMotifBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import jp.wasabeef.blurry.Blurry

class AvatarMotifActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAvatarMotifBinding
    private var avatarImageID : Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAvatarMotifBinding.inflate(layoutInflater)
        val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        var avatarImageID = sharedPref.getInt("avatar",3)

        //초기 컬러 Circle 셋팅
        binding.avatarMotifWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
        binding.avatarMotifPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
        binding.avatarMotifPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
        binding.avatarMotifBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
        binding.avatarMotifGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)

        when(avatarImageID) {
            1 -> binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_pink_img)
            2 -> binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_white_img)
            3 -> binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_purple_img)
            4 -> binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_black_img)
            5 -> binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_gray_img)
        }

        //보라색 Circle 눌렸을 때
        binding.avatarMotifPurpleCircleIv.setOnClickListener{
            binding.avatarMotifPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_check_img)
            binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_purple_img)
            binding.avatarMotifPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
            binding.avatarMotifBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
            binding.avatarMotifGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)
            binding.avatarMotifWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)

            avatarImageID = 3
        }

        //핑크색 Circle 눌렸을 때
        binding.avatarMotifPickCircleIv.setOnClickListener{
            binding.avatarMotifPickCircleIv.setImageResource(R.drawable.pick_color_circle_check_img)
            binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_pink_img)
            binding.avatarMotifBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
            binding.avatarMotifGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)
            binding.avatarMotifWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
            binding.avatarMotifPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)

            avatarImageID = 1
        }

        //검정색 Circle 눌렸을 때
        binding.avatarMotifBlackCircleIv.setOnClickListener{
            binding.avatarMotifBlackCircleIv.setImageResource(R.drawable.black_color_circle_check_img)
            binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_black_img)
            binding.avatarMotifGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)
            binding.avatarMotifWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
            binding.avatarMotifPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
            binding.avatarMotifPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)

            avatarImageID = 4
        }

        //회색 Circle 눌렸을 때
        binding.avatarMotifGrayCircleIv.setOnClickListener{
            binding.avatarMotifGrayCircleIv.setImageResource(R.drawable.gray_color_circle_check_img)
            binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_gray_img)
            binding.avatarMotifWhiteCircleIv.setImageResource(R.drawable.white_color_circle_img)
            binding.avatarMotifPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
            binding.avatarMotifPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
            binding.avatarMotifBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)

            avatarImageID = 5
        }

        //흰색 Circle 눌렸을 때
        binding.avatarMotifWhiteCircleIv.setOnClickListener{
            binding.avatarMotifWhiteCircleIv.setImageResource(R.drawable.white_color_circle_check_img)
            binding.avatarMotifAvatarIv.setImageResource(R.drawable.avartar_basic_white_img)
            binding.avatarMotifPurpleCircleIv.setImageResource(R.drawable.purple_color_circle_img)
            binding.avatarMotifPickCircleIv.setImageResource(R.drawable.pick_color_circle_img)
            binding.avatarMotifBlackCircleIv.setImageResource(R.drawable.black_color_circle_img)
            binding.avatarMotifGrayCircleIv.setImageResource(R.drawable.gray_color_circle_img)

            avatarImageID = 2
        }

        //뒤로 가기 '<' 눌렀을 때
        binding.avatarMotifBackIv.setOnClickListener {
            finish()
        }

        //nickname ET 눌렀을 때
        binding.avatarMotifNicknameEt.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                actionId == EditorInfo.IME_ACTION_GO ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // EditText에 입력된 텍스트 가져오기
                val nickNameValue = binding.avatarMotifNicknameEt.text.toString()

                // EditText에 입력된 텍스트 반영
                binding.avatarMotifNicknameEt.setText(nickNameValue)

                // 포커스를 다른 뷰로 이동시켜서 커서를 감추기
                binding.avatarMotifNicknameEt.clearFocus()

                // Hide the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.avatarMotifNicknameEt.windowToken, 0)

                // SharedPreferences 객체 생성
//                val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

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


        //수정 완료 버튼 눌렀을 때
        binding.avatarMotifCompleteCv.setOnClickListener{
            //색깔 데이터를 이전 Activity에 넘긴다.
            //색깔 데이터와 닉네임 데이터를 MainActivity에 넘긴다.
            //Toast.makeText(this, "수정완료되었습니다", Toast.LENGTH_SHORT).show()

            val editor = sharedPref.edit()
            editor.putInt("avatar",avatarImageID)
            editor.apply()

            finish()
        }

        //아바타 외형 변경하기 버튼을 눌렀을 때
        binding.avatarChangeBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.ui_change_avatar,null)
            val layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                283.dpToPx() // dp를 pixel로 변환
            )
            layoutParams.behavior = BottomSheetBehavior<ConstraintLayout>().apply {
                peekHeight = 283.dpToPx() // 최대 높이 설정
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
            sheetView.layoutParams = layoutParams

            //닫는 버튼 누르면 닫기
            val changeAvatarCancel : ImageView = sheetView.findViewById(R.id.change_avatar_cancel)
            changeAvatarCancel.setOnClickListener{
                bottomSheetDialog.dismiss()
            }

            //완료 버튼 색깔변경
            val changeAvatarCompleteCv : CardView = sheetView.findViewById(R.id.change_avatar_complete_cv)

            val muscleEt : EditText = sheetView.findViewById(R.id.change_avatar_muscle_et)
            val fatEt : EditText = sheetView.findViewById(R.id.change_avatar_fat_et)

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (muscleEt.text.isNotEmpty() && fatEt.text.isNotEmpty()) {
                        // 두 EditText 모두 값이 있을 때 색상 변경
                        changeAvatarCompleteCv.setCardBackgroundColor(Color.parseColor("#7C5CF8"))
                    } else {
                        // 하나라도 값이 없을 때 색상 변경
                        changeAvatarCompleteCv.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            }

            muscleEt.addTextChangedListener(textWatcher)
            fatEt.addTextChangedListener(textWatcher)

            bottomSheetDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)

            bottomSheetDialog.setContentView(sheetView)

            val constraintLayout: ConstraintLayout = findViewById(R.id.avatar_motif_cv)

            // BottomSheetDialog가 보여질 때 블러 처리 적용
            bottomSheetDialog.setOnShowListener {
                constraintLayout.post {
                    Blurry.with(this@AvatarMotifActivity)
                        .radius(10)
                        .sampling(8)
                        .color(Color.argb(0, 0, 0, 0))
                        .async()
                        .animate(500)
                        .onto(constraintLayout)
                }
            }

            // BottomSheetDialog가 사라질 때 블러 처리 제거
            bottomSheetDialog.setOnDismissListener {
                Blurry.delete(constraintLayout)
            }

            bottomSheetDialog.show()
        }

        setContentView(binding.root)
    }

    //dp를 pixel로 변환
    fun Int.dpToPx(): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density).toInt()
    }
}