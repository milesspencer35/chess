package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurnColor;
    ChessBoard board;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // A move is valid if it doesn't put you in check or leave you in check
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);

        for (ChessMove move: moves) {
            ChessBoard cloneBoard = board.clone(); // copy board
            ChessPiece tempPiece = cloneBoard.getPiece(move.getStartPosition()); //tempPiece at position
            cloneBoard.addPiece(move.getStartPosition(), null); //Remove piece from start position
            cloneBoard.addPiece(move.getEndPosition(), tempPiece); //Move piece to end position
            ChessGame game = new ChessGame(); //new chess game so that I can call isInCheck
            game.setBoard(cloneBoard); // set the game board as the cloned board

            // If the move puts you in check, remove it
            if (game.isInCheck(tempPiece.getTeamColor())) {
                moves.remove(move);
            }
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()).contains(move)
                && board.getPiece(move.getStartPosition()).getTeamColor() == teamTurnColor) {
            ChessPiece piece = board.getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        ArrayList<ChessPosition> piecePositions = (ArrayList<ChessPosition>) getPiecePositions();

        for (ChessPosition pos : piecePositions) {
            if (board.getPiece(pos).getTeamColor() != teamColor) {
                ArrayList<ChessMove> moves = (ArrayList<ChessMove>) board.getPiece(pos).pieceMoves(board, pos);
                for (ChessMove move : moves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    private ChessPosition getKingPosition(TeamColor teamColor) {
        for (ChessPosition pos : getPiecePositions()) {
            if (board.getPiece(pos).getTeamColor() == teamColor
                    && board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING) {
                return pos;
            }
        }
        return null;
    }

    private Collection<ChessPosition> getPiecePositions() {
        ArrayList<ChessPosition> piecePositions = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) != null) {
                    piecePositions.add(position);
                }
            }
        }
        return piecePositions;
    }

}
