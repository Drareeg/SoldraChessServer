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
package Shared.Chess;
import java.io.Serializable;

/**
 *
 * @author Geerard
 */
public class Board implements Serializable {
    protected Position position;

    //mergen met Position ...?
    public Board() {
        position = new Position();
        position.setPiece(0, 0, new Rook(false));
        position.setPiece(0, 7, new Rook(false));
        position.setPiece(7, 0, new Rook(true));
        position.setPiece(7, 7, new Rook(true));
        position.setPiece(0, 1, new Knight(false));
        position.setPiece(0, 6, new Knight(false));
        position.setPiece(7, 1, new Knight(true));
        position.setPiece(7, 6, new Knight(true));
        position.setPiece(0, 2, new Bishop(false));
        position.setPiece(0, 5, new Bishop(false));
        position.setPiece(7, 2, new Bishop(true));
        position.setPiece(7, 5, new Bishop(true));
        position.setPiece(0, 3, new Queen(false));
        position.setPiece(7, 3, new Queen(true));
        position.setPiece(0, 4, new King(false));
        position.setPiece(7, 4, new King(true));
        for (int i = 0; i < 8; i++) {
            position.setPiece(1, i, new Pawn(false));
            position.setPiece(6, i, new Pawn(true));
        }
    }

    //copy constructor
    private Board(Board board) {
        position = new Position(board.getPosition());
    }

    public boolean isValidCoordinate(Coordinate coord) {
        return position.isValidCoordinate(coord);
    }

    public ChessPiece getPiece(Coordinate fromCoord) {
        return position.getPiece(fromCoord);
    }

    public Board getDeepCopy() {
        return new Board(this);
    }

    public void setPiece(Coordinate coordinate, ChessPiece piece) {
        position.setPiece(coordinate, piece);
    }

    public boolean hasPiece(Coordinate coord) {
        return position.hasPiece(coord);
    }

    public Position getPosition() {
        return position;
    }

    boolean underAttack(Coordinate coordinate, boolean attackerIsWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Coordinate testCoord = new Coordinate(row, col);
                ChessPiece testPiece = position.getPiece(testCoord);
                if (testPiece != null && testPiece.isWhite() == attackerIsWhite) {
                    if (testPiece.canReachFromTo(coordinate, coordinate, this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //geeft de coordinate die hiermee overeenkomt die wel op het bord ligt (handig voor cilinerschaak)
    Coordinate adjustToBoard(Coordinate coord) {
        return coord;
    }

}
