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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Drareeg
 */
public abstract class ChessPiece implements Serializable {
    //tijdelijke representatie van een stuk is een letter.
    public boolean isWhite;
    public boolean hasMoved;
    public List<List<Coordinate>> possibleMovesListList;

    public ChessPiece(boolean isWhite) {
        this.isWhite = isWhite;
        possibleMovesListList = new ArrayList<>();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isSameColor(ChessPiece piece) {
        return this.isWhite == piece.isWhite;
    }

    boolean canMoveFromTo(Coordinate fromCoord, Coordinate toCoord, Board board) {
        for (List<Coordinate> coordList : possibleMovesListList) {
            for (Coordinate diffCoord : coordList) {
                Coordinate testCoord = diffCoord.add(fromCoord);
                if (board.containsCoordinate(testCoord)) {
                    if (board.hasPiece(testCoord)) {
                        if (this.isSameColor(board.getPiece(testCoord))) {
                            break; // als het stuk dat we tegenkomen dezelfde kleur is gaan we over naar de volgende lijst
                        } else {
                            //stuk verschillende kleur -> mag enkel als testcoord is waar het stuk naartoe wou gaan
                            if (!testCoord.equals(toCoord)) {
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
                //nu weten we dus dat testCoord bereikbaar is voor het stuk
                if (testCoord.equals(toCoord)) {
                    return true;
                }
            }
        }
        //als we het niet zijn tegengekomen mag de zet niet
        return false; //return checkspecialstuff() voor rokade en en passant en pion first move
    }

    ArrayList<ChessPiece> getAttackedPieces(Board board, Coordinate pieceLocation) {
        ArrayList<ChessPiece> attackedPieces = new ArrayList();
        for (List<Coordinate> coordList : possibleMovesListList) {
            for (Coordinate diffCoord : coordList) {
                Coordinate testCoord = diffCoord.add(pieceLocation);
                if (board.containsCoordinate(testCoord)) {
                    if (board.hasPiece(testCoord)) {
                        attackedPieces.add(board.getPiece(testCoord));
                        break;
                    }
                }
            }
        }
        return attackedPieces;
    }
}
