package com.example.mealplanb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.lang.Math.cos
import java.lang.Math.sin

class CustomCircularProgress: View {
    private var progress : Int = 0
    private var progress_color : Int? = null
    private var text : String? = null
    private var textColor : Int? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?,attrs: AttributeSet?) : super(context,attrs) {
        if(attrs != null && context != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCircularProgress)
            progress = typedArray.getInt(R.styleable.CustomCircularProgress_progress, 0)
            progress_color = typedArray.getColor(R.styleable.CustomCircularProgress_progress_color, Color.BLACK)
            text = typedArray.getString(R.styleable.CustomCircularProgress_text)
            textColor = typedArray.getColor(R.styleable.CustomCircularProgress_textColor,Color.BLACK)
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        //width와 height를 기준으로 중심 좌표, 반지름을 설정한다.
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 2f - 40f
        val startAngle = -180f
        val sweepAngle = (180 * progress / 100).toFloat()

        val endX = centerX + radius * cos(Math.toRadians((startAngle + sweepAngle).toDouble())).toFloat()
        val endY = centerY + radius * sin(Math.toRadians((startAngle + sweepAngle).toDouble())).toFloat()
        val endCircleRadius = 40f // 원형 view의 반지름

        val progressPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 20f
            strokeCap = Paint.Cap.ROUND
            color = progress_color ?: Color.BLACK
        }
        val endCirclePaint = Paint().apply {
            style = Paint.Style.FILL
            color = progress_color ?: Color.BLACK
        }
        val textPaint = Paint().apply {
            color = textColor ?: Color.BLACK
            textSize = 24.0f
            textAlign = Paint.Align.CENTER
            typeface = ResourcesCompat.getFont(context,R.font.pretendard_medium)
        }

        val text = text ?: "null"
        val textHeight = (textPaint.descent() + textPaint.ascent()) / 2

        canvas.drawArc(centerX - radius,centerY - radius,centerX + radius,centerY + radius,startAngle,sweepAngle,false, progressPaint)
        canvas.drawCircle(endX, endY, endCircleRadius, endCirclePaint)
        canvas.drawText(text,endX,endY - textHeight,textPaint)
    }

    fun updateProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    fun getText(): String? {
        return text
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }
}