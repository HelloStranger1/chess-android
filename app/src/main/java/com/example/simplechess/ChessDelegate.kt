package com.example.simplechess

interface ChessDelegate {
    fun pieceAt(col : Int, row : Int) : ChessPiece?
}