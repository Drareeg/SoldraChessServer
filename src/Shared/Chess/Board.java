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

    //copy constr -> nog steeds zelfde chesspiece objecten, oppassen met hasmoved dat niet veranderd!
    private Board(Board from) {
        this.model = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                this.model[row][col] = from.model[row][col];
            }
        }
    }

    public void movePiece(Coordinate fromCoord, Coordinate toCoord) {
        int fromRow = fromCoord.getRow();
        int fromCol = fromCoord.getCol();
        int toRow = toCoord.getRow();
        int toCol = toCoord.getCol();
        model[toRow][toCol] = model[fromRow][fromCol];
        model[fromRow][fromCol] = null;
        //tornado
        //tornado();
        //magneetschaak
//        attract(toCoord, 1, 0);
//        attract(toCoord, -1, 0);
//        attract(toCoord, 0, 1);
//        attract(toCoord, 0, -1);
        fireChanged();
    }

    private void tornado() {
        ChessPiece temp = model[3][3];
        model[3][3] = model[4][3];
        model[4][3] = model[4][4];
        model[4][4] = model[3][4];
        model[3][4] = temp;
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

    private void attract(Coordinate origin, int rdiff, int cdiff) {
        int row = origin.getRow();
        int col = origin.getCol();
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

    public boolean isMoveAllowed(Coordinate fromCoord, Coordinate toCoord) {
        boolean playerIsWhite = this.getPiece(fromCoord).isWhite;
        Board boardCopy = new Board(this);
        boardCopy.movePiece(fromCoord, toCoord);
        if (boardCopy.isInCheck(playerIsWhite)) { // als de zet uitgevoerd resulteert in schaak van de kleur van de zetter -> mag niet
            return false;
        }
        return model[fromCoord.getRow()][fromCoord.getCol()].canMoveFromTo(fromCoord, toCoord, this);
    }

    boolean containsCoordinate(Coordinate coord) {
        return coord.getRow() >= 0 && coord.getRow() <= 7
                && coord.getCol() >= 0 && coord.getCol() <= 7;
    }

    boolean hasPiece(Coordinate coord) {
        return model[coord.getRow()][coord.getCol()] != null;
    }

    public ChessPiece getPiece(Coordinate coord) {
        return model[coord.getRow()][coord.getCol()];
    }

    private boolean isInCheck(boolean white) { //is white/black in check
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                ChessPiece piece = model[row][col];
                if (piece != null && piece.isWhite != white) { //je kan enkel schaak staan door enemy stukken
                    for (ChessPiece attackedPiece : piece.getAttackedPieces(this, new Coordinate(row, col))) {
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

    public boolean isMate(boolean checkingForWhite) {
        if (isInCheck(checkingForWhite)) {
            return hasNoLegalMoves(checkingForWhite);
        }
        return false;
    }

    public boolean isStaleMate(boolean checkingForWhite) {
        if (!isInCheck(checkingForWhite)) {
            return hasNoLegalMoves(checkingForWhite);
        }
        return false;
    }

    private boolean hasNoLegalMoves(boolean checkingForWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece pieceToCheck = model[row][col];
                if (pieceToCheck != null && pieceToCheck.isWhite == checkingForWhite) {
                    Coordinate pieceLocation = new Coordinate(row, col);
                    for (Coordinate reachableField : pieceToCheck.getReachableCoords(pieceLocation, this)) {
                        if (this.isMoveAllowed(pieceLocation, reachableField)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
