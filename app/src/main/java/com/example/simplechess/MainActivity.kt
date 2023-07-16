package com.example.simplechess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

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
}