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

import Domain.Chess.TurnSystem;
import Shared.Chess.Board;
import Networking.Server;
import Domain.Chess.Variants.NormalChess;
import Shared.Chess.Coordinate;
import Shared.Networking.GameFinishedMessage;
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
    NormalChess variant;
    Server server;
    int turn = 0;
    private Board board;
    private TurnSystem turnSystem;

    //idee dat ergens moet staan, vast niet hier. hopelijk lees ik het nog eens
    //ipv bij promotie auto dame/laten kiezen, tijdens de match in de GUI reeds kunnen aanduiden wat de volgende promotie moet zijn.
    public Game(Socket player1, Socket player2, Server server, NormalChess variant) {
        this.server = server;
        this.player1 = player1;
        this.player2 = player2;
        if (player1 == player2) {
            System.out.println("DAFUQ, da meende nu toch niet.");
        }
        this.variant = variant;
        board = variant.getBoard();
        turnSystem = variant.getTurnSystem();
        turnSystem.setPlayer1(player1);
        turnSystem.setPlayer2(player2);
    }

    public void start() {
        sendTurnMessages();
    }

    boolean hasPlayer(Socket source) {
        return player1 == source || player2 == source;
    }

    boolean movePieceIfAllowed(Coordinate fromCoord, Coordinate toCoord) {
        boolean wasAllowed = variant.isMoveAllowed(fromCoord, toCoord);
        //TODO niet meer nodig als isMoveallowed werkt op de copy
        board = variant.getBoard();
        //nog checken of het command komt van dienen aan de beurt, en of dat een stuk is van hem
        if (wasAllowed) {
            boolean playerIsWhite = board.getPiece(fromCoord).isWhite();
            variant.movePiece(fromCoord, toCoord);
            if (variant.isLost(!playerIsWhite)) {
                gameWon(turn == 0);
            } else if (variant.isStaleMated(!playerIsWhite)) {
                gameDrawed();
            } else {
                nextTurn();
            }
        }
        return wasAllowed;
    }

    Iterable<Socket> getPlayers() {
        return new ImmutableObservableList<>(player1, player2);
    }

    private void nextTurn() {
        turnSystem.nextTurn();
        sendTurnMessages();
    }

    //TODO afgehandelde games nog verwijderen uit gamemeneger (met een callback)
    public void gameWon(boolean player1Won) {
        Socket winner = player1Won ? player1 : player2;
        Socket loser = player1Won ? player2 : player1;
        server.sendMessage(winner, new GameFinishedMessage(1));
        server.sendMessage(loser, new GameFinishedMessage(0));
    }

    public void gameDrawed() {
        //tijdelijk 2 = DRAW
        server.sendMessage(player1, new GameFinishedMessage(2));
        server.sendMessage(player2, new GameFinishedMessage(2));
    }

    Socket getOtherPlayer(Socket source) {
        return player1 == source ? player2 : player1;
    }

    public Board getBoard() {
        return board;
    }

    private void sendTurnMessages() {
        server.sendMessage(turnSystem.getWhoseTurn(), new TurnMessage(true));
        server.sendMessage(turnSystem.getNotWhoseTurn(), new TurnMessage(false));
    }

}
