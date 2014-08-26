/*
 * The MIT License
 *
 * Copyright 2014 Dries Weyme & Geerard Ponnet.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Domain.Chess;
import Domain.Chess.Variants.NormalChess;
import Shared.Chess.ChessPiece;
import Shared.Chess.Coordinate;
import Shared.Chess.King;

/**
 *
 * @author Geerard
 */
public class PositionJuror {

    //ook oude versie meegeven? bv in geval van weggeefschaak is nodig om te weten wat voorgaande stukkentelling was -> naah oplossen door overschrijven in weggeefschaak
    public boolean isValid(Board board, boolean whiteToPlay) {
        return isInCheck(board, whiteToPlay);
    }

    private boolean isInCheck(Board board, boolean white) { //is white/black in check
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                ChessPiece piece = board.getPiece(new Coordinate(row, col));
                if (piece != null && piece.isWhite != white) { //je kan enkel schaak staan door enemy stukken
                    for (ChessPiece attackedPiece : piece.getAttackedPieces(board, new Coordinate(row, col))) {
                        if (attackedPiece instanceof King && attackedPiece.isWhite == white) {
                            System.out.println("isincheck");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isLost(NormalChess variant, Board board, boolean checkingForWhite) {
        if (isInCheck(board, checkingForWhite)) {
            return hasNoLegalMoves(variant, board, checkingForWhite);
        }
        return false;
    }

    public boolean isStaleMated(NormalChess variant, Board board, boolean checkingForWhite) {
        if (!isInCheck(board, checkingForWhite)) {
            return hasNoLegalMoves(variant, board, checkingForWhite);
        }
        return false;
    }

    private boolean hasNoLegalMoves(NormalChess variant, Board board, boolean checkingForWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece pieceToCheck = board.getPiece(new Coordinate(row, col));
                if (pieceToCheck != null && pieceToCheck.isWhite == checkingForWhite) {
                    Coordinate pieceLocation = new Coordinate(row, col);
                    for (Coordinate reachableField : pieceToCheck.getReachableCoords(pieceLocation, board)) {
                        if (variant.isMoveAllowed(pieceLocation, reachableField)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
