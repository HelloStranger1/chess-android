package com.example.simplechess

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class ChessView(context : Context?, attrs : AttributeSet?) : View(context, attrs) {

    private final val originX = 20f
    private final val originY = 200f
    private final val cellSide : Float = 130f

    private var lightColor = ContextCompat.getColor(context!!, R.color.lightSquare)
    private var darkColor = ContextCompat.getColor(context!!, R.color.darkSquare)

    private final val paint = Paint()
    private final val bitmaps = mutableMapOf<Int, Bitmap>()
    private final val imgResIDs = setOf(
        R.drawable.ic_white_king,
        R.drawable.ic_white_queen,
        R.drawable.ic_white_rook,
        R.drawable.ic_white_knight,
        R.drawable.ic_white_bishop,
        R.drawable.ic_white_pawn,

        R.drawable.ic_black_king,
        R.drawable.ic_black_queen,
        R.drawable.ic_black_rook,
        R.drawable.ic_black_knight,
        R.drawable.ic_black_bishop,
        R.drawable.ic_black_pawn,
    )

    var chessDelegate : ChessDelegate? = null
    init {
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas?){
        drawChessBoard(canvas)
        drawPieces(canvas)
    }

    private fun loadBitmaps(){
        imgResIDs.forEach{
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun drawPieces(canvas: Canvas?){
        for(row in 0..7){
            for(col in 0..7){
                val piece = chessDelegate?.pieceAt(col,row)?.let{
                    drawPieceAt(canvas, col, row, it.resID)
                }

            }
        }

    }

    private fun drawPieceAt(canvas: Canvas?, col : Int, row : Int, resID : Int){
        val pieceBitmap = bitmaps[resID]!!
        canvas?.drawBitmap(pieceBitmap,
            null,
            RectF(originX + col * cellSide,
                originY + (7-row) * cellSide,
                originX + (col+1) * cellSide,
                originY + ((7-row) + 1) * cellSide),
            paint
        )
    }
    private fun drawChessBoard(canvas: Canvas?){
        for(row in 0..7){
            for (col in 0..7) {
                drawSquareAt(canvas, col, row, (col+row) % 2 == 0)
            }
        }
    }

    private fun drawSquareAt(canvas: Canvas?, col : Int,row : Int, isDark : Boolean){
        if(isDark){
            paint.color = darkColor
        }else{
            paint.color = lightColor
        }
        canvas?.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1)*cellSide,
            originY + (row + 1) * cellSide,
            paint
        )
    }

}