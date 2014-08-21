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
import Shared.Networking.ChallengeMessage;
import Shared.Networking.MoveMessage;
import Shared.Networking.ThisIsTheBoardMessage;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Geerard
 */
public class GameManager {

    //om messages te versturen
    private Server server;

    private List<Game> games;

    public GameManager(Server server) {
        this.server = server;
        games = new ArrayList<>();
    }

    //voorlopig betekend een challenge nog dat er een spel is begonnen
    public void handleChallenge(ChallengeMessage challengeMessage) {
        Game game = new Game(challengeMessage.getSource(), server.getSocketFromUsername(challengeMessage.getTarget()), server);
        games.add(game);
    }

    public void handleMove(MoveMessage moveMessage) {
        for (Game game : games) {
            if (game.hasPlayer(moveMessage.getSource())) {
                game.movePiece(moveMessage.getFromCoord(), moveMessage.getToCoord());
                for (Socket s : game.getPlayers()) {
                    server.sendMessage(s, new ThisIsTheBoardMessage(game.board));
                }
            }
        }
    }
}
