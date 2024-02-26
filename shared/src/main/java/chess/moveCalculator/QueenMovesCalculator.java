package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        PieceMovesCalculator bishopMoves = new BishopMovesCalculator();
        PieceMovesCalculator rookMoves = new RookMovesCalculator();

        possibleMoves.addAll(bishopMoves.pieceMoves(board, myPosition));
        possibleMoves.addAll(rookMoves.pieceMoves(board, myPosition));

        return possibleMoves;
    }
}
