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
package Shared.Networking;
import Shared.Chess.Variants.Variant;

/**
 * S -> C: a new game started for you
 *
 * @author Drareeg
 */
public class GameStartMessage extends Message {

    boolean amIWhite;
    String againstName;
    Variant variant;

    public GameStartMessage(boolean amIWhite, String againstName, Variant variant) {
        this.amIWhite = amIWhite;
        this.againstName = againstName;
        this.variant = variant;
    }

    public boolean AmIWhite() {
        return amIWhite;
    }

    public String getAgainstName() {
        return againstName;
    }

    public Variant getVariant() {
        return variant;
    }

    @Override
    public void handleSelf(MessageHandler m) {
        m.handleGameStart(this);
    }

}
