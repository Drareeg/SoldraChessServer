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
import Domain.Chess.Variants.AttractChess;
import Domain.Chess.Variants.CilinderChess;
import Domain.Chess.Variants.NormalChess;
import Domain.Chess.Variants.HiddenQueenChess;
import Domain.Chess.Variants.OneTwoThreeChess;
import Domain.Chess.Variants.TornadoChess;
import Shared.Chess.Variant;
import Shared.Networking.AcceptChallengeMessage;
import Shared.Networking.GameFinishedMessage;
import Shared.Networking.GameStartMessage;
import Shared.Networking.MoveMessage;
import Shared.Networking.SurrenderMessage;
import Shared.Networking.ThisIsMyHiddenQueenMessage;
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

    //TODO afgehandelde games nog afhandelen
    private List<Game> games;

    public GameManager(Server server) {
        this.server = server;
        games = new ArrayList<>();
    }

    public void handleMove(MoveMessage moveMessage) {
        for (Game game : games) {
            if (game.hasPlayer(moveMessage.getSource())) {
                if (game.movePieceIfAllowed(moveMessage.getFromCoord(), moveMessage.getToCoord())) {
                    for (Socket s : game.getPlayers()) {
                        server.sendMessage(s, new ThisIsTheBoardMessage(game.getBoard()));
                    }
                }
            }
        }
    }

    public void handleChallengeAccepted(AcceptChallengeMessage aThis) {
        //controlleer of challenger nog geen spel bezig is
        //maak spel aan en laat beiden weten over het spel
        Socket p1 = aThis.getSource();
        Socket p2 = server.getSocketFromUsername(aThis.getChallenge().getOrigin());
        if (p2 == null) {
            System.out.println("couldnt find socket for username " + aThis.getChallenge().getOrigin());
        }
        Variant v = aThis.getChallenge().getVariant();
        //TODO: board/game logisch onderverdelen nu varianten ontstaan
        NormalChess variant = null;
        if (v.equals(Variant.ATTRACT)) {
            variant = new AttractChess();
        }
        if (v.equals(Variant.HIDDENQUEEN)) {
            variant = new HiddenQueenChess();
        }
        if (v.equals(Variant.TORNADO)) {
            variant = new TornadoChess();
        }
        if (v.equals(Variant.CLASSIC)) {
            variant = new NormalChess();
        }
        if (v.equals(Variant.ONETWOTHREE)) {
            variant = new OneTwoThreeChess();
        }
        if (v.equals(Variant.CILINDER)) {
            variant = new CilinderChess();
        }
        Game game = new Game(p1, p2, server, variant);
        games.add(game);
        server.sendMessage(p1, new GameStartMessage(false, server.getUserNameFromSocket(p2), v)); //wit
        server.sendMessage(p2, new GameStartMessage(true, aThis.getChallenge().getOrigin(), v)); //zwart
        server.sendMessage(p1, new ThisIsTheBoardMessage(variant.getBoard()));
        server.sendMessage(p2, new ThisIsTheBoardMessage(variant.getBoard()));
        game.start();
    }

    public void handleSurrender(SurrenderMessage aThis) {
        for (Game game : games) {
            if (game.hasPlayer(aThis.getSource())) {
                server.sendMessage(game.getOtherPlayer(aThis.getSource()), new GameFinishedMessage(1));
            }
        }
    }

    public void handleHiddenQueen(ThisIsMyHiddenQueenMessage aThis) {
        for (Game game : games) {
            if (game.hasPlayer(aThis.getSource())) {
                game.variant.handleCustomMessage(aThis);
                for (Socket s : game.getPlayers()) {
                    server.sendMessage(s, new ThisIsTheBoardMessage(game.getBoard()));
                }
            }
        }
    }
}
