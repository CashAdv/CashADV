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
import java.lang.Integer.max

class CustomViewDiagramThird @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.CustomViewDiagramThirdStyle
) : View(context, attributesSet, defStyleAttr, defStyleRes) {

    private var barWidth: Float = 0f
    private var barMaxWidth = 0f

    private var backgroundProgressColor = Color.GRAY
    private var progressColor = Color.GREEN
    private var textProgressColor = Color.WHITE
    private var textSizeProgress = DEFAULT_TEXT_SIZE

    private var paddingProgressFromBackground = BACKGROUND_PROGRESS_PADDING
    private var progressTextPadding = PROGRESS_TEXT_PADDING
    private var cornerRadius = DEFAULT_CORNER_RADIUS

    private lateinit var rectPaint: Paint
    private lateinit var progressRectPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var textFontProgress: Typeface

    private val backgroundSizeField = RectF(0f, 0f, 0f, 0f)
    private val progressSizeField = RectF(0f, 0f, 0f, 0f)

    var progress: Int = 0
        set(value) {
            field = value.coerceIn(0, 100)
            barWidth = barMaxWidth * (field) / 100f
            invalidate()
        }

    init {
        initAttributes(attributesSet, defStyleAttr, defStyleRes)
        initPaint()
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {

        val typedArray = context.obtainStyledAttributes(
            attributesSet, R.styleable.CustomViewDiagramThird, defStyleAttr, defStyleRes
        ).apply {
            backgroundProgressColor = getColor(
                R.styleable.CustomViewDiagramThird_dt_colorBackground, Color.GRAY
            )
            progressColor = getColor(R.styleable.CustomViewDiagramThird_dt_colorProgress, Color.GREEN)
            cornerRadius =
                getDimension(R.styleable.CustomViewDiagramThird_dt_radiusView, DEFAULT_CORNER_RADIUS)
            textProgressColor =
                getColor(R.styleable.CustomViewDiagramThird_dt_textColorProgress, Color.WHITE)
            textSizeProgress = getDimension(
                R.styleable.CustomViewDiagramThird_dt_textSizeProgress, DEFAULT_TEXT_SIZE
            )
        }
        val textFontId =
            typedArray.getResourceId(R.styleable.CustomViewDiagramThird_dt_textFontProgress, 0)
        textFontProgress = if (textFontId != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Typeface.create(resources.getFont(textFontId), Typeface.NORMAL)
            } else {
                Typeface.DEFAULT
            }
        } else {
            Typeface.DEFAULT
        }

        paddingProgressFromBackground = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, BACKGROUND_PROGRESS_PADDING ,
            resources.displayMetrics
        )
        progressTextPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, PROGRESS_TEXT_PADDING, resources.displayMetrics
        )
        typedArray.recycle()
    }

    private fun initPaint() {
        rectPaint = Paint().apply {
            isAntiAlias = true
            color = backgroundProgressColor
        }
        progressRectPaint = Paint().apply {
            isAntiAlias = true
            color = progressColor
        }
        textPaint = Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
            color = textProgressColor
            textSize = textSizeProgress
            typeface = textFontProgress
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
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
        barMaxWidth = width.toFloat() - progressSizeField.height() - paddingProgressFromBackground
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

        val xPos = progressSizeField.left + progressTextPadding + textPaint.measureText(progress.toString())
        val yPos = height / 2f
        val yPosText = (yPos - ((textPaint.descent() + textPaint.ascent()) / 2))
        progressSizeField.right = progressSizeField.height() + barWidth

        canvas.drawRoundRect(
            backgroundSizeField, cornerRadius, cornerRadius, rectPaint
        )

        if (progress != 0) {
            canvas.drawRoundRect(
                progressSizeField, cornerRadius, cornerRadius, progressRectPaint
            )
        }

        canvas.drawText(
            progress.toString(), xPos, yPosText, textPaint
        )
    }


    companion object {

        const val BACKGROUND_PROGRESS_PADDING  = 1f
        const val PROGRESS_TEXT_PADDING = 3f
        const val DEFAULT_CORNER_RADIUS = 10f
        const val DEFAULT_TEXT_SIZE = 11F
        const val DEFAULT_HEIGHT = 15f
        const val DEFAULT_WIDTH = 150f
    }

}