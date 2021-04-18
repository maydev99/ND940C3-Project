package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var valueAnimator = ValueAnimator()
    private var progress = 0
    private var loadingColor = 0


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Completed -> {
                valueAnimator.cancel()
                invalidate()
                requestLayout()
            }

            ButtonState.Clicked -> {
                valueAnimator.start()
                invalidate()
                requestLayout()
            }

            ButtonState.Loading -> {
                valueAnimator.start()
                invalidate()
                requestLayout()
            }

        }
    }

    private var buttonColor: Int = 0
    private var textColor: Int = 0

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 60.0f
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingButtonColor, 0)

        }

        valueAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration = 3000
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
            addUpdateListener {
                progress = this.animatedValue as Int
                invalidate()
                requestLayout()
            }
        }

        buttonState = ButtonState.Completed

    }

    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }

    fun onClickButton() {
        buttonState = ButtonState.Clicked
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (buttonState) {


            ButtonState.Clicked -> {
                drawLoadingButton(canvas)
                drawText(resources.getString(R.string.button_loading), canvas)

            }

            ButtonState.Completed -> {
                drawDefaultButton(canvas)
                drawText(resources.getString(R.string.download), canvas)

            }

            ButtonState.Loading -> {
                drawLoadingButton(canvas)
                drawText(resources.getString(R.string.button_loading), canvas)
            }


        }


    }

    private fun drawLoadingButton(canvas: Canvas) {
        paint.color = buttonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = loadingColor
        canvas.drawRect(
            0f,
            0f,
            (widthSize * (progress.toDouble() / 100)).toFloat(),
            heightSize.toFloat(),
            paint
        )

        // Draw Arc
        paint.color = Color.YELLOW
        canvas.drawArc(
            widthSize - 170.toFloat(),
            10.0f, widthSize - 30.toFloat(),
            heightSize - 10.toFloat(),
            360f,
            (360f * progress.toDouble() / 100).toFloat(),
            true,
            paint
        )


    }

    private fun drawDefaultButton(canvas: Canvas) {
        paint.color = buttonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawText(text: String, canvas: Canvas) {
        paint.color = textColor
        canvas.drawText(
            text,
            (widthSize / 2).toFloat(),
            (heightSize / 2) + (heightSize / 10).toFloat(),
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


}