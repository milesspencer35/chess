package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame game = new ChessGame();
        game.setBoard(board);

        drawBoard(game);
    }

    public static void drawBoard(ChessGame game) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawLetterCoordinates(out, game.getTeamTurn());
        drawPlaySpace(out, game.getBoard());
        drawLetterCoordinates(out, game.getTeamTurn());
    }

    private static void drawLetterCoordinates(PrintStream out, ChessGame.TeamColor color) {
        String[] whiteLetterCoordinates = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] blackLetterCoordinates = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] letterCoordinates =
                color == ChessGame.TeamColor.WHITE ? whiteLetterCoordinates : blackLetterCoordinates;

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

    private static void drawPlaySpace(PrintStream out, ChessBoard board) {
        for (int boardRow = 1; boardRow <= BOARD_HEIGHT; boardRow++) {
            drawNumberCoordinate(out, boardRow);




            drawNumberCoordinate(out, boardRow);
            resetBGColor(out);
            out.println();
        }
    }

    private static void drawNumberCoordinate(PrintStream out, int boardRow) {
        setBackgroundGray(out);
        out.print(" ");
        out.print(boardRow);
        out.print(" ");
    }




    private static void drawEMPTY(PrintStream out) {
        out.print(EMPTY);
    }

    private static void setBackgroundGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void resetBGColor(PrintStream out) {
        out.print("\u001B[0m");

    }
}
