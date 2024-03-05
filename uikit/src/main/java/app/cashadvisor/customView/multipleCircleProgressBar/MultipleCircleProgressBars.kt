package app.cashadvisor.customView.multipleCircleProgressBar


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.os.bundleOf
import app.cashadvisor.customView.multipleCircleProgressBar.model.Progress
import app.cashadvisor.customView.multipleCircleProgressBar.model.ProgressCircle


class MultipleCircleProgressBars @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var progressList: List<Progress> = listOf()

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private var listOfMainPaints: List<Paint> = listOf()
    private var listOfTransparentPaints: List<Paint> = listOf()
    private var progressCircleList: List<ProgressCircle> = listOf()

    fun initialiseProgressBar(progressList: List<Progress>) {
        this.progressList = progressList
        initPaints()
        invalidate()
    }

    fun setProgressById(id: Int, amount: Int) {
        val result = progressList.map { progress ->
            if (progress.id == id) {
                progress.copy(value = amount)
            } else progress.copy()
        }
        progressList = result
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val defaultSide = dp(DEFAULT_VIEW_SIZE).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val sideSize = widthSize.coerceAtMost(heightSize)


        val width = when (widthMode) {
            MeasureSpec.AT_MOST -> sideSize.coerceAtMost(defaultSide)
            MeasureSpec.EXACTLY -> sideSize
            else -> defaultSide
        }

        val height = when (heightMode) {
            MeasureSpec.AT_MOST -> sideSize.coerceAtMost(defaultSide)
            MeasureSpec.EXACTLY -> sideSize
            else -> defaultSide
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        prepareForDraw()
        drawProgressBar(
            canvas = canvas, progressCircleList = progressCircleList
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                super.onRestoreInstanceState(
                    state.getParcelable(STATE_KEY, BaseSavedState::class.java)
                )
                val savedProgressList =
                    state.getParcelableArray(PROGRESS_LIST_KEY, Progress::class.java)
                if (savedProgressList != null) {
                    progressList = savedProgressList.toList()
                    invalidate()
                }
            } else {
                super.onRestoreInstanceState(state.getParcelable(STATE_KEY))
                val savedProgressList = state.getParcelableArray(PROGRESS_LIST_KEY)
                if (savedProgressList != null) {
                    progressList = savedProgressList.toList() as List<Progress>
                    invalidate()
                }
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return bundleOf(
            STATE_KEY to super.onSaveInstanceState(),
            PROGRESS_LIST_KEY to progressList.toTypedArray()
        )
    }


    private fun prepareForDraw() {
        centerX = width / 2f
        centerY = height / 2f

        if (progressList.isNotEmpty()) {
            val size = width.coerceAtMost(height)

            val strokeWidth = calculateStrokeWidth(size, progressList.size)
            val radiusList = calculateRadiusList(size, strokeWidth, progressList.size)
            applyStrokeWidthToPaints(listOfMainPaints, strokeWidth)
            applyStrokeWidthToPaints(listOfTransparentPaints, strokeWidth)

            progressCircleList = initProgressCircles(
                progressList, listOfMainPaints, listOfTransparentPaints, radiusList
            )
        }
    }

    private fun drawProgressBar(canvas: Canvas, progressCircleList: List<ProgressCircle>) {
        for (progressCircle in progressCircleList) {
            val rectF = RectF(
                centerX - progressCircle.radius.toFloat(),
                centerY - progressCircle.radius.toFloat(),
                centerX + progressCircle.radius.toFloat(),
                centerY + progressCircle.radius.toFloat()
            )
            canvas.drawCircle(
                centerX, centerY, progressCircle.radius.toFloat(), progressCircle.paintBackground
            )

            canvas.drawArc(
                rectF, START_ANGLE, progressCircle.endAngle, false, progressCircle.paintMain
            )
        }
    }

    private fun initPaints() {
        listOfMainPaints = initMainPaints(progressList)
        listOfTransparentPaints = initBackgroundPaints(progressList)
    }

    private fun initMainPaints(progressList: List<Progress>): List<Paint> {
        return progressList.map { progress ->
            Paint().apply {
                color = resources.getColor(
                    progress.mainColor,
                    context.theme
                )
                style = Paint.Style.STROKE
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
            }
        }
    }

    private fun initBackgroundPaints(progressList: List<Progress>): List<Paint> {
        return progressList.map { progress ->
            Paint().apply {
                color = resources.getColor(
                    progress.backgroundColor,
                    context.theme
                )
                style = Paint.Style.STROKE
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
            }
        }
    }

    private fun initProgressCircles(
        progressList: List<Progress>,
        listOfMainPaints: List<Paint>,
        listOfBackgroundPaints: List<Paint>,
        radiusList: List<Int>
    ): List<ProgressCircle> {

        val result: MutableList<ProgressCircle> = mutableListOf()

        for ((index, progress) in progressList.withIndex()) {
            result.add(
                ProgressCircle(
                    paintMain = listOfMainPaints[index],
                    paintBackground = listOfBackgroundPaints[index],
                    endAngle = getAngle(progress.value, progress.maxValue),
                    radius = radiusList[index]
                )
            )
        }
        return result
    }

    private fun calculateRadiusList(width: Int, strokeWidth: Int, count: Int): List<Int> {
        val result = mutableListOf<Int>()
        val maxRadius = width / 2 - (strokeWidth / 2)
        if (count >= 1) {
            result.add(maxRadius)
        }
        var radius = maxRadius
        for (i in 1 until count) {
            radius -= (strokeWidth * 2)
            result.add(radius)
        }
        return result
    }


    private fun calculateStrokeWidth(width: Int, progressCount: Int): Int {
        return (width / 2) / (progressCount * 2)
    }

    private fun getAngle(progress: Int, maxValue: Int): Float {
        return if (progress > maxValue) {
            FULL_CIRCLE_ANGLE
        } else progress * FULL_CIRCLE_ANGLE / maxValue
    }

    private fun applyStrokeWidthToPaints(paints: List<Paint>, strokeWidth: Int) {
        for (paint in paints) {
            paint.strokeWidth = strokeWidth.toFloat()
        }
    }

    companion object {
        const val DEFAULT_VIEW_SIZE = 160
        const val FULL_CIRCLE_ANGLE = 360f
        const val START_ANGLE = 270f

        const val STATE_KEY = "superState"
        const val PROGRESS_LIST_KEY = "progressList"
    }
}