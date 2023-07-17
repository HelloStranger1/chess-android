package com.example.simplechess.ChessComponents

data class ChessPiece(
    val col : Int,
    val row : Int,
    val player : ChessPlayer,
    val rank : ChessRank,
    val resID : Int

)