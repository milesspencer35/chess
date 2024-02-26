package chess;

import chess.moveCalculator.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        possibleMoves.addAll(possibleStraightMoves(board, myPosition, "Right"));
        possibleMoves.addAll(possibleStraightMoves(board, myPosition, "Left"));
        possibleMoves.addAll(possibleStraightMoves(board, myPosition, "Up"));
        possibleMoves.addAll(possibleStraightMoves(board, myPosition, "Down"));

        return possibleMoves;
    }

    private Collection<ChessMove> possibleStraightMoves(ChessBoard board, ChessPosition myPosition, String direction) {
        Collection<ChessMove> possibleStraightMoves = new ArrayList<ChessMove>();

        int rowIncrement = 0;
        int columnIncrement = 0;

        switch (direction) {
            case "Right":
                columnIncrement = 1;
                break;
            case "Left":
                columnIncrement = -1;
                break;
            case "Up":
                rowIncrement = 1;
                break;
            case "Down":
                rowIncrement = -1;
                break;
        }

        int tempRow = myPosition.getRow();
        int tempColumn = myPosition.getColumn();
        tempRow += rowIncrement;
        tempColumn += columnIncrement;
        while ((tempRow < 9 && tempRow > 0) && (tempColumn < 9 && tempColumn > 0)) {
            ChessPosition endPosition = new ChessPosition(tempRow, tempColumn);
            // if the endPosition is empty or the color of the piece isn't the same as the moving piece
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //We don't want the colors to be the same
                ChessMove newChessMove = new ChessMove(myPosition, endPosition, null);
                possibleStraightMoves.add(newChessMove);
                //System.out.println(newChessMove.ToString());
                //If the piece at end position is opposite the moving piece break the loop
                if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
            } else {
                break;
            }
            tempRow += rowIncrement;
            tempColumn += columnIncrement;
        }

        return possibleStraightMoves;
    }
}
