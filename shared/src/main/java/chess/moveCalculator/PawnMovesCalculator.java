package chess.moveCalculator;

import chess.*;
import chess.moveCalculator.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    ChessPiece.PieceType[] promotionArray = new ChessPiece.PieceType[]
            {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(possibleBlackPawnMoves(board, myPosition));
        possibleMoves.addAll(possibleWhitePawnMoves(board, myPosition));
        return possibleMoves;
    }

    private Collection<ChessMove> possibleBlackPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        // Black PAWN check for move forward 1, forward promotion, and move forward 2
        ChessPosition blackOneForward = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if (board.getPiece(blackOneForward) == null && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            //Test for promotion
            if (myPosition.getRow() - 1 == 1) {
                for (ChessPiece.PieceType promotion : promotionArray) {
                    ChessMove newChessMove = new ChessMove(myPosition, blackOneForward, promotion);
                    possibleMoves.add(newChessMove);
                }
            } else {
                ChessMove newChessMove = new ChessMove(myPosition, blackOneForward, null);
                possibleMoves.add(newChessMove);
            }

            ChessPosition blackTwoForward = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
            if (myPosition.getRow() == 7 && board.getPiece(blackTwoForward) == null) {
                ChessMove newChessMove = new ChessMove(myPosition, blackTwoForward, null);
                possibleMoves.add(newChessMove);
            }
        }


        //Move diagonal if opposite color piece in diagonal spot
        ChessPosition blackDiagonalRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        ChessPosition blackDiagonalLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (blackDiagonalRight.getColumn() < 9
                    && board.getPiece(blackDiagonalRight) != null
                    && board.getPiece(blackDiagonalRight).getTeamColor() != ChessGame.TeamColor.BLACK) {
                if (myPosition.getRow() - 1 == 1) {
                    for (ChessPiece.PieceType promotion : promotionArray) {
                        ChessMove newChessMove = new ChessMove(myPosition, blackDiagonalRight, promotion);
                        possibleMoves.add(newChessMove);
                    }
                }
                else {
                    ChessMove newChessMove = new ChessMove(myPosition, blackDiagonalRight, null);
                    possibleMoves.add(newChessMove);
                }
            }

            if (blackDiagonalLeft.getColumn() > 0
                    && board.getPiece(blackDiagonalLeft) != null
                    && board.getPiece(blackDiagonalLeft).getTeamColor() != ChessGame.TeamColor.BLACK) {
                if (myPosition.getRow() - 1 == 1) {
                    for (ChessPiece.PieceType promotion : promotionArray) {
                        ChessMove newChessMove = new ChessMove(myPosition, blackDiagonalLeft, promotion);
                        possibleMoves.add(newChessMove);
                    }
                }
                else {
                    ChessMove newChessMove = new ChessMove(myPosition, blackDiagonalLeft, null);
                    possibleMoves.add(newChessMove);
                }
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> possibleWhitePawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        // White PAWN check for move forward 1, forward promotion, and move forward 2
        ChessPosition whiteOneForward = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (board.getPiece(whiteOneForward) == null
                && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE
                && whiteOneForward.getRow() < 9) {
            //Test for promotion
            if (myPosition.getRow() + 1 == 8) {
                for (ChessPiece.PieceType promotion : promotionArray) {
                    ChessMove newChessMove = new ChessMove(myPosition, whiteOneForward, promotion);
                    possibleMoves.add(newChessMove);
                }
            } else {
                ChessMove newChessMove = new ChessMove(myPosition, whiteOneForward, null);
                possibleMoves.add(newChessMove);
            }

            //Move two moves forward
            ChessPosition whiteTwoForward = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
            if (myPosition.getRow() == 2 && board.getPiece(whiteTwoForward) == null) {
                ChessMove newChessMove = new ChessMove(myPosition, whiteTwoForward, null);
                possibleMoves.add(newChessMove);
            }
        }

        //Move diagonal if opposite color piece in diagonal spot
        ChessPosition whiteDiagonalRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        ChessPosition whiteDiagonalLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if ( whiteDiagonalRight.getColumn() < 9
                    && board.getPiece(whiteDiagonalRight) != null
                    && board.getPiece(whiteDiagonalRight).getTeamColor() != ChessGame.TeamColor.WHITE) {
                if (myPosition.getRow() + 1 == 8) {
                    for (ChessPiece.PieceType promotion : promotionArray) {
                        ChessMove newChessMove = new ChessMove(myPosition, whiteDiagonalRight, promotion);
                        possibleMoves.add(newChessMove);
                    }
                }
                else {
                    ChessMove newChessMove = new ChessMove(myPosition, whiteDiagonalRight, null);
                    possibleMoves.add(newChessMove);
                }
            }

            if (whiteDiagonalLeft.getColumn() > 0
                    && board.getPiece(whiteDiagonalLeft) != null
                    && board.getPiece(whiteDiagonalLeft).getTeamColor() != ChessGame.TeamColor.WHITE) {
                if (myPosition.getRow() + 1 == 8) {
                    for (ChessPiece.PieceType promotion : promotionArray) {
                        ChessMove newChessMove = new ChessMove(myPosition, whiteDiagonalLeft, promotion);
                        possibleMoves.add(newChessMove);
                    }
                }
                else {
                    ChessMove newChessMove = new ChessMove(myPosition, whiteDiagonalLeft, null);
                    possibleMoves.add(newChessMove);
                }
            }
        }

        return possibleMoves;
    }


}


