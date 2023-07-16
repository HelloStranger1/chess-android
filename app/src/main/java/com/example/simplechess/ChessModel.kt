package com.example.simplechess

class ChessModel {
    var piecesBox = mutableSetOf<ChessPiece>()

    init {
        piecesBox.removeAll(piecesBox)
        //white Rooks
        piecesBox.add(ChessPiece(0,0,ChessPlayer.WHITE, ChessRank.ROOK))
        piecesBox.add(ChessPiece(7,0,ChessPlayer.WHITE, ChessRank.ROOK))

        //black rooks
        piecesBox.add(ChessPiece(0,7,ChessPlayer.BLACK, ChessRank.ROOK))
        piecesBox.add(ChessPiece(7,7,ChessPlayer.BLACK, ChessRank.ROOK))

        //white Knights
        piecesBox.add(ChessPiece(1,0,ChessPlayer.WHITE, ChessRank.KNIGHT))
        piecesBox.add(ChessPiece(6,0,ChessPlayer.WHITE, ChessRank.KNIGHT))

        //black Knights
        piecesBox.add(ChessPiece(1,7,ChessPlayer.BLACK, ChessRank.KNIGHT))
        piecesBox.add(ChessPiece(6,7,ChessPlayer.BLACK, ChessRank.KNIGHT))

        //white Bishops
        piecesBox.add(ChessPiece(2,0,ChessPlayer.WHITE, ChessRank.BISHOP))
        piecesBox.add(ChessPiece(5,0,ChessPlayer.WHITE, ChessRank.BISHOP))

        //black Bishops
        piecesBox.add(ChessPiece(2,7,ChessPlayer.BLACK, ChessRank.BISHOP))
        piecesBox.add(ChessPiece(5,7,ChessPlayer.BLACK, ChessRank.BISHOP))

        //white Queen and King
        piecesBox.add(ChessPiece(3,0,ChessPlayer.WHITE, ChessRank.QUEEN))
        piecesBox.add(ChessPiece(4,0,ChessPlayer.WHITE, ChessRank.KING))

        //black Bishops
        piecesBox.add(ChessPiece(3,7,ChessPlayer.BLACK, ChessRank.QUEEN))
        piecesBox.add(ChessPiece(4,7,ChessPlayer.BLACK, ChessRank.KING))

        for( i in 0..7){ //Pawns
            piecesBox.add(ChessPiece(i,1,ChessPlayer.WHITE, ChessRank.PAWN))
            piecesBox.add(ChessPiece(i,6,ChessPlayer.BLACK, ChessRank.PAWN))
        }


    }

    fun pieceAt(col : Int, row : Int) : ChessPiece?{
        for (piece in piecesBox){
            if(col == piece.col && row == piece.row){
                return piece
            }
        }
        return null
    }
    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0){
            desc += "$row"
            for (col in 0..7){
                val pieceAt = pieceAt(col, row)
                if (pieceAt == null) {
                    desc +=" ."
                } else{
                    val isWhite = pieceAt.player == ChessPlayer.WHITE
                    desc += " "
                    desc += when (pieceAt.rank){
                        ChessRank.KING -> { if (isWhite) "k" else "K" }
                        ChessRank.QUEEN -> { if (isWhite) "q" else "Q" }
                        ChessRank.ROOK -> { if (isWhite) "r" else "R" }
                        ChessRank.BISHOP -> { if (isWhite) "b" else "B" }
                        ChessRank.KNIGHT -> { if (isWhite) "n" else "N" }
                        ChessRank.PAWN -> { if (isWhite) "p" else "P" }
                    }
                }

            }
            desc += "\n"
        }
        desc +="  0 1 2 3 4 5 6 7"
        return desc
    }
}