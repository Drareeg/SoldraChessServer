/*
 * The MIT License
 *
 * Copyright 2014 Drareeg.
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
import UI.BoardChangeListener;
import java.io.Serializable;

/**
 *
 * @author Drareeg
 */
public class Board implements Serializable {
    private ChessPiece[][] model;

    public Board() {
        model = new ChessPiece[8][8];
        model[0][0] = new Rook(false);
        model[0][7] = new Rook(false);
        model[7][0] = new Rook(true);
        model[7][7] = new Rook(true);
        model[0][1] = new Knight(false);
        model[0][6] = new Knight(false);
        model[7][1] = new Knight(true);
        model[7][6] = new Knight(true);
        model[0][2] = new Bishop(false);
        model[0][5] = new Bishop(false);
        model[7][2] = new Bishop(true);
        model[7][5] = new Bishop(true);
        model[0][3] = new Queen(false);
        model[7][3] = new Queen(true);
        model[0][4] = new King(false);
        model[7][4] = new King(true);
        for (int i = 0; i < 8; i++) {
            model[1][i] = new Pawn(false);
            model[6][i] = new Pawn(true);
        }
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        model[toRow][toCol] = model[fromRow][fromCol];
        model[fromRow][fromCol] = null;
        //magneetschaak
        attract(toRow, toCol, 1, 0);
        attract(toRow, toCol, -1, 0);
        attract(toRow, toCol, 0, 1);
        attract(toRow, toCol, 0, -1);
        fireChanged();
    }

    public ChessPiece getPiece(int row, int col) {
        return model[row][col];
    }

    BoardChangeListener bcl;

    private void fireChanged() {
        if (bcl != null) {
            bcl.boardChanged();
        }
    }

    public void setBCL(BoardChangeListener aThis) {
        this.bcl = aThis;
    }

    private void attract(int row, int col, int rdiff, int cdiff) {
        int testR = row;
        int testC = col;
        boolean done = false;
        while (testR + rdiff >= 0 && testR + rdiff <= 7 && testC + cdiff >= 0 && testC + cdiff <= 7 && !done) {
            testR += rdiff;
            testC += cdiff;
            if (model[testR][testC] != null) {
                done = true;
                //als het ernaast stond willen we het niet verwijderen
                if (!(testR - row == rdiff && testC - col == cdiff)) {
                    model[row + rdiff][ col + cdiff] = model[testR][testC];
                    model[testR][testC] = null;
                }
            }
        }
    }

    public void updateTo(Board board) {
        this.model = board.model;
        fireChanged();
    }

    public boolean isMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
        return model[fromRow][fromCol].canMoveFromTo(fromRow, fromCol, toRow, toCol, this);
    }
}
