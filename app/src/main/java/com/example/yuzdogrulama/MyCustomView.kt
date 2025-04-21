package com.example.yuzdogrulama

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class MyCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ellipsePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val crossPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = 1.dpToPx(context).toFloat()
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(4.dpToPx(context).toFloat(), 3.dpToPx(context).toFloat()), 2f)
    }

    private val rect = RectF()
    private var centerX = 0f
    private var centerY = 0f

    private var margin = 40.dpToPx(context)

    private var currentMode = DrawMode.CURVED_HORIZONTAL_LEFT

    enum class DrawMode {
        NORMAL,
        CURVED_VERTICAL,
        // yukarı bakar BOTH ile aynı CURVED_HORIZONTAL,
        CURVED_BOTH,
        CURVED_VERTICAL_DOWN,
        CURVED_HORIZONTAL_LEFT
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Elipsin konumunu hesapla
        val ovalWidth = width * 0.6f
        val ovalHeight = height * 0.4f
        val left = (width - ovalWidth) / 2f
        val top = (height - ovalHeight) / 2f
        val right = left + ovalWidth
        val bottom = top + ovalHeight

        rect.set(left, top, right, bottom)

        centerX = rect.centerX()
        centerY = rect.centerY()

        // Seçili moda göre çizimi çağır
        when (currentMode) {
            DrawMode.NORMAL -> drawNormal(canvas)
            DrawMode.CURVED_VERTICAL -> drawCurvedVertical(canvas)
            //DrawMode.CURVED_HORIZONTAL -> drawCurvedHorizontal(canvas)
            DrawMode.CURVED_BOTH -> drawCurvedBoth(canvas)
            DrawMode.CURVED_VERTICAL_DOWN -> drawCurvedHorizontalDown(canvas)
            DrawMode.CURVED_HORIZONTAL_LEFT -> drawCurvedVerticalLeft(canvas)
        }
    }

    private fun drawNormal(canvas: Canvas) {
        canvas.drawArc(rect, 0f, 360f, false, crossPaint)
        canvas.drawLine(rect.left, centerY, rect.right, centerY, crossPaint)
        canvas.drawLine(centerX, rect.top, centerX, rect.bottom, crossPaint)
    }

    private fun drawCurvedVertical(canvas: Canvas) {
        canvas.drawArc(rect, 0f, 360f, false, crossPaint)
        canvas.drawLine(rect.left, centerY, rect.right, centerY, crossPaint)

        val path = Path()
        path.moveTo(centerX, rect.top)
        path.quadTo(centerX + margin, centerY, centerX, rect.bottom)
        canvas.drawPath(path, crossPaint)
    }

    private fun drawCurvedVerticalLeft(canvas: Canvas) {

        canvas.drawArc(rect, 0f, 360f, false, crossPaint)
        val path = Path()
        path.moveTo(centerX, rect.top)
        path.quadTo(centerX - margin, centerY, centerX, rect.bottom)
        canvas.drawPath(path, crossPaint)
        canvas.drawLine(rect.left, centerY, rect.right, centerY, crossPaint)



    }
    /* private fun drawCurvedHorizontal(canvas: Canvas) {
         canvas.drawArc(rect, 0f, 360f, false, crossPaint)
         canvas.drawLine(centerX, rect.top, centerX, rect.bottom, crossPaint)

         val path = Path()
         path.moveTo(rect.left, centerY)
         path.quadTo(centerX, centerY - 100f, rect.right, centerY)
         canvas.drawPath(path, crossPaint)
     }*/

    private fun drawCurvedBoth(canvas: Canvas) {
        canvas.drawArc(rect, 0f, 360f, false, crossPaint)

        val hPath = Path()
        hPath.moveTo(rect.left, centerY)
        hPath.quadTo(centerX, centerY - margin, rect.right, centerY)
        canvas.drawPath(hPath, crossPaint)

        val vPath = Path()
        vPath.moveTo(centerX, rect.top)
        vPath.lineTo(centerX, rect.bottom)
        canvas.drawPath(vPath, crossPaint)
    }

    private fun drawCurvedHorizontalDown(canvas: Canvas) {
        canvas.drawArc(rect, 0f, 360f, false, crossPaint)
        canvas.drawLine(centerX, rect.top, centerX, rect.bottom, crossPaint)

        val path = Path()
        path.moveTo(rect.left, centerY)
        path.quadTo(centerX, centerY + margin, rect.right, centerY) // Aşağı bakan bombe
        canvas.drawPath(path, crossPaint)
    }
}

internal fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}