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
import static Shared.Chess.ChessPiece.images;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Drareeg
 */
public class Pawn extends ChessPiece {

    private Coordinate forward;
    private Coordinate twoForward;
    private Coordinate leftFront; //left is niet per se left voor de pion zelf.
    private Coordinate rightFront;//zelfde als hierboven

    public Pawn(boolean isWhite) {
        super(isWhite);
        int direction = isWhite ? -1 : 1;
        forward = new Coordinate(direction, 0);
        twoForward = new Coordinate(direction * 2, 0);
        leftFront = new Coordinate(direction, 1);
        rightFront = new Coordinate(direction, -1);
    }

    @Override
    public boolean canReachFromTo(Coordinate fromCoord, Coordinate toCoord, Board board) {
        //vakje ervoor & leeg
        Coordinate testCoord = fromCoord.add(forward);
        boolean vakErvoorLeeg = board.isValidCoordinate(testCoord) && !board.hasPiece(testCoord);
        if (toCoord.equals(testCoord) && vakErvoorLeeg) {
            return true;
        }
        //niet begwogen & 2 vakken ervoor
        testCoord = fromCoord.add(twoForward);
        if (board.isValidCoordinate(testCoord)) {
            boolean tweeVakkenErvoorLeeg = !board.hasPiece(testCoord);
            if (!hasMoved && toCoord.equals(testCoord) && vakErvoorLeeg && tweeVakkenErvoorLeeg && (fromCoord.getRow() == 1 || fromCoord.getRow() == 6)) {
                return true;
            }
        }
        //linkservoor && daar staat enemy
        testCoord = fromCoord.add(leftFront);
        if (board.isValidCoordinate(testCoord)) {
            ChessPiece takePiece = board.getPiece(testCoord); //het stuk dat het potentieen kan pakken
            if (toCoord.equals(testCoord) && takePiece != null && !takePiece.isSameColor(this)) {
                return true;
            }
        }
        //rechtservoor && daar staat enemy
        testCoord = fromCoord.add(rightFront);
        if (board.isValidCoordinate(testCoord)) {
            ChessPiece takePiece = board.getPiece(testCoord); //het stuk dat het potentieen kan pakken
            if (toCoord.equals(testCoord) && takePiece != null && !takePiece.isSameColor(this)) {
                return true;
            }
        }
        //iets met en passant (als je met een pion 2 vooruit speel ergens voor 1 beurt bijhouden welke pionen u hoe mogen en passanten)
        //wss: op moment daje met nen pion 2 vakken vooruit knalt instellen bij de 2 potentiele pionen ernaast dat ze mogen en passanten op veld X en enkel beurt zelf
        return false;
    }

    @Override
    public ArrayList<ChessPiece> getAttackedPieces(Board board, Coordinate pieceLocation) {
        ArrayList<ChessPiece> attackedPieces = new ArrayList();
        Coordinate testCoord = rightFront.add(pieceLocation);
        if (board.isValidCoordinate(testCoord)) {
            if (board.hasPiece(testCoord)) {
                attackedPieces.add(board.getPiece(testCoord));
            }
        }
        testCoord = leftFront.add(pieceLocation);
        if (board.isValidCoordinate(testCoord)) {
            if (board.hasPiece(testCoord)) {
                attackedPieces.add(board.getPiece(testCoord));
            }
        }
        if (!attackedPieces.isEmpty()) {
            System.out.println("Pion op " + pieceLocation.toString() + " valt " + attackedPieces.size() + " stukken aan");
        }
        return attackedPieces;
    }

    @Override
    public Image getImage(boolean amIWhite) {
        return new Image(images.get("Pawn" + (this.isWhite ? "W" : "B")));
    }

    @Override
    public void executeMove(Coordinate fromCoord, Coordinate toCoord, Board b) {
        super.executeMove(fromCoord, toCoord, b);
        if (toCoord.getRow() == 7 || toCoord.getRow() == 0) {
            b.setPiece(toCoord, new Queen(isWhite));
        }
    }
}
