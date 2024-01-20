package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board = new ChessPiece[9][9];
    public ChessBoard() {
        
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        // WHITE PIECES

        //rook
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition leftWhiteRook = new ChessPosition(1, 1);
        ChessPosition rightWhiteRook = new ChessPosition(1, 8);
        this.addPiece(leftWhiteRook, whiteRook);
        this.addPiece(rightWhiteRook, whiteRook);

        //knight
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPosition leftWhiteKnight = new ChessPosition(1, 2);
        ChessPosition rightWhiteKnight = new ChessPosition(1, 7);
        this.addPiece(leftWhiteKnight, whiteKnight);
        this.addPiece(rightWhiteKnight, whiteKnight);

        //bishop
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPosition leftWhiteBishop = new ChessPosition(1, 3);
        ChessPosition rightWhiteBishop = new ChessPosition(1, 6);
        this.addPiece(leftWhiteBishop, whiteBishop);
        this.addPiece(rightWhiteBishop, whiteBishop);

        //queen
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPosition whiteQueenPos = new ChessPosition(1, 4);
        this.addPiece(whiteQueenPos, whiteQueen);

        //king
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPosition whiteKingPos = new ChessPosition(1, 5);
        this.addPiece(whiteKingPos, whiteKing);

        //pawns
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        for (int i = 1; i < 9; i++) {
            ChessPosition whitePawnPos = new ChessPosition(2, i);
            this.addPiece(whitePawnPos, whitePawn);
        }

        //BLACK PIECES

        //rook
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPosition leftBlackRook = new ChessPosition(8, 1);
        ChessPosition rightBlackRook = new ChessPosition(8, 8);
        this.addPiece(leftBlackRook, blackRook);
        this.addPiece(rightBlackRook, blackRook);

        //knight
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPosition leftBlackKnight = new ChessPosition(8, 2);
        ChessPosition rightBlackKnight = new ChessPosition(8, 7);
        this.addPiece(leftBlackKnight, blackKnight);
        this.addPiece(rightBlackKnight, blackKnight);

        //bishop
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPosition leftBlackBishop = new ChessPosition(8, 3);
        ChessPosition rightBlackBishop = new ChessPosition(8, 6);
        this.addPiece(leftBlackBishop, blackBishop);
        this.addPiece(rightBlackBishop, blackBishop);

        //queen
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPosition blackQueenPos = new ChessPosition(8, 4);
        this.addPiece(blackQueenPos, blackQueen);

        //king
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPosition blackKingPos = new ChessPosition(8, 5);
        this.addPiece(blackKingPos, blackKing);

        //pawns
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int i = 1; i < 9; i++) {
            ChessPosition blackPawnPos = new ChessPosition(7, i);
            this.addPiece(blackPawnPos, blackPawn);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < this.board.length; i++) {
            for (int j = 1; j < this.board.length; j++) {
                if (this.board[i][j] != null) {
                    stringBuilder.append("{").append(i).append(",").append(j).append("}");
                    stringBuilder.append(" is Type:").append(this.board[i][j].getPieceType()).append('\n');
                }
            }
        }
        return stringBuilder.toString();
    }
}
