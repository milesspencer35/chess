package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        switch (this.type) {
            case KING:
                System.out.println("KING");
                //Code
                break;
            case QUEEN:
                System.out.println("QUEEN");
                // Code
                break;
            case BISHOP:
                System.out.println("BISHOP");
                possibleMoves.addAll(diagonalMoves(board, myPosition));

                break;
            case KNIGHT:
                System.out.println("KNIGHT");
                // Code
                break;
            case ROOK:
                System.out.println("ROOK");
                // Code
                break;
            case PAWN:
                System.out.println("PAWN");
                // Code
                break;
        }

        return possibleMoves;
        //throw new RuntimeException("Not implemented");
    }


    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleDiagonalMoves = new ArrayList<ChessMove>();

        possibleDiagonalMoves.addAll(possibleDiagonalMoves(board, myPosition, "upToRight"));
        possibleDiagonalMoves.addAll(possibleDiagonalMoves(board, myPosition, "downToRight"));
        possibleDiagonalMoves.addAll(possibleDiagonalMoves(board, myPosition, "downToLeft"));
        possibleDiagonalMoves.addAll(possibleDiagonalMoves(board, myPosition, "upToLeft"));

        return possibleDiagonalMoves;
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
        while((tempRow < 8 && tempRow > 1) && (tempColumn < 8 && tempColumn > 1)) {
            tempRow += rowIncrement;
            tempColumn += columnIncrement;
            ChessPosition endPosition = new ChessPosition(tempRow, tempColumn);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != this.pieceColor) { //We don't want the colors to be the same
                ChessMove newChessMove = new ChessMove(myPosition, endPosition, null);
                System.out.println(newChessMove.ToString());
                possibleDiagonalMoves.add(newChessMove);
            } else {
                break;
            }
        }
        return possibleDiagonalMoves;
    }



}
