package chess;

import chess.moveCalculator.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        possibleMoves.addAll(possibleKnightMoves(board, myPosition, "up"));
        possibleMoves.addAll(possibleKnightMoves(board, myPosition, "right"));
        possibleMoves.addAll(possibleKnightMoves(board, myPosition, "down"));
        possibleMoves.addAll(possibleKnightMoves(board, myPosition, "left"));

        return possibleMoves;
    }

    private Collection<ChessMove> possibleKnightMoves(ChessBoard board, ChessPosition myPosition, String direction) {
        Collection<ChessMove> possibleKnightMoves = new ArrayList<ChessMove>();

        int rowIncrement = 0;
        int columnIncrement = 0;

        switch (direction) {
            case "up":
                rowIncrement = 2;
                columnIncrement = 1;
                break;
            case "right":
                rowIncrement = 1;
                columnIncrement = 2;
                break;
            case "down":
                rowIncrement = -2;
                columnIncrement = 1;
                break;
            case "left":
                rowIncrement = 1;
                columnIncrement = -2;
                break;
        }

        int tempRow = myPosition.getRow();
        int tempColumn = myPosition.getColumn();
        tempRow += rowIncrement;
        tempColumn += columnIncrement;
        for (int i = 0; i < 2; i++) {
            if ((tempRow < 9 && tempRow > 0) && (tempColumn < 9 && tempColumn > 0)) {
                ChessPosition endPosition = new ChessPosition(tempRow, tempColumn);
                // if the endPosition is empty or the color of the piece isn't the same as the moving piece
                if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //We don't want the colors to be the same
                    ChessMove newChessMove = new ChessMove(myPosition, endPosition, null);
                    possibleKnightMoves.add(newChessMove);
                }
            }

            if (rowIncrement == 1) {
                tempRow = tempRow - 2;
            } else {
                tempColumn = tempColumn - 2;
            }
        }

        return possibleKnightMoves;
    }
}
