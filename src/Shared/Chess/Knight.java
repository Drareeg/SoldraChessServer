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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Drareeg
 */
public class Knight extends ChessPiece {

    public Knight(boolean isWhite) {
        super(isWhite);
        List<Coordinate> list1 = new ArrayList<>();
        List<Coordinate> list2 = new ArrayList<>();
        List<Coordinate> list3 = new ArrayList<>();
        List<Coordinate> list4 = new ArrayList<>();
        List<Coordinate> list5 = new ArrayList<>();
        List<Coordinate> list6 = new ArrayList<>();
        List<Coordinate> list7 = new ArrayList<>();
        List<Coordinate> list8 = new ArrayList<>();
        list1.add(new Coordinate(1, 2));
        list2.add(new Coordinate(1, -2));
        list3.add(new Coordinate(-1, 2));
        list4.add(new Coordinate(-1, -2));
        list5.add(new Coordinate(2, 1));
        list6.add(new Coordinate(2, -1));
        list7.add(new Coordinate(-2, 1));
        list8.add(new Coordinate(-2, -1));
        possibleMovesListList.add(list1);
        possibleMovesListList.add(list2);
        possibleMovesListList.add(list3);
        possibleMovesListList.add(list4);
        possibleMovesListList.add(list5);
        possibleMovesListList.add(list6);
        possibleMovesListList.add(list7);
        possibleMovesListList.add(list8);
    }

}
