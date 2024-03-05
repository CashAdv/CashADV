package app.cashadvisor.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import app.cashadvisor.uikit.R
import kotlin.math.max


class CustomViewDiagramFourth @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.CustomViewDiagramFourthStyle
) : View(context, attributesSet, defStyleAttr, defStyleRes) {

    private var barWidth: Float = 0f
    private var barMaxWidth = 0f

    private var backgroundProgressColor = Color.GRAY
    private var progressColor = Color.GREEN
    private var progressTextColor = Color.WHITE
    private var progressTextSize = DEFAULT_TEXT_SIZE

    private var progressPaddingBackground = PROGRESS_PADDING_BACKGROUND
    private var progressTextPadding = PROGRESS_PADDING_TEXT
    private var cornerRadius = DEFAULT_CORNER_RADIUS

    private lateinit var paintRect: Paint
    private lateinit var paintProgressRect: Paint
    private lateinit var paintText: Paint
    private lateinit var textFontProgress: Typeface

    private val backgroundSizeField = RectF(0f, 0f, 0f, 0f)
    private val progressSizeField = RectF(0f, 0f, 0f, 0f)

    var progress: Int = 0
        set(value) {
            field = value.coerceIn(0, 100)
            barWidth = barMaxWidth * (100 - field) / 100f
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
            backgroundProgressColor = getColor(
                R.styleable.CustomViewDiagramFourth_colorBackground, Color.GRAY
            )
            progressColor = getColor(R.styleable.CustomViewDiagramFourth_colorProgress, Color.GREEN)
            cornerRadius =
                getDimension(R.styleable.CustomViewDiagramFourth_radiusView, DEFAULT_CORNER_RADIUS)
            progressTextColor =
                getColor(R.styleable.CustomViewDiagramFourth_textColorProgress, Color.WHITE)
            progressTextSize = getDimension(
                R.styleable.CustomViewDiagramFourth_textSizeProgress, DEFAULT_TEXT_SIZE
            )
        }
        val textFontId =
            typedArray.getResourceId(R.styleable.CustomViewDiagramFourth_textFontProgress, 0)
        textFontProgress = if (textFontId != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Typeface.create(resources.getFont(textFontId), Typeface.NORMAL)
            } else {
                Typeface.DEFAULT
            }
        } else {
            Typeface.DEFAULT
        }

        progressPaddingBackground = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, PROGRESS_PADDING_BACKGROUND, resources.displayMetrics
        )
        progressTextPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, PROGRESS_PADDING_TEXT, resources.displayMetrics
        )
        typedArray.recycle()
    }

    private fun initPaint() {
        paintRect = Paint().apply {
            isAntiAlias = true
            color = backgroundProgressColor
        }
        paintProgressRect = Paint().apply {
            isAntiAlias = true
            color = progressColor
        }
        paintText = Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
            color = progressTextColor
            textSize = progressTextSize
            typeface = textFontProgress
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
            left = paddingLeft.toFloat() + progressPaddingBackground
            top = paddingTop.toFloat() + progressPaddingBackground
            right = width - paddingRight - progressPaddingBackground
            bottom = height - paddingBottom - progressPaddingBackground
        }
        barMaxWidth = width.toFloat() - progressSizeField.height()
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

        val xPos = progressSizeField.right - progressTextPadding - progressPaddingBackground
        val yPos = height / 2f
        val yPosText = (yPos - ((paintText.descent() + paintText.ascent()) / 2))
        progressSizeField.left = paddingLeft.toFloat() + progressPaddingBackground + barWidth

        canvas.drawRoundRect(
            backgroundSizeField, cornerRadius, cornerRadius, paintRect
        )

        if (progress != 0) {
            canvas.drawRoundRect(
                progressSizeField, cornerRadius, cornerRadius, paintProgressRect
            )
        }

        canvas.drawText(
            progress.toString(), xPos, yPosText, paintText
        )
    }

    companion object {
        const val PROGRESS_PADDING_BACKGROUND = 1f
        const val PROGRESS_PADDING_TEXT = 5f
        const val DEFAULT_CORNER_RADIUS = 10f
        const val DEFAULT_TEXT_SIZE = 11F
        const val DEFAULT_HEIGHT = 15f
        const val DEFAULT_WIDTH = 150f
    }
}