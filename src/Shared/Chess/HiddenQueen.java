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
import static Shared.Chess.ChessPiece.images;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Geerard
 */
public class HiddenQueen extends ChessPiece {

    boolean discovered;
    private Pawn pawn;
    private Queen queen;

    public HiddenQueen(boolean isWhite) {
        super(isWhite);
        pawn = new Pawn(isWhite);
        queen = new Queen(isWhite);
    }

    @Override
    public boolean canMoveFromTo(Coordinate fromCoord, Coordinate toCoord, Board board) {
        return queen.canMoveFromTo(fromCoord, toCoord, board);
    }

    //als we nog nie ontdekt zijn, willen we niet dat onze schaak via dame reeds ontdekt wordt
    @Override
    public ArrayList<ChessPiece> getAttackedPieces(Board board, Coordinate pieceLocation) {
        if (discovered) {
            return queen.getAttackedPieces(board, pieceLocation);
        } else {
            return pawn.getAttackedPieces(board, pieceLocation);
        }
    }

    //wordt gebruikt voor te kijken of je zelf mat staat
    @Override
    public ArrayList<Coordinate> getReachableCoords(Coordinate pieceLocation, Board board) {
        return queen.getReachableCoords(pieceLocation, board);
    }

    @Override
    public Image getImage(boolean fromWhiteStandpoint) {
        if (discovered) {
            return queen.getImage(isWhite);
        }
        if (fromWhiteStandpoint == isWhite) {
            return new Image(images.get("Undercover"));
        }
        return pawn.getImage(isWhite);
    }

    @Override
    public void executeMove(Coordinate fromCoord, Coordinate toCoord, Board b) {
        if (!discovered && !pawn.canMoveFromTo(fromCoord, toCoord, b)) {
            System.out.println("QUEEN DISCOVERED");
            discovered = true;
        }
        super.executeMove(fromCoord, toCoord, b);
    }

}
