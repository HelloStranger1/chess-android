package com.example.simplechess.chessComponents

import android.util.Log
import com.example.simplechess.R

object ChessGame {
    var piecesBox = mutableSetOf<ChessPiece>()
    lateinit var whiteKingLocation : Square
    lateinit var blackKingLocation : Square
    var isWhitesTurn : Boolean = true

    init{

        reset()
    }

    fun movePiece(from : Square, to : Square,  isPlayerWhite : Boolean) : Boolean{
        if (from.col == to.col && from.row == to.row) return false
        val movingPiece = pieceAt(from) ?: return false
        if(movingPiece.player == ChessPlayer.WHITE && !isPlayerWhite) return false
        if(movingPiece.player == ChessPlayer.BLACK && isPlayerWhite) return false

        if (!canPieceMove(movingPiece, from, to, movingPiece.player == ChessPlayer.WHITE)){
            Log.e("TAG", "Piece  cannot move")
            return false
        }else{
            Log.e("TAG", "Piece $movingPiece can move from $from to $to")
        }
        pieceAt(to)?.let{
            if (it.player == movingPiece.player){
                Log.e("TAG", "Cannot move, friendly piece in way")
                return false
            }
            piecesBox.remove(it)
        }
        piecesBox.remove(movingPiece)
        piecesBox.add(ChessPiece(to.col, to.row, movingPiece.player, movingPiece.pieceType, movingPiece.resID))
        if(movingPiece.pieceType == PieceType.KING){
            if(movingPiece.player == ChessPlayer.WHITE) {
                whiteKingLocation = Square(to.col, to.row)
            }else{
                blackKingLocation = Square(to.col, to.row)
            }
        }
        return true
    }
    private fun canPieceMove(movingPiece: ChessPiece, from: Square, to: Square, isWhite : Boolean, isCapturingKing : Boolean = false) : Boolean{
        if (to.col < 0 || to.col > 7 || to.row < 0 || to.row > 7) return false
        if(!isCapturingKing && movingPiece.pieceType != PieceType.KING){
            val kingLocation = if(isWhite) whiteKingLocation else blackKingLocation
            piecesBox.remove(movingPiece)
            val pieceChecking : ChessPiece? = pieceCheckingKing(kingLocation, isWhite)
            var isKingSafe : Boolean = true
            if(pieceChecking != null) {
                if(pieceChecking.col == to.col && pieceChecking.row == to.row){
                    piecesBox.remove(pieceChecking)
                    val anotherPieceChecking : ChessPiece? = pieceCheckingKing(kingLocation, isWhite)
                    if (anotherPieceChecking != null) {
                        isKingSafe = false
                    }
                    piecesBox.add(ChessPiece(pieceChecking.col, pieceChecking.row, pieceChecking.player, pieceChecking.pieceType, pieceChecking.resID))
                }else{
                    isKingSafe = false
                }
            }
            piecesBox.add(ChessPiece(from.col, from.row, movingPiece.player, movingPiece.pieceType, movingPiece.resID))
            if(!isKingSafe) return false
        }

        return when(movingPiece.pieceType){
            PieceType.QUEEN -> { canQueenMove(from, to)}
            PieceType.KING -> { canKingMove(from, to, isWhite)}
            PieceType.ROOK -> { canRookMove(from, to)}
            PieceType.BISHOP -> { canBishopMove(from, to)}
            PieceType.KNIGHT -> { canKnightMove(from, to)}
            PieceType.PAWN -> { canPawnMove(from, to, isWhite)}

        }
    }
    fun reset(){
        piecesBox.removeAll(piecesBox)
        isWhitesTurn = true
        //white Rooks
        piecesBox.add(ChessPiece(0,0, ChessPlayer.WHITE, PieceType.ROOK, R.drawable.ic_white_rook))
        piecesBox.add(ChessPiece(7,0, ChessPlayer.WHITE, PieceType.ROOK, R.drawable.ic_white_rook))

        //black rooks
        piecesBox.add(ChessPiece(0,7, ChessPlayer.BLACK, PieceType.ROOK, R.drawable.ic_black_rook))
        piecesBox.add(ChessPiece(7,7, ChessPlayer.BLACK, PieceType.ROOK, R.drawable.ic_black_rook))

        //white Knights
        piecesBox.add(ChessPiece(1,0, ChessPlayer.WHITE, PieceType.KNIGHT, R.drawable.ic_white_knight))
        piecesBox.add(ChessPiece(6,0, ChessPlayer.WHITE, PieceType.KNIGHT, R.drawable.ic_white_knight))

        //black Knights
        piecesBox.add(ChessPiece(1,7, ChessPlayer.BLACK, PieceType.KNIGHT, R.drawable.ic_black_knight))
        piecesBox.add(ChessPiece(6,7, ChessPlayer.BLACK, PieceType.KNIGHT, R.drawable.ic_black_knight))

        //white Bishops
        piecesBox.add(ChessPiece(2,0, ChessPlayer.WHITE, PieceType.BISHOP, R.drawable.ic_white_bishop))
        piecesBox.add(ChessPiece(5,0, ChessPlayer.WHITE, PieceType.BISHOP, R.drawable.ic_white_bishop))

        //black Bishops
        piecesBox.add(ChessPiece(2,7, ChessPlayer.BLACK, PieceType.BISHOP, R.drawable.ic_black_bishop))
        piecesBox.add(ChessPiece(5,7, ChessPlayer.BLACK, PieceType.BISHOP, R.drawable.ic_black_bishop))

        //white Queen and King
        piecesBox.add(ChessPiece(3,0, ChessPlayer.WHITE, PieceType.QUEEN, R.drawable.ic_white_queen))
        piecesBox.add(ChessPiece(4,0, ChessPlayer.WHITE, PieceType.KING, R.drawable.ic_white_king))
        whiteKingLocation = Square(4,0)

        //black Queen and king
        piecesBox.add(ChessPiece(3,7, ChessPlayer.BLACK, PieceType.QUEEN, R.drawable.ic_black_queen))
        piecesBox.add(ChessPiece(4,7, ChessPlayer.BLACK, PieceType.KING, R.drawable.ic_black_king))
        blackKingLocation = Square(4,7)
        for( i in 0..7){ //Pawns
            piecesBox.add(ChessPiece(i,1, ChessPlayer.WHITE, PieceType.PAWN, R.drawable.ic_white_pawn))
            piecesBox.add(ChessPiece(i,6, ChessPlayer.BLACK, PieceType.PAWN, R.drawable.ic_black_pawn))
        }


    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    private fun canKingMove(from: Square, to: Square, isWhite: Boolean): Boolean {

        if (canQueenMove(from, to) && pieceCheckingKing(to, isWhite) == null) {
            val deltaCol = kotlin.math.abs(from.col - to.col)
            val deltaRow = kotlin.math.abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    private fun pieceCheckingKing(to: Square, isWhite: Boolean) : ChessPiece?{

        for(piece in piecesBox){
            if(piece.pieceType == PieceType.KING ||
                (isWhite && piece.player == ChessPlayer.WHITE) ||
                (!isWhite && piece.player == ChessPlayer.BLACK) ||
                (piece.col == to.col && piece.row == to.row)){
                continue
            }
            if(canPieceMove(piece, Square(piece.col, piece.row), to, !isWhite, true)){
                Log.e("TAG", "King can't move to $to because $piece can capture him")
                return piece
            }
        }
        return null
    }
    private fun canRookMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)) {


            return true
        }
        return false
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (kotlin.math.abs(from.col - to.col) == kotlin.math.abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }
    private fun canKnightMove(from : Square, to : Square) : Boolean{
        if(kotlin.math.abs(from.col - to.col) == 2 && kotlin.math.abs(from.row - to.row) == 1){
            return true
        }else if(kotlin.math.abs(from.col - to.col) == 1 && kotlin.math.abs(from.row - to.row) == 2){
            return true
        }
        return false
    }

    private fun canPawnMove(from: Square, to: Square, isWhite: Boolean): Boolean {
        if(isWhite){
            if(from.col == to.col){
                if(to.row - from.row == 1){
                    return pieceAt(to) == null
                }else if(to.row - from.row == 2){
                    return from.row == 1 && pieceAt(to) == null && pieceAt(Square(to.col, to.row -1)) == null
                }else{
                    return false
                }
            }else if(kotlin.math.abs(from.col - to.col) == 1) {
                return to.row - from.row == 1 && pieceAt(to) != null
            }else{
                return false
            }
        } else{
            if(from.col == to.col){
                if(from.row - to.row == 1){
                    return true
                }else if(from.row - to.row == 2){
                    return from.row == 6 && pieceAt(to) == null && pieceAt(Square(to.col, to.row +1)) == null
                }else{
                    return false
                }
            }else if(kotlin.math.abs(from.col - to.col) == 1) {
                return from.row - to.row == 1 && pieceAt(to) != null
            }else{
                return false
            }
        }
    }


    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = kotlin.math.abs(from.col - to.col) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Square(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = kotlin.math.abs(from.row - to.row) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Square(from.col, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (kotlin.math.abs(from.col - to.col) != kotlin.math.abs(from.row - to.row)) return false
        val gap = kotlin.math.abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Square(nextCol, nextRow)) != null) {
                return false
            }
        }
        return true
    }


    fun pieceAt(square: Square) : ChessPiece?{
        for (piece in piecesBox){
            if(square.col == piece.col && square.row == piece.row){
                return piece
            }
        }
        return null
    }
    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0){
            desc += "$row"
            desc += boardRow(row)

        }
        desc +="  0 1 2 3 4 5 6 7"
        return desc
    }

    private fun boardRow(row : Int) : String{
        var desc =""
        for (col in 0..7){
            val pieceAt = pieceAt(Square(col, row))
            if (pieceAt == null) {
                desc +=" ."
            } else{
                val isWhite = pieceAt.player == ChessPlayer.WHITE
                desc += " "
                desc += when (pieceAt.pieceType){
                    PieceType.KING -> { if (isWhite) "k" else "K" }
                    PieceType.QUEEN -> { if (isWhite) "q" else "Q" }
                    PieceType.ROOK -> { if (isWhite) "r" else "R" }
                    PieceType.BISHOP -> { if (isWhite) "b" else "B" }
                    PieceType.KNIGHT -> { if (isWhite) "n" else "N" }
                    PieceType.PAWN -> { if (isWhite) "p" else "P" }
                }
            }
        }
        desc += "\n"
        return desc
    }

    fun pgn() : String {
        var desc = " \n"
        for (row in 8 downTo 1){
            desc += "${row + 1}"
            desc += boardRow(row)

        }
        desc += " a b c d e f g h"

        return desc
    }

}
