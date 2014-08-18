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
package Domain;
import Networking.Server;
import Shared.Chess.Board;
import Shared.Networking.TurnMessage;
import com.sun.javafx.collections.ImmutableObservableList;
import java.net.Socket;

/**
 *
 * @author Geerard
 */
class Game {
    Socket player1;
    Socket player2;
    Board board;
    Server server;
    int turn = 0;

    public Game(Socket player1, Socket player2, Server server) {
        this.server = server;
        this.player1 = player1;
        this.player2 = player2;
        if (player1 == player2) {
            System.out.println("DAFUQ, da meende nu toch niet.");
        }
        board = new Board();
        nextTurn();
    }

    boolean hasPlayer(Socket source) {
        return player1 == source || player2 == source;
    }

    void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        //nog checken of het command komt van dienen aan de beurt, en of dat een stuk is van hem
        board.movePiece(fromRow, fromCol, toRow, toCol);
        nextTurn();
        //later variantlogica in deze klasse, en hier ook
    }

    Iterable<Socket> getPlayers() {
        return new ImmutableObservableList<>(player1, player2);
    }

    private void nextTurn() {
        turn = (turn + 1) % 2;
        server.sendMessage(player1, new TurnMessage(turn == 0));
        server.sendMessage(player2, new TurnMessage(turn != 0));
    }

}
