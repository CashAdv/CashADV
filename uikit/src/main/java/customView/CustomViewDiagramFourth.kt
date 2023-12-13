package app.cashadvisor.uikit.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import app.cashadvisor.uikit.R
import kotlin.math.max
import kotlin.properties.Delegates


class CustomViewDiagramFourth @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.CustomViewDiagramFourthStyle
) : View(context, attributesSet, defStyleAttr, defStyleRes) {

    private var widthBar: Float = 0f
    private var maxWidthBar = 0f

    private var colorBackgroundProgress by Delegates.notNull<Int>()
    private var colorProgress by Delegates.notNull<Int>()
    private var paddingProgressFromBackground by Delegates.notNull<Float>()
    private var cornerRadius by Delegates.notNull<Float>()

    private lateinit var rectPaint: Paint
    private lateinit var rectPaintProgress: Paint

    private val backgroundSizeField = RectF(0f, 0f, 0f, 0f)
    private val progressSizeField = RectF(0f, 0f, 0f, 0f)

    var progress: Int = 0
        set(value) {
            field = value.coerceIn(0, 100)
            widthBar = maxWidthBar * (100 - field) / 100f
            invalidate()
        }

    init {
        initAttributes(attributesSet, defStyleAttr, defStyleRes)
        initPaint()
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {

        val typedArray = context.obtainStyledAttributes(
            attributesSet, R.styleable.CustomViewDiagramFourth, defStyleAttr, defStyleRes
        ).apply {
            colorBackgroundProgress = getColor(
                R.styleable.CustomViewDiagramFourth_colorBackground, Color.GRAY
            )
            colorProgress = getColor(R.styleable.CustomViewDiagramFourth_colorProgress, Color.GREEN)
            cornerRadius =
                getDimension(R.styleable.CustomViewDiagramFourth_radiusView, DEFAULT_CORNER_RADIUS)
        }

        paddingProgressFromBackground = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, PADDING_PROGRESS_BACKGROUND, resources.displayMetrics
        )
        typedArray.recycle()
    }

    private fun initPaint() {
        rectPaint = Paint().apply {
            isAntiAlias = true
            color = colorBackgroundProgress
        }
        rectPaintProgress = Paint().apply {
            isAntiAlias = true
            color = colorProgress
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {

        backgroundSizeField.apply {
            left = paddingLeft.toFloat()
            top = paddingTop.toFloat()
            right = width - paddingRight.toFloat()
            bottom = height - paddingBottom.toFloat()
        }
        progressSizeField.apply {
            left = paddingLeft.toFloat() + paddingProgressFromBackground
            top = paddingTop.toFloat() + paddingProgressFromBackground
            right = width - paddingRight - paddingProgressFromBackground
            bottom = height - paddingBottom - paddingProgressFromBackground
        }
        maxWidthBar = width.toFloat() - progressSizeField.height()
        progress = progress
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredHeightSizeInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEIGHT, resources.displayMetrics
        ).toInt()
        val desiredWidthSizeInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DEFAULT_WIDTH, resources.displayMetrics
        ).toInt()

        val desiredWidth = max(minWidth, desiredWidthSizeInPixels + paddingLeft + paddingRight)
        val desiredHeight = max(minHeight, desiredHeightSizeInPixels + paddingTop + paddingBottom)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        progressSizeField.left = paddingLeft.toFloat() + paddingProgressFromBackground + widthBar

        canvas.drawRoundRect(
            backgroundSizeField, cornerRadius, cornerRadius, rectPaint
        )
        if (progress == 0) return
        canvas.drawRoundRect(
            progressSizeField, cornerRadius, cornerRadius, rectPaintProgress
        )
    }

    companion object {
        const val PADDING_PROGRESS_BACKGROUND = 1f
        const val DEFAULT_CORNER_RADIUS = 10F
        const val DEFAULT_HEIGHT = 15f
        const val DEFAULT_WIDTH = 150f
    }
}