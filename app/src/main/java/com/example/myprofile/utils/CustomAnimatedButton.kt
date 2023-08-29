package com.example.myprofile.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import com.example.myprofile.R
import com.google.android.material.button.MaterialButton

// Custom button with centered text and icon.
// Icon can be animated when touched
class CustomAnimatedButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    private var isAnimated = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomAnimatedButton,
            0, 0
        ).apply {
            try {
                isAnimated = getBoolean(R.styleable.CustomAnimatedButton_isAnimated, false)
                setupIcon(this)
                setupText(this)
                setupBackground(this)
            } finally {
                recycle()
            }
        }
    }

    fun isIconAnimated(): Boolean {
        return isAnimated
    }

    fun setText(customText: String) {
        text = customText
        invalidate()
        requestLayout()
    }

    fun setButtonBackgroundColor(customBackground: Int) {
        setBackgroundColor(customBackground)
        invalidate()
        requestLayout()
    }

    fun setButtonIcon(customIcon: Drawable) {
        icon = customIcon
        invalidate()
        requestLayout()
    }

    private fun setupIcon(ta: TypedArray) {
        val customIcon = ta.getDrawable(R.styleable.CustomAnimatedButton_buttonIcon)
        icon = customIcon
        iconTint = null
        iconGravity = ICON_GRAVITY_TEXT_START
    }

    private fun setupText(ta: TypedArray) {
        val customText = ta.getString(R.styleable.CustomAnimatedButton_buttonText)
        text = customText
        gravity = Gravity.CENTER
    }

    private fun setupBackground(ta: TypedArray) {
        val customBackground =
            ta.getColor(R.styleable.CustomAnimatedButton_buttonBackgroundColor, 0)
        setBackgroundColor(customBackground)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startDrawableAnimation()
            }
        }
        return super.onTouchEvent(event)
    }

    // makes the height of the button big enough to fit the animated icon
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (icon == null) return

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // multiplies height by the coefficient so as not to go beyond the background
        val minButtonHeight = (icon.intrinsicHeight.times(ANIM_SCALE_COEF * SCALE_FACTOR)).toInt()
        val textSize = textSize.times(ANIM_SCALE_COEF * SCALE_FACTOR).toInt()
        val minHeight = kotlin.math.max(textSize, minButtonHeight)

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            // if layout_height="wrap_content" -> the button height should be al least minHeight
            MeasureSpec.AT_MOST -> minHeight.let {
                kotlin.math.min(
                    it,
                    heightSize
                )
            }

            else -> minButtonHeight
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }

    private fun startDrawableAnimation() {
        if (!isAnimated) return
        val animator = ValueAnimator.ofFloat(INITIAL_VALUE, ANIM_SCALE_COEF, INITIAL_VALUE)

        animator.apply {
            duration = ANIM_DURATION
            repeatCount = 1
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                scaleDrawable(animatedValue)
            }
            start()
        }

    }

    // enlarges the icon scale times
    private fun scaleDrawable(scale: Float) {
        val width = (icon?.intrinsicWidth?.times(scale))?.toInt()
        val height = (icon?.intrinsicHeight?.times(scale))?.toInt()

        if (width != null && height != null) {
            val scaledDrawable = icon?.mutate()?.apply {
                setBounds(0, 0, width, height)
            }
            icon = scaledDrawable
        }
    }

    companion object {
        private const val INITIAL_VALUE = 1f
        private const val ANIM_SCALE_COEF = 1.2f
        private const val SCALE_FACTOR = 1.5f
        private const val ANIM_DURATION = 1000L
    }

}

