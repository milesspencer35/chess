package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    public static void main(String[] args) {
        drawBoard();
    }

    public static void drawBoard(ChessGame game) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawLetterCoordinates(out, game.getTeamTurn());
    }

    private static void drawLetterCoordinates(PrintStream out, ChessGame.TeamColor color) {
        String[] whiteLetterCoordinates = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] blackLetterCoordinates = {"h", "g", "f", "e", "d", "c", "b", "a"};

        String[] letterCoordinates =
                color == ChessGame.TeamColor.WHITE ? whiteLetterCoordinates : blackLetterCoordinates;

        // start here

    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
