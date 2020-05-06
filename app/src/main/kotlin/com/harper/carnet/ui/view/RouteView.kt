package com.harper.carnet.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.harper.carnet.R
import com.mapbox.android.gestures.Utils


class RouteView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, styleDef: Int = 0
) : View(context, attributeSet, styleDef) {
    private val paint: Paint = Paint().apply {
        strokeWidth = Utils.dpToPx(4f)
    }

    init {
        with(context.obtainStyledAttributes(attributeSet, R.styleable.RouteView)) {
            paint.color =
                getColor(R.styleable.RouteView_routeTint, ContextCompat.getColor(context, R.color.colorPrimary))
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defWidth = CIRCLE_RADIUS.toInt() * 2
        val width = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> defWidth
            MeasureSpec.AT_MOST -> Math.min(MeasureSpec.getSize(widthMeasureSpec), defWidth)
            else -> defWidth
        }

        super.setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawCircle(CIRCLE_RADIUS, CIRCLE_RADIUS, CIRCLE_RADIUS, paint)
        canvas.drawCircle(CIRCLE_RADIUS, height - CIRCLE_RADIUS, CIRCLE_RADIUS, paint)
        canvas.drawLine(CIRCLE_RADIUS, CIRCLE_RADIUS, CIRCLE_RADIUS, height - CIRCLE_RADIUS, paint)

        canvas.restore()
    }

    companion object {
        private const val CIRCLE_RADIUS = 10f
    }
}