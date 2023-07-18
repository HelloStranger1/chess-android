package com.example.simplechess.chessComponents

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?

    fun movePiece(from : Square, to : Square)
}