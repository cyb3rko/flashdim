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
    private val mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mWaveRect = RectF()
    private lateinit var progressBitmap: Bitmap

    var onProgressChanged: SeekBarChangeListener? = null

    var maxProgress: Float = 1F
        set(value) {
            field = value
            progress = value
            invalidate()
        }

    private var progress: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var levelBackgroundColor: Int = Color.LTGRAY

    private var levelProgressColor: Int = Color.WHITE

    private var levelGap: Float = calculateDp(2)

    private var levelCornerRadius: Float = calculateDp(4)

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
        super.onDraw(canvas)
        canvas.clipRect(
            paddingLeft,
            paddingTop,
            mCanvasWidth - paddingRight,
            mCanvasHeight - paddingBottom
        )
        val availableWidth = getAvailableWidth()
        val availableHeight = getAvailableHeight()
        val totalWaveHeight = availableHeight / maxProgress
        val waveHeight = totalWaveHeight - levelGap

        var previousWaveBottom = paddingTop.toFloat()

        val progressYPosition: Float = availableHeight * progress / maxProgress

        for (i in 0 until maxProgress.roundToInt()) {
            mWaveRect.set(
                paddingLeft.toFloat(),
                previousWaveBottom,
                availableWidth.toFloat() + paddingRight.toFloat(),
                previousWaveBottom + waveHeight
            )
            if (mWaveRect.centerY() <= progressYPosition) {
                mWavePaint.color = levelBackgroundColor
            } else {
                mWavePaint.color = levelProgressColor
            }
            canvas.drawRoundRect(mWaveRect, levelCornerRadius, levelCornerRadius, mWavePaint)
            previousWaveBottom = mWaveRect.bottom + levelGap
        }
    }

    private fun updateProgress(event: MotionEvent) {
        progress = getRawProgress(event)
        onProgressChanged?.onProgressChanged(maxProgress - progress)
    }

    private fun getRawProgress(event: MotionEvent) = maxProgress * event.y / getAvailableHeight()

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
