package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.moveCalculator.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        String[] directions = new String[] {"up", "upRight", "right", "downRight", "down", "downLeft", "left", "upLeft"};
        for (int i = 0; i < directions.length; i++) {
            possibleMoves.addAll(possibleKingMove(board, myPosition, directions[i]));
        }
        return possibleMoves;
    }

    private Collection<ChessMove> possibleKingMove(ChessBoard board, ChessPosition myPosition, String direction) {
        Collection<ChessMove> possibleKingMoves = new ArrayList<ChessMove>();

        int tempRow = myPosition.getRow();
        int tempColumn = myPosition.getColumn();

        switch(direction) {
            case "up":
                tempRow++;
                break;
            case "upRight":
                tempRow++;
                tempColumn++;
                break;
            case "right":
                tempColumn++;
                break;
            case "downRight":
                tempRow--;
                tempColumn++;
                break;
            case "down":
                tempRow--;
                break;
            case "downLeft":
                tempRow--;
                tempColumn--;
                break;
            case "left":
                tempColumn--;
                break;
            case "upLeft":
                tempRow++;
                tempColumn--;
                break;
        }

        if ((tempRow < 9 && tempRow > 0) && (tempColumn < 9 && tempColumn > 0)) {
            ChessPosition endPosition = new ChessPosition(tempRow, tempColumn);
            // if the endPosition is empty or the color of the piece isn't the same as the moving piece
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //We don't want the colors to be the same
                ChessMove newChessMove = new ChessMove(myPosition, endPosition, null);
                possibleKingMoves.add(newChessMove);
            }
        }

        return possibleKingMoves;
    }


}
