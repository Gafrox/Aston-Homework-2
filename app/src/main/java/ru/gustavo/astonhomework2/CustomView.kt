package ru.gustavo.astonhomework2

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlin.math.min

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var centerX = 0F
    private var centerY = 0F
    private var radius = 0F
    private val colors = listOf(
        Color.RED, Color.rgb(255, 165, 0), Color.YELLOW,
        Color.GREEN, Color.BLUE, Color.rgb(75, 0, 130), Color.rgb(238, 130, 238)
    )
    private var currentAngle = 0F
    private lateinit var rotationAnimator: ObjectAnimator
    private var isAnimating = false
    private var colorIndex = 0
    private var textColor = "COLOR"

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isAnimating) {
                    startAnimation()
                } else stopRotationAnimation()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startAnimation() {
        val random = (3240..3600).random().toFloat()
        rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", currentAngle, random).apply {
            duration = 2000L
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    isAnimating = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    isAnimating = false
                    calcSector(random)
                }

                override fun onAnimationCancel(animation: Animator) {
                    isAnimating = false
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        }
        rotationAnimator.start()
    }

    private fun stopRotationAnimation() {
        rotationAnimator.cancel()
        rotation = 0F
    }

    override fun onDraw(canvas: Canvas) {
        drawDrum(canvas)
        invalidate()
    }

    fun clear() {
        val imageView = (parent as? ViewGroup)?.findViewById<ImageView>(R.id.imageView)
        imageView?.setImageDrawable(null)
        val textView = (parent as? ViewGroup)?.findViewById<TextView>(R.id.textView)
        textView?.text = ""
    }

    fun setSize(width: Int, height: Int) {
        layoutParams.width = width
        layoutParams.height = height
        requestLayout()
    }


    private fun setImage() {
        val url = "https://loremflickr.com/320/240"
        val imageView = (parent as? ViewGroup)?.findViewById<ImageView>(R.id.imageView)
        try {
            Picasso.get().load(url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setText(text: String) {
        val textView = (parent as? ViewGroup)?.findViewById<TextView>(R.id.textView)
        textView?.text = text
    }

    private fun drawDrum(canvas: Canvas) {
        centerX = width / 2F
        centerY = height / 2F
        val sweepAngle = 360f / colors.size
        radius = min(centerX, centerY)
        for (i in colors.indices) {
            val startAngle = i * sweepAngle
            paint.apply {
                color = colors[i]
            }
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                startAngle,
                sweepAngle,
                true,
                paint
            )
        }
    }

    private fun calcSector(random: Float) {
        currentAngle = random - 3240
        val sectorAngle = 360F / colors.size
        val centerSector = sectorAngle / 4
        colorIndex = if ((currentAngle - centerSector) / (sectorAngle) < 0) {
            6
        } else {
            ((currentAngle - centerSector) / (sectorAngle)).toInt() % colors.size
        }
        textColor = when (colorIndex) {
            0 -> "BLUE"
            1 -> "GREEN"
            2 -> "YELLOW"
            3 -> "ORANGE"
            4 -> "RED"
            5 -> "VIOLET"
            else -> "DEEP BLUE"
        }
        if (textColor == "RED" || textColor == "YELLOW" || textColor == "BLUE" || textColor == "VIOLET") {
            setText(textColor)
        } else {
            setImage()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0xFF000000.toInt()
    }
}