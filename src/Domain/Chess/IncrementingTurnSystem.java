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
package Domain.Chess;
import Domain.Chess.PositionJuror;
import Domain.Chess.TurnSystem;
import Shared.Chess.Board;
import java.net.Socket;

/**
 *
 * @author Geerard
 */
public class IncrementingTurnSystem extends TurnSystem {

    PositionJuror positionJuror;
    private int turnsLeft = 1;
    private int lastPlayerGotXTurns = 1;
    private boolean whitePlaying;
    private Board board;

    public IncrementingTurnSystem(PositionJuror positionJuror, Board board) {
        this.positionJuror = positionJuror;
        this.board = board;
    }

    @Override
    public Socket getWhoseTurn() {
        if (whitePlaying) {
            return white;
        } else {
            return black;
        }
    }

    @Override
    public Socket getNotWhoseTurn() {
        if (!whitePlaying) {
            return white;
        } else {
            return black;
        }
    }

    @Override
    public void nextTurn() {
        turnsLeft--;
        //als je beurten op zijn of als je de tegenstander schaak hebt gezet gaat de beurt door
        if (turnsLeft == 0 || positionJuror.isInCheck(board, whitePlaying)) {
            whitePlaying = !whitePlaying;
            lastPlayerGotXTurns++;
            turnsLeft = lastPlayerGotXTurns;
        }
    }

}
