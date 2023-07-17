package com.example.simplechess.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.simplechess.chessComponents.ChessDelegate
import com.example.simplechess.chessComponents.ChessModel
import com.example.simplechess.chessComponents.ChessPiece
import com.example.simplechess.ChessView
import com.example.simplechess.R

class MainActivity : AppCompatActivity(), ChessDelegate {

    private var chessModel : ChessModel = ChessModel()
    private var btnReset : Button? = null
    private lateinit var chessView: ChessView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById(R.id.chess_view)

        btnReset = findViewById(R.id.btn_reset)

        btnReset?.setOnClickListener{
            chessModel.reset()
            chessView.invalidate()
        }

        val chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.chessDelegate = this
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return chessModel.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow)
        chessView.invalidate()
    }
}