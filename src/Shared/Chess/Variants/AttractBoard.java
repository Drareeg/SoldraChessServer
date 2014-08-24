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
package Shared.Chess.Variants;
import Shared.Chess.Coordinate;

/**
 *
 * @author Geerard
 */
public class AttractBoard extends Board {
    @Override
    public void postMove(Coordinate fromCoord, Coordinate toCoord) {
        attract(toCoord, 1, 0);
        attract(toCoord, -1, 0);
        attract(toCoord, 0, 1);
        attract(toCoord, 0, -1);
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
}
