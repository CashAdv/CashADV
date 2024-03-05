package customcircle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import app.cashadvisor.customView.multipleCircleProgressBar.dp
import app.cashadvisor.uikit.R

class CircularDiagramFive(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val paintFirstCircle = Paint()
    private val paintSecondCircle = Paint()

    private val paintPeriod = Paint()
    private val paintWord = Paint()

    private var firstCircleColor = context.getColor(R.color.subcolour1)
    private var secondCircleColor = context.getColor(R.color.m5)

    private var firstTextColor = context.getColor(R.color.colour2)
    private var secondTextColor = context.getColor(R.color.subcolour2)

    private var firstTextSize = STANDARD_TEXT_SIZE_BIG
    private var secondTextSize = STANDARD_TEXT_SIZE_SMALL

    private var strokeWidthFirst = STANDARD_STROKE_SIZE_SMALL
    private var strokeWidthSecond = STANDARD_STROKE_SIZE_BIG

    private var sweepAngle = 0f

    private var fontFirst = UNDEFINED_FONT
    private var fontSecond = UNDEFINED_FONT

    private var periodCount = "0/0"
    private var measure = context.getString(R.string.months)
    private var textSpacing = dp(DEFAULT_SPACING_BETWEEN_TEXT_DP)

    private val boundOne = Rect()
    private val boundTwo = Rect()

    init {
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.CircularDiagramFive)
            firstCircleColor = typedArray.getColor(
                R.styleable.CircularDiagramFive_cdf_firstCircleColor,
                firstCircleColor
            )
            secondCircleColor = typedArray.getColor(
                R.styleable.CircularDiagramFive_cdf_secondCircleColor,
                secondCircleColor
            )
            firstTextColor = typedArray.getColor(
                R.styleable.CircularDiagramFive_cdf_firstTextColor,
                firstTextColor
            )
            secondTextColor = typedArray.getColor(
                R.styleable.CircularDiagramFive_cdf_secondTextColor,
                secondTextColor
            )
            firstTextSize = typedArray.getDimension(
                R.styleable.CircularDiagramFive_cdf_firstTextSize,
                firstTextSize
            )
            secondTextSize = typedArray.getDimension(
                R.styleable.CircularDiagramFive_cdf_secondTextSize,
                secondTextSize
            )
            strokeWidthFirst = typedArray.getDimension(
                R.styleable.CircularDiagramFive_cdf_strokeWidthFirst,
                strokeWidthFirst
            )
            strokeWidthSecond = typedArray.getDimension(
                R.styleable.CircularDiagramFive_cdf_strokeWidthSecond,
                strokeWidthSecond
            )
            measure = typedArray.getString(R.styleable.CircularDiagramFive_cdf_measure) ?: measure
            fontFirst = typedArray.getResourceId(
                R.styleable.CircularDiagramFive_cdf_fontFamilyFirst,
                fontFirst
            )
            fontSecond = typedArray.getResourceId(
                R.styleable.CircularDiagramFive_cdf_fontFamilySecond,
                fontSecond
            )
            textSpacing = typedArray.getDimension(
                R.styleable.CircularDiagramFive_cdf_textSpacing,
                textSpacing
            )

            typedArray.recycle()
        }

        paintFirstCircle.apply {
            style = Paint.Style.STROKE
            strokeWidth = strokeWidthFirst
            color = firstCircleColor
            isAntiAlias = true
        }

        paintSecondCircle.apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = strokeWidthSecond
            color = secondCircleColor
            isAntiAlias = true
        }

        paintPeriod.apply {
            color = firstTextColor
            textAlign = Paint.Align.CENTER
            textSize = firstTextSize
            isAntiAlias = true
            if (fontFirst != UNDEFINED_FONT) {
                typeface = ResourcesCompat.getFont(context, fontFirst)
            }
        }

        paintWord.apply {
            color = secondTextColor
            textAlign = Paint.Align.CENTER
            textSize = secondTextSize
            isAntiAlias = true
            if (fontSecond != UNDEFINED_FONT) {
                typeface = ResourcesCompat.getFont(context, fontSecond)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val circleRadius = width.coerceAtMost(height) / 2f - strokeWidthSecond / 2

        val xPos = width / 2f
        val yPos = height / 2f

        paintPeriod.getTextBounds(periodCount, 0, periodCount.length, boundOne)
        val heightWordOne = boundOne.height()

        paintWord.getTextBounds(measure, 0, measure.length, boundTwo)
        val heightWordTwo = boundTwo.height()

        val yPosTextOne = yPos - (paintPeriod.descent() + paintPeriod.ascent()) / 2

        val yPosTextTwoCenter =
            yPos + heightWordOne / 2 + heightWordTwo / 2 + textSpacing

        val yPosTextTwo = yPosTextTwoCenter - (paintWord.descent() + paintWord.ascent()) / 2

        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            circleRadius,
            paintFirstCircle
        )
        canvas.drawText(periodCount, xPos, yPosTextOne, paintPeriod)
        canvas.drawText(measure, xPos, yPosTextTwo, paintWord)

        canvas.drawArc(
            xPos - circleRadius,
            yPos - circleRadius,
            xPos + circleRadius,
            yPos + circleRadius,
            START_POINT,
            sweepAngle,
            false,
            paintSecondCircle
        )
    }

    fun setValue(current: Int, total: Int) {
        periodCount = "$current/$total"
        sweepAngle = (360 / total) * current.toFloat()
        invalidate()
    }

    companion object {
        const val STANDARD_TEXT_SIZE_BIG = 120f
        const val STANDARD_TEXT_SIZE_SMALL = 40f
        const val STANDARD_STROKE_SIZE_BIG = 30f
        const val STANDARD_STROKE_SIZE_SMALL = 10f
        const val START_POINT = 270f
        const val UNDEFINED_FONT = 0
        const val DEFAULT_SPACING_BETWEEN_TEXT_DP = 5
    }
}