package chess;

import chess.moveCalculator.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        possibleMoves.addAll(possibleDiagonalMoves(board, myPosition, "upToRight"));
        possibleMoves.addAll(possibleDiagonalMoves(board, myPosition, "downToRight"));
        possibleMoves.addAll(possibleDiagonalMoves(board, myPosition, "downToLeft"));
        possibleMoves.addAll(possibleDiagonalMoves(board, myPosition, "upToLeft"));

        return possibleMoves;
    }

    private Collection<ChessMove> possibleDiagonalMoves(ChessBoard board, ChessPosition myPosition, String direction) {
        Collection<ChessMove> possibleDiagonalMoves = new ArrayList<>();
        int rowIncrement = 0;
        int columnIncrement = 0;

        switch(direction) {
            case "upToRight":
                rowIncrement = 1;
                columnIncrement = 1;
                break;
            case "downToRight":
                rowIncrement = -1;
                columnIncrement = 1;
                break;
            case "downToLeft":
                rowIncrement = -1;
                columnIncrement = -1;
                break;
            case "upToLeft":
                rowIncrement = 1;
                columnIncrement = -1;
                break;
        }

        int tempRow = myPosition.getRow();
        int tempColumn = myPosition.getColumn();
        tempRow += rowIncrement;
        tempColumn += columnIncrement;
        while((tempRow < 9 && tempRow > 0) && (tempColumn < 9 && tempColumn > 0)) {
            ChessPosition endPosition = new ChessPosition(tempRow, tempColumn);
            // if the endPosition is empty or the color of the piece isn't the same as the moving piece
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //We don't want the colors to be the same
                ChessMove newChessMove = new ChessMove(myPosition, endPosition, null);
                possibleDiagonalMoves.add(newChessMove);
                //If the piece at end position is opposite the moving piece break the loop
                if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
            }
            else {
                break;
            }
            tempRow += rowIncrement;
            tempColumn += columnIncrement;
        }
        return possibleDiagonalMoves;
    }

}
