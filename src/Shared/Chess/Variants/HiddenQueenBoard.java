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
import Shared.Chess.HiddenQueen;
import Shared.Networking.Message;
import Shared.Networking.ThisIsMyHiddenQueenMessage;

/**
 *
 * @author Geerard
 */
public class HiddenQueenBoard extends Board {

    boolean hasWhiteChoosenQueen;
    boolean hasBlackChoosenQueen;

    @Override
    public void handleCustomMessage(Message message) {
        if (message instanceof ThisIsMyHiddenQueenMessage) {
            ThisIsMyHiddenQueenMessage qMessage = (ThisIsMyHiddenQueenMessage) message;
            model[qMessage.isSentByWhite() ? 6 : 1][qMessage.getCol()] = new HiddenQueen(qMessage.isSentByWhite());
            if (qMessage.isSentByWhite()) {
                hasWhiteChoosenQueen = true;
            } else {
                hasBlackChoosenQueen = true;
            }
        }
        super.fireChanged();
    }

    public boolean hasChosenQueen(boolean amIWhite) {
        return amIWhite ? hasWhiteChoosenQueen : hasBlackChoosenQueen;
    }

    public void updateTo(Board board) {
        hasWhiteChoosenQueen = ((HiddenQueenBoard) board).hasWhiteChoosenQueen;
        hasBlackChoosenQueen = ((HiddenQueenBoard) board).hasBlackChoosenQueen;
        this.model = board.model;
        fireChanged();
    }

}