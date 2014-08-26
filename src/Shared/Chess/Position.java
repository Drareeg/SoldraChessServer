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
public class Position implements Serializable {

    private ChessPiece[][] model;

    public Position() {
        model = new ChessPiece[8][8];
    }

    Position(Position position) {
        model = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                model[row][col] = position.model[row][col]; //TODO: ook een deep copy pakken via model[][].getDeepCopy();
            }
        }
    }

    public ChessPiece[][] getModel() {
        return model;
    }

    public boolean setPiece(int row, int col, ChessPiece piece) {
        return this.setPiece(new Coordinate(row, col), piece);
    }

    public boolean setPiece(Coordinate coord, ChessPiece piece) {
        boolean hadAPiece = hasPiece(coord);
        model[coord.getRow()][coord.getCol()] = piece;
        return hadAPiece;
    }

    public boolean removePiece(Coordinate coord) {
        return setPiece(coord, null);
    }

    public ChessPiece getPiece(Coordinate coord) {
        return model[coord.getRow()][coord.getCol()];
    }

    public boolean hasPiece(Coordinate coord) {
        return getPiece(coord) != null;
    }

    public boolean isValidCoordinate(Coordinate coord) {
        return coord.getCol() <= 7 && coord.getCol() >= 0 && coord.getRow() <= 7 && coord.getRow() >= 0;
    }

}
