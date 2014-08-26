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
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author Drareeg
 */
public class King extends ChessPiece {

    public King(boolean isWhite) {
        super(isWhite);
        List<Coordinate> list1 = new ArrayList<>();
        List<Coordinate> list2 = new ArrayList<>();
        List<Coordinate> list3 = new ArrayList<>();
        List<Coordinate> list4 = new ArrayList<>();
        List<Coordinate> list5 = new ArrayList<>();
        List<Coordinate> list6 = new ArrayList<>();
        List<Coordinate> list7 = new ArrayList<>();
        List<Coordinate> list8 = new ArrayList<>();
        list1.add(new Coordinate(0, 1));
        list2.add(new Coordinate(0, -1));
        list3.add(new Coordinate(1, -1));
        list4.add(new Coordinate(1, 0));
        list5.add(new Coordinate(1, 1));
        list6.add(new Coordinate(-1, -1));
        list7.add(new Coordinate(-1, 0));
        list8.add(new Coordinate(-1, 1));
        possibleMovesListList.add(list1);
        possibleMovesListList.add(list2);
        possibleMovesListList.add(list3);
        possibleMovesListList.add(list4);
        possibleMovesListList.add(list5);
        possibleMovesListList.add(list6);
        possibleMovesListList.add(list7);
        possibleMovesListList.add(list8);
    }

    @Override
    public Image getImage(boolean amIWhite) {
        return new Image(images.get("King" + (this.isWhite ? "W" : "B")));
    }

    @Override
    public boolean canReachFromTo(Coordinate fromCoord, Coordinate toCoord, Board board) {
        if (super.canReachFromTo(fromCoord, toCoord, board)) {
            return true;
        } else {
            if (isCastleShort(toCoord)) {
                return canCastleLong(board);
            }
            if (isCastleLong(toCoord)) {
                return canCastleShort(board);
            }
        }
        return false;
    }

    @Override
    public void executeMove(Coordinate fromCoord, Coordinate toCoord, Board board) {
        if (isCastleLong(toCoord)) {
            int row = isWhite ? 7 : 0;
            Coordinate rookCoord = new Coordinate(row, 0);
            board.getPiece(rookCoord).executeMove(rookCoord, new Coordinate(row, 3), board);
        } else if (isCastleShort(toCoord)) {
            int row = isWhite ? 7 : 0;
            Coordinate rookCoord = new Coordinate(row, 7);
            board.getPiece(rookCoord).executeMove(rookCoord, new Coordinate(row, 5), board);
        }
        super.executeMove(fromCoord, toCoord, board);
    }

    private boolean isCastleShort(Coordinate toCoord) {
        return !hasMoved && toCoord.getCol() == 6;
    }

    private boolean isCastleLong(Coordinate toCoord) {
        return !hasMoved && toCoord.getCol() == 2;
    }

    private boolean canCastleLong(Board board) {
        int row = isWhite ? 7 : 0;
        ChessPiece rook = board.getPiece(new Coordinate(row, 0));
        if (hasMoved || rook == null || rook.hasMoved) {
            return false;
        }
        Coordinate[] hasToBeEmpty = new Coordinate[]{new Coordinate(row, 1), new Coordinate(row, 2), new Coordinate(row, 3)};
        for (Coordinate coordinate : hasToBeEmpty) {
            if (board.hasPiece(coordinate)) {
                return false;
            }
        }
        //king is nog allowed to pass in check chess, nor be in check when starting castling
        Coordinate[] kingPassesHere = new Coordinate[]{new Coordinate(row, 2), new Coordinate(row, 3), new Coordinate(row, 4)};
        for (Coordinate coordinate : kingPassesHere) {
            if (board.underAttack(coordinate, !isWhite)) {
                return false;
            }
        }
        return true;
    }

    private boolean canCastleShort(Board board) {
        int row = isWhite ? 7 : 0;
        ChessPiece rook = board.getPiece(new Coordinate(row, 7));
        if (hasMoved || rook == null || rook.hasMoved) {
            return false;
        }
        Coordinate[] hasToBeEmpty = new Coordinate[]{new Coordinate(row, 5), new Coordinate(row, 6)};
        for (Coordinate coordinate : hasToBeEmpty) {
            if (board.hasPiece(coordinate)) {
                return false;
            }
        }
        //king is nog allowed to pass in check chess, nor be in check when starting castling
        Coordinate[] kingPassesHere = new Coordinate[]{new Coordinate(row, 4), new Coordinate(row, 5), new Coordinate(row, 6)};
        for (Coordinate coordinate : kingPassesHere) {
            if (board.underAttack(coordinate, !isWhite)) {
                return false;
            }
        }
        return true;
    }
}
