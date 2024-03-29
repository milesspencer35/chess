package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame game = new ChessGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        System.out.println("White Board");
        drawBoard(game);

        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        System.out.println("Black Board");
        drawBoard(game);
    }

    public static void drawBoard(ChessGame game) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        ChessBoard board = game.getBoard();

        // Hard code
        board = new ChessBoard();
        board.resetBoard();
        // Hard code

        if (game.getTeamTurn() == ChessGame.TeamColor.BLACK) {
            board = reversedBoard(game.getBoard());
        }

//        drawLetterCoordinates(out, game.getTeamTurn());
//        drawPlaySpace(out, board, game.getTeamTurn());
//        drawLetterCoordinates(out, game.getTeamTurn());

        // hard code
        drawLetterCoordinates(out, ChessGame.TeamColor.WHITE);
        drawPlaySpace(out, board, ChessGame.TeamColor.WHITE);
        drawLetterCoordinates(out, ChessGame.TeamColor.WHITE);
        out.println();

        drawLetterCoordinates(out, ChessGame.TeamColor.BLACK);
        drawPlaySpace(out, board, ChessGame.TeamColor.BLACK);
        drawLetterCoordinates(out, ChessGame.TeamColor.BLACK);
        // hard code

        out.println();
    }

    private static ChessBoard reversedBoard(ChessBoard board) {
        ChessBoard reverseBoard = new ChessBoard();
        for (int row = 1; row <= 8; row++) {
            ArrayList<ChessPiece> pieces = new ArrayList<>();
            for (int col = 1; col <= 8; col++) {
                pieces.add(board.getPiece(new ChessPosition(row, col)));
            }
            int piecePrevColumn = 0;
            for (int col = 8; col >= 1; col--) {
                reverseBoard.addPiece(new ChessPosition(row, col), pieces.get(piecePrevColumn));
                piecePrevColumn++;
            }
        }
        return reverseBoard;
    }


    private static void drawLetterCoordinates(PrintStream out, ChessGame.TeamColor color) {
        String[] whiteLetterCoordinates = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] blackLetterCoordinates = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] letterCoordinates =
                color == ChessGame.TeamColor.BLACK ? blackLetterCoordinates : whiteLetterCoordinates;

        setBackgroundGray(out);
        drawEMPTY(out);
        for (int boardCol = 0; boardCol < BOARD_WIDTH; boardCol++) {
            drawLetter(out, letterCoordinates[boardCol]);
        }
        drawEMPTY(out);

        resetBGColor(out);
        out.println();
    }

    private static void drawLetter(PrintStream out, String letterText) {
        out.print(" ");
        out.print(letterText);
        out.print(" ");
    }

    private static void drawPlaySpace(PrintStream out, ChessBoard board, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            for (int boardRow = 8; boardRow >= 1; boardRow--) {
                drawNumberCoordinate(out, boardRow);

                drawPlayRow(out, board, boardRow);
                drawNumberCoordinate(out, boardRow);
                resetBGColor(out);
                out.println();
            }
        } else {
            for (int boardRow = 1; boardRow <= BOARD_HEIGHT; boardRow++) {
                drawNumberCoordinate(out, boardRow);

                drawPlayRow(out, board, boardRow);

                drawNumberCoordinate(out, boardRow);
                resetBGColor(out);
                out.println();
            }
        }
    }

    private static void drawNumberCoordinate(PrintStream out, int boardRow) {
        setBackgroundGray(out);
        out.print(" ");
        out.print(boardRow);
        out.print(" ");
    }

    private static void drawPlayRow(PrintStream out, ChessBoard board, int row) {

        for (int col = 1; col <= 8; col++) {
            ChessPiece piece = getPiece(board, col, row);

            if (row % 2 == 1) {
                if (col % 2 == 1) {
                    drawWhiteSquare(out, getPieceString(piece), getPieceColor(piece));
                } else {
                    drawBlackSquare(out, getPieceString(piece), getPieceColor(piece));
                }
            } else {
                if (col % 2 == 1) {
                    drawBlackSquare(out, getPieceString(piece), getPieceColor(piece));
                } else {
                    drawWhiteSquare(out, getPieceString(piece), getPieceColor(piece));
                }
            }
        }
    }

    private static String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return " ";
        }

        switch (piece.getPieceType()) {
            case KING:
                return "K";
            case QUEEN:
                return "Q";
            case BISHOP:
                return "B";
            case KNIGHT:
                return "N";
            case ROOK:
                return "R";
            default:
                return "P";
        }
    }

    private static ChessGame.TeamColor getPieceColor(ChessPiece piece) {
        if (piece == null) {
            return ChessGame.TeamColor.WHITE;
        }

        switch(piece.getTeamColor()) {
            case WHITE:
                return ChessGame.TeamColor.WHITE;
            default:
                return ChessGame.TeamColor.BLACK;
        }
    }

    private static void drawSquare(PrintStream out, String piece, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            drawWhiteSquare(out, piece, color);
        }
    }

    private static void drawWhiteSquare(PrintStream out, String piece, ChessGame.TeamColor color) {
        setPieceColor(out, color);
        setBackgroundWhite(out);
        out.print(" ");
        out.print(piece);
        out.print(" ");
    }

    private static void drawBlackSquare(PrintStream out, String piece, ChessGame.TeamColor color) {
        setPieceColor(out, color);
        setBackgroundBlack(out);
        out.print(" ");
        out.print(piece);
        out.print(" ");
    }

    private static void setPieceColor(PrintStream out, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            setTextColorBlue(out);
        } else {
            setTextColorRed(out);
        }
    }

    private static ChessPiece getPiece(ChessBoard board, int col, int row){
        ChessPosition pos = new ChessPosition(row, col);
        return board.getPiece(pos);
    }


    private static void drawEMPTY(PrintStream out) {
        out.print(EMPTY);
    }

    private static void setBackgroundGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBackgroundWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setBackgroundBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void setTextColorBlue(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setTextColorRed(PrintStream out) {
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void resetBGColor(PrintStream out) {
        out.print("\u001B[0m");
    }
}
