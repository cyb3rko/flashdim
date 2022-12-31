package com.cyb3rko.flashdim.seekbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.cyb3rko.flashdim.R
import kotlin.math.roundToInt

open class LightLevelSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mCanvasWidth = 0
    private var mCanvasHeight = 0
    private val mLevelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mLevelRect = RectF()
    private lateinit var progressBitmap: Bitmap

    var onProgressChanged: SeekBarChangeListener? = null

    var maxProgress: Int = 1
        set(value) {
            field = value
            progress = value
            invalidate()
        }

    private var progress: Int = 1
        set(value) {
            field = value
            invalidate()
        }

    private var levelBackgroundColor: Int = Color.LTGRAY

    private var levelCornerRadius: Float = calculateDp(4)

    private var levelGap: Float = calculateDp(2)

    private var levelProgressColor: Int = Color.WHITE

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LightLevelSeekBar)
        levelBackgroundColor = typedArray.getColor(
            R.styleable.LightLevelSeekBar_level_background_color, levelBackgroundColor
        )
        levelCornerRadius = typedArray.getDimension(
            R.styleable.LightLevelSeekBar_level_corner_radius, levelCornerRadius
        )
        levelGap = typedArray.getDimension(R.styleable.LightLevelSeekBar_level_gap, levelGap)
        levelProgressColor = typedArray.getColor(
            R.styleable.LightLevelSeekBar_level_progress_color, levelProgressColor
        )
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasWidth = w
        mCanvasHeight = h
        progressBitmap =
            Bitmap.createBitmap(getAvailableWidth(), getAvailableHeight(), Bitmap.Config.ARGB_8888)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipRect(
            paddingLeft,
            paddingTop,
            mCanvasWidth,
            mCanvasHeight
        )
        val availableWidth = getAvailableWidth()
        val availableHeight = getAvailableHeight()
        val totalLevelHeight = availableHeight / maxProgress
        val levelHeight = totalLevelHeight - levelGap

        var previousLevelBottom = paddingTop.toFloat()

        for (i in 0 until maxProgress) {
            mLevelRect.set(
                paddingLeft.toFloat(),
                previousLevelBottom,
                availableWidth.toFloat() + paddingRight.toFloat(),
                previousLevelBottom + levelHeight
            )
            if (i + 1 <= progress) {
                mLevelPaint.color = levelBackgroundColor
            } else {
                mLevelPaint.color = levelProgressColor
            }
            canvas.drawRoundRect(mLevelRect, levelCornerRadius, levelCornerRadius, mLevelPaint)
            previousLevelBottom = mLevelRect.bottom + levelGap
        }
    }

    private fun updateProgress(event: MotionEvent) {
        if (getRawProgress(event) != progress) {
            progress = getRawProgress(event)
            onProgressChanged?.onProgressChanged(maxProgress - progress)
        }
    }

    private fun getRawProgress(event: MotionEvent): Int {
        return (maxProgress * event.y / getAvailableHeight()).roundToInt()
    }

    internal fun setProgress(progress: Int) {
        this.progress = maxProgress - progress
    }

    private fun getAvailableWidth(): Int {
        var width = mCanvasWidth - paddingLeft - paddingRight
        if (width <= 0) width = 1
        return width
    }

    private fun getAvailableHeight(): Int {
        var height = mCanvasHeight - paddingTop - paddingBottom
        if (height <= 0) height = 1
        return height
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled)
            return false

        event?.let {
            updateProgress(it)
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun calculateDp(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }
}
