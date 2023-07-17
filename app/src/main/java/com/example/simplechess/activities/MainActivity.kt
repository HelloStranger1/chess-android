package com.example.simplechess.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.simplechess.ChessComponents.ChessDelegate
import com.example.simplechess.ChessComponents.ChessModel
import com.example.simplechess.ChessComponents.ChessPiece
import com.example.simplechess.ChessView
import com.example.simplechess.R

class MainActivity : AppCompatActivity(), ChessDelegate {

    var chessModel : ChessModel = ChessModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("D", "$chessModel")

        val chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.chessDelegate = this
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return chessModel.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow)
        val chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.invalidate()
    }
}