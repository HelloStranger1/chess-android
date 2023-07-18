package com.example.simplechess.chessComponents

data class ChessPiece(
    val col : Int,
    val row : Int,
    val player : ChessPlayer,
    val pieceType : PieceType,
    val resID : Int

)