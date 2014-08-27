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

/**
 *
 * @author Geerard
 */
public class CilinderBoard extends Board {

    public CilinderBoard() {
    }

    private CilinderBoard(Board board) {
        position = new Position(board.getPosition());
    }

    @Override
    public boolean isValidCoordinate(Coordinate coord) {
        return coord.getCol() <= 15 && coord.getCol() >= -8 && coord.getRow() <= 7 && coord.getRow() >= 0;
    }

    @Override
    public ChessPiece getPiece(Coordinate fromCoord) {
        return position.getPiece(adjustToBoard(fromCoord));
    }

    @Override
    public Board getDeepCopy() {
        return new CilinderBoard(this);
    }

    //geeft de coordinate die hiermee overeenkomt die wel op het bord ligt (handig voor cilinerschaak)
    @Override
    Coordinate adjustToBoard(Coordinate coord) {
        if (coord.getCol() > 7) {
            return new Coordinate(coord.getRow(), coord.getCol() - 8);
        } else if (coord.getCol() < 0) {
            return new Coordinate(coord.getRow(), coord.getCol() + 8);
        } else {
            return coord;
        }
    }
}
