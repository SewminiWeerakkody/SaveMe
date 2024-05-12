package com.example.saveme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import java.lang.Exception

class GameView(var c: Context, var gameTask: Gametask) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myStonePosition = 0
    private val otherStones = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0



    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherStones.add(map)
        }
        time += 10 + speed
        val stoneWidth = viewWidth / 5
        val stoneHeight = stoneWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.rboralu, null)

        d.setBounds(
            myStonePosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - stoneHeight,
            myStonePosition * viewWidth / 3 + viewWidth / 15 + stoneWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in otherStones.indices) {
            try {
                val stoneX = otherStones[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var stoneY = time - (otherStones[i]["startTime"] as Int)
                val d2 = resources.getDrawable(R.drawable.ystone, null)

                d2.setBounds(
                    stoneX + 25, stoneY - stoneHeight, stoneX + stoneWidth - 25, stoneY
                )

                d2.draw(canvas)
                if (otherStones[i]["lane"] as Int == myStonePosition) {
                    if (stoneY > viewHeight - 2 - stoneHeight && stoneY < viewHeight - 2) {
                        gameTask.closeGame(score)
                    }
                }
                if (stoneY > viewHeight + stoneHeight) {
                    otherStones.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore) {
                        highScore = score
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x1 = event.x
                    if (x1 < viewWidth / 2) {
                        if (myStonePosition > 0) {
                            myStonePosition--
                        }
                    }
                    if (x1 > viewWidth / 2) {
                        if (myStonePosition < 2) {
                            myStonePosition++
                        }
                    }
                    invalidate()
                }

                MotionEvent.ACTION_UP -> {
                }
            }
        }
        return true
    }
}