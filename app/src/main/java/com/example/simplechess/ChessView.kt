package com.example.simplechess

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.simplechess.chessComponents.ChessDelegate
import com.example.simplechess.chessComponents.ChessPiece
import com.example.simplechess.utils.constants.cellSide
import com.example.simplechess.utils.constants.imgResIDs
import com.example.simplechess.utils.constants.originX
import com.example.simplechess.utils.constants.originY

import com.example.simplechess.utils.constants.scaleFactor

import java.lang.Integer.min

class ChessView(context : Context?, attrs : AttributeSet?) : View(context, attrs) {

    var chessDelegate : ChessDelegate? = null

    private var lightColor = ContextCompat.getColor(context!!, R.color.lightSquare)
    private var darkColor = ContextCompat.getColor(context!!, R.color.darkSquare)

    private val paint = Paint()
    private val pieceBitmaps = mutableMapOf<Int, Bitmap>()

    private var movingPieceBitmap : Bitmap? = null
    private var movingPiece : ChessPiece? = null

    private var fromCol : Int = -1
    private var fromRow : Int = -1
    private var movingPieceX : Float = -1f
    private var movingPieceY : Float = -1f

    init {
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas?){
        canvas ?: return


        val chessBoardSide = min(width, height) * scaleFactor
        cellSide = chessBoardSide / 8f
        originX= (width - chessBoardSide) / 2f
        originY = (height - chessBoardSide) / 2f

        drawChessBoard(canvas)
        drawPieces(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action){
            MotionEvent.ACTION_DOWN ->{
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.pieceAt(fromCol, fromRow)?.let{
                    movingPiece = it
                    movingPieceBitmap = pieceBitmaps[it.resID]
                }
            }
            MotionEvent.ACTION_UP ->{
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                if(fromCol != col || fromRow != row){
                    chessDelegate?.movePiece(fromCol, fromRow, col, row)
                }

                movingPieceBitmap = null
                movingPiece = null
                invalidate()
            }
            MotionEvent.ACTION_MOVE ->{
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }
        }
        return true
    }
    private fun loadBitmaps(){
        imgResIDs.forEach{
            pieceBitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun drawPieces(canvas: Canvas?){
        for(row in 0..7){
            for(col in 0..7){
                chessDelegate?.pieceAt(col, row)?.let{
                    if(it != movingPiece){
                        drawPieceAt(canvas, col, row, it.resID)
                    }
                }

            }
        }

        movingPieceBitmap?.let {
            canvas?.drawBitmap(it,
                null,
                RectF(
                    movingPieceX - cellSide / 2,
                    movingPieceY - cellSide / 2,
                    movingPieceX + cellSide / 2,
                    movingPieceY + cellSide / 2),
                paint
            )
        }


    }

    private fun drawPieceAt(canvas: Canvas?, col : Int, row : Int, resID : Int){
        canvas ?: return

        val pieceBitmap = pieceBitmaps[resID]!!
        canvas.drawBitmap(pieceBitmap,
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
        canvas ?: return

        if(isDark){
            paint.color = darkColor
        }else{
            paint.color = lightColor
        }
        canvas.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1)*cellSide,
            originY + (row + 1) * cellSide,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

}