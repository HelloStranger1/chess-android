package com.example.simplechess

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ChessView(context : Context?, attrs : AttributeSet?) : View(context, attrs) {

    private final val originX = 20f
    private final val originY = 200f
    private final val cellSide : Float = 130f
    override fun onDraw(canvas: Canvas?){
        val paint = Paint()

        for(j in 0..7){
            for (i in 0..7) {
                if( (i + j) % 2 == 0){
                    paint.color = Color.LTGRAY
                }else{
                    paint.color = Color.DKGRAY
                }
                canvas?.drawRect(originX + i * cellSide, originY + j * cellSide, originX + (i + 1)*cellSide, originY + (j + 1) * cellSide, paint)

            }
        }


    }

}