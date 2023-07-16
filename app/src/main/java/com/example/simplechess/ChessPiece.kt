package com.example.simplechess

data class ChessPiece(
    val col : Int,
    val row : Int,
    val player : ChessPlayer,
    val rank : ChessRank

)