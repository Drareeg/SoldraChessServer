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
import javafx.scene.image.Image;

/**
 *
 * @author Drareeg
 */
public class Bishop extends ChessPiece {

    //manier van werken: (potentieel tijdelijk)
    //Lijst hebben van lijsten verkenvolgordes. bv bij een loper: een lijst met lijsten van coordinaten naar linksonder/rechtsboven/...
    //dan per lijst kijken tot waar de lijst legaal is en kijken of de voorgestelde zet ertussen zit
    //dus: "de zetten zoeken waar hij heen kan, en kijken of die er tussen zit"
    //dat is wss wat meer werk dan "controleren of de werk geldig is", maar verwaarloosbaar verlies (hoop ik)
    public Bishop(boolean isWhite) {
        super(isWhite);
        List<Coordinate> list1 = new ArrayList<>();
        List<Coordinate> list2 = new ArrayList<>();
        List<Coordinate> list3 = new ArrayList<>();
        List<Coordinate> list4 = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            list1.add(new Coordinate(1 * i, 1 * i));
            list2.add(new Coordinate(-1 * i, 1 * i));
            list3.add(new Coordinate(1 * i, -1 * i));
            list4.add(new Coordinate(-1 * i, -1 * i));
        }
        possibleMovesListList.add(list1);
        possibleMovesListList.add(list2);
        possibleMovesListList.add(list3);
        possibleMovesListList.add(list4);
    }

    @Override
    public Image getImage(boolean amIWhite) {
        return new Image(images.get("Bishop" + (this.isWhite ? "W" : "B")));
    }

}
