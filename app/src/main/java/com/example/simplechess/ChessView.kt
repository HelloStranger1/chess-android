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
import com.example.simplechess.ChessComponents.ChessDelegate
import java.lang.Integer.min

class ChessView(context : Context?, attrs : AttributeSet?) : View(context, attrs) {

    private val scaleFactor = 0.9f
    private var originX = 20f
    private var originY = 200f
    private var cellSide : Float = 130f

    private var lightColor = ContextCompat.getColor(context!!, R.color.lightSquare)
    private var darkColor = ContextCompat.getColor(context!!, R.color.darkSquare)

    private val paint = Paint()
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val imgResIDs = setOf(
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

    private var fromCol : Int = -1
    private var fromRow : Int = -1
    private var movingPieceX : Float = -1f
    private var movingPieceY : Float = -1f
    var chessDelegate : ChessDelegate? = null
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
            }
            MotionEvent.ACTION_UP ->{
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                Log.d("Move", "from ($fromCol, $fromRow) to ($col, $row)")
                chessDelegate?.movePiece(fromCol, fromRow, col, row)
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
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun drawPieces(canvas: Canvas?){
        for(row in 0..7){
            for(col in 0..7){
                if(row != fromRow || col != fromCol){
                    val piece = chessDelegate?.pieceAt(col,row)?.let{
                        drawPieceAt(canvas, col, row, it.resID)
                    }
                }

            }
        }
        chessDelegate?.pieceAt(fromCol, fromRow)?.let{
            val pieceBitmap = bitmaps[it.resID]!!
            canvas?.drawBitmap(pieceBitmap,
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

        val pieceBitmap = bitmaps[resID]!!
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

}