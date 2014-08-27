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
import UI.SoldraChess;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author Drareeg
 */
public abstract class ChessPiece implements Serializable {
    //tijdelijke representatie van een stuk is een letter.
    public boolean isWhite;
    public boolean hasMoved;
    public List<List<Coordinate>> possibleMovesListList;
    protected Image image;

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

    public boolean canReachFromTo(Coordinate fromCoord, Coordinate toCoord, Board board) {
        for (List<Coordinate> coordList : possibleMovesListList) {
            for (Coordinate diffCoord : coordList) {
                //TODO: in overschrijvende methoden ook rekening houden met cilinderbord dat testcoord eig veranderd
                Coordinate testCoord = board.adjustToBoard(diffCoord.add(fromCoord));
                if (board.isValidCoordinate(testCoord)) {
                    if (board.getPiece(testCoord) != null) {
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

    public ArrayList<ChessPiece> getAttackedPieces(Board board, Coordinate pieceLocation) {
        ArrayList<ChessPiece> attackedPieces = new ArrayList();
        for (List<Coordinate> coordList : possibleMovesListList) {
            for (Coordinate diffCoord : coordList) {
                Coordinate testCoord = diffCoord.add(pieceLocation);
                if (board.isValidCoordinate(testCoord)) {
                    if (board.getPiece(testCoord) != null) {
                        attackedPieces.add(board.getPiece(testCoord));
                        break;
                    }
                }
            }
        }
        return attackedPieces;
    }

    //kan wel wa efficienter ;)
    public ArrayList<Coordinate> getReachableCoords(Coordinate pieceLocation, Board board) {
        ArrayList<Coordinate> reachableFields = new ArrayList();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Coordinate tryToReachThisCoord = new Coordinate(row, col);
                if (canReachFromTo(pieceLocation, tryToReachThisCoord, board)) {
                    reachableFields.add(tryToReachThisCoord);
                }
            }
        }
        return reachableFields;
    }

    public abstract Image getImage(boolean amIWhite);
    public final static HashMap<String, String> images;

    static {
        images = new HashMap<>();
        images.put("RookB", SoldraChess.class.getResource("resources/rook_black.png").toExternalForm());
        images.put("RookW", SoldraChess.class.getResource("resources/rook_white.png").toExternalForm());
        images.put("KnightB", SoldraChess.class.getResource("resources/knight_black.png").toExternalForm());
        images.put("KnightW", SoldraChess.class.getResource("resources/knight_white.png").toExternalForm());
        images.put("BishopB", SoldraChess.class.getResource("resources/bishop_black.png").toExternalForm());
        images.put("BishopW", SoldraChess.class.getResource("resources/bishop_white.png").toExternalForm());
        images.put("QueenB", SoldraChess.class.getResource("resources/queen_black.png").toExternalForm());
        images.put("QueenW", SoldraChess.class.getResource("resources/queen_white.png").toExternalForm());
        images.put("KingB", SoldraChess.class.getResource("resources/king_black.png").toExternalForm());
        images.put("KingW", SoldraChess.class.getResource("resources/king_white.png").toExternalForm());
        images.put("PawnB", SoldraChess.class.getResource("resources/pawn_black.png").toExternalForm());
        images.put("PawnW", SoldraChess.class.getResource("resources/pawn_white.png").toExternalForm());
        images.put("FieldB", SoldraChess.class.getResource("resources/field_black.png").toExternalForm());
        images.put("FieldW", SoldraChess.class.getResource("resources/field_white.png").toExternalForm());
        images.put("Undercover", SoldraChess.class.getResource("resources/undercover.png").toExternalForm());
    }

    public void executeMove(Coordinate fromCoord, Coordinate toCoord, Board b) {
        b.setPiece(toCoord, b.getPiece(fromCoord));
        b.setPiece(fromCoord, null);
    }

}
