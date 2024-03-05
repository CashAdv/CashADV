package app.cashadvisor.uikit.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import app.cashadvisor.uikit.R

class CircularGraph(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {

    private val paint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)

    private var fontResourceId = UNDEFINED_FONT
    private var totalValueText = ""
    private var headerText = ""
    private var currencySymbol = context.getString(R.string.circular_graph_unknown_text)
    private var strokeWidth = DEFAULT_WIDTH

    private var color1 = context.getColor(R.color.subcolour1)
    private var color2 = context.getColor(R.color.m6)
    private var textColor1 = context.getColor(R.color.colour1)
    private var textColor2 = textColor1

    private var textSize = DEFAULT_TEXT_SIZE

    private var biasAngle = DEFAULT_BIAS_ANGLE

    private var startAngle1 = 0f
    private var sweepAngle1 = TWO_PI_ANGLE
    private var startAngle2 = 0f
    private var sweepAngle2 = 0f

    init {
        paint1.style = Paint.Style.STROKE
        paint1.strokeCap = Paint.Cap.ROUND

        paint2.style = Paint.Style.STROKE
        paint2.strokeCap = Paint.Cap.ROUND

        textPaint1.textAlign = Paint.Align.CENTER
        textPaint2.textAlign = Paint.Align.CENTER

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularGraph)

            color1 = typedArray.getColor(R.styleable.CircularGraph_cg_varSourceArcColor, color1)
            color2 = typedArray.getColor(R.styleable.CircularGraph_cg_constSourceArcColor, color2)
            strokeWidth = typedArray.getDimension(R.styleable.CircularGraph_cg_circleThickness, strokeWidth)
            biasAngle = typedArray.getFloat(R.styleable.CircularGraph_cg_arcSpacing, biasAngle)
            headerText = typedArray.getString(R.styleable.CircularGraph_cg_headerText) ?: headerText
            textSize = typedArray.getDimension(R.styleable.CircularGraph_cg_captionTextSize, textSize)
            currencySymbol = typedArray.getString(R.styleable.CircularGraph_cg_currencySymbol) ?: currencySymbol
            fontResourceId = typedArray.getResourceId(R.styleable.CircularGraph_android_fontFamily, fontResourceId)
            textColor1 = typedArray.getColor(R.styleable.CircularGraph_cg_headerTextColor, textColor1)
            textColor2 = typedArray.getColor(R.styleable.CircularGraph_cg_valueTextColor, textColor2)

            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint1.strokeWidth = strokeWidth
        paint1.color = color1

        paint2.strokeWidth = strokeWidth
        paint2.color = color2

        textPaint1.textSize = textSize
        textPaint2.textSize = textSize

        if (fontResourceId != UNDEFINED_FONT) {
            val customFont = ResourcesCompat.getFont(context, fontResourceId)
            textPaint1.typeface = customFont
            textPaint2.typeface = customFont
        }

        textPaint1.color = textColor1
        textPaint2.color = textColor2


        val centerX = width / 2f
        val centerY = height / 2f

        val circleRadius = width.coerceAtMost(height) / 2 - 0.5f * strokeWidth

        canvas.drawArc(
            centerX - circleRadius,
            centerY - circleRadius,
            centerX + circleRadius,
            centerY + circleRadius,
            startAngle1,
            sweepAngle1,
            false,
            paint1
        )

        canvas.drawArc(
            centerX - circleRadius,
            centerY - circleRadius,
            centerX + circleRadius,
            centerY + circleRadius,
            startAngle2,
            sweepAngle2,
            false,
            paint2
        )

        val textCenterBias = centerY / 8

        val textY1 = centerY - (textPaint1.descent() + textPaint1.ascent()) / 2 - textCenterBias
        canvas.drawText(headerText, centerX, textY1, textPaint1)

        val textY2 = centerY - (textPaint2.descent() + textPaint2.ascent()) / 2 + textCenterBias
        canvas.drawText(totalValueText, centerX, textY2, textPaint2)
    }

    fun setNewValues(varValue: Float, constValue: Float) {

        val sum = varValue + constValue

        val sumFormat = String.format("%.2f", sum)

        val varValueRatio = varValue / sum
        val constValueRatio = constValue / sum

        totalValueText = "$currencySymbol $sumFormat"

        val varValueAngle = varValueRatio * (360 - 2 * biasAngle)
        val constValueAngle = constValueRatio * (360 - 2 * biasAngle)

        if (constValueAngle <= biasAngle) {
            startAngle1 = 0f + biasAngle
            sweepAngle1 = varValueAngle
            sweepAngle2 = 0f
        } else if (varValueAngle <= biasAngle) {
            startAngle1 = 0f
            sweepAngle1 = 0f
            startAngle2 = 0f + biasAngle
            sweepAngle2 = constValueAngle
        } else {
            startAngle1 = 0f + biasAngle
            sweepAngle1 = varValueAngle - biasAngle
            startAngle2 = varValueAngle + 2 * biasAngle
            sweepAngle2 = constValueAngle - biasAngle
        }

        invalidate()
    }

    companion object {
        private const val UNDEFINED_FONT = -1
        private const val DEFAULT_WIDTH = 36f
        private const val DEFAULT_TEXT_SIZE = 40f
        private const val DEFAULT_BIAS_ANGLE = 7f
        private const val TWO_PI_ANGLE = 360f
    }
}