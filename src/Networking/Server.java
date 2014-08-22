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
package Networking;

import Domain.GameManager;
import Shared.Networking.AcceptChallengeMessage;
import Shared.Networking.ChallengeMessage;
import Shared.Networking.ChatMessage;
import Shared.Networking.GameStartMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import Shared.Networking.JoinLobbyMessage;
import Shared.Networking.LeaveLobbyMessage;
import Shared.Networking.Message;
import Shared.Networking.MoveMessage;
import Shared.Networking.ThisIsTheLobbyMessage;
import java.util.concurrent.ConcurrentHashMap;
import Shared.Networking.MessageHandler;
import Shared.Networking.ThisIsTheBoardMessage;
import Shared.Networking.TurnMessage;

/**
 *
 * @author Dries
 */
public class Server implements MessageHandler {

    private ConcurrentHashMap<Socket, ObjectOutputStream> clientOosMap;
    private ConcurrentHashMap<Socket, String> clientUsernameMap;
    //incoming messages from all clients
    private LinkedBlockingQueue<Message> messageQueue;
    private GameManager gameManager;

    public Server() {
        clientOosMap = new ConcurrentHashMap<>();
        clientUsernameMap = new ConcurrentHashMap<>();
        gameManager = new GameManager(this);
        //start nieuwe ListenToOneClientThread per connectie die hij binnen krijgt.
        new ConnectionAccepterThread(this).start();
        System.out.println("Beep Boop: server is running");
        messageQueue = new LinkedBlockingQueue<Message>();
        handleIncomingMessages();
    }

    public void addMessage(Message message) {
        messageQueue.add(message);
    }

    private void handleIncomingMessages() {
        while (true) {
            try {
                //na een jaar zonder message hebt ge een probleem
                Message toHandle = messageQueue.poll(356, TimeUnit.DAYS);
                toHandle.handleSelf(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //aangeroepen vanaf de connectionaccepterthread
    public void acceptNewConnection(Socket connection) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connection.getOutputStream());
            //flush to send header, otherwise client keeps blocking
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        new ListenToOneClientThread(connection, this).start();
        clientOosMap.put(connection, oos);
    }

    public void handleJoinLobby(JoinLobbyMessage message) {
        ThisIsTheLobbyMessage thisIsTheLobbyMessage = new ThisIsTheLobbyMessage(clientUsernameMap.values());
        sendMessage(message.getSource(), thisIsTheLobbyMessage);
        clientUsernameMap.put(message.getSource(), message.getUsername());
        broadcast(new JoinLobbyMessage(message.getUsername()));
    }

    private void broadcast(Message message) {
        for (Socket socket : clientOosMap.keySet()) {
            sendMessage(socket, message);
        }
    }

    public void sendMessage(Socket client, Message message) {
        //als we in een andere methode (bv chat) een message die we ontvangen rechtstreeks willen broadcasten, dan moeten we daar dit niet doen
        message.setSource(null);
        try {
            clientOosMap.get(client).reset();
            clientOosMap.get(client).writeUnshared(message);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleChallenge(ChallengeMessage challengeMessage) {
        // GameStartMessage message = new GameStartMessage();
        // sendMessage(challengeMessage.getSource(), message);
        for (Socket client : clientUsernameMap.keySet()) {
            if (clientUsernameMap.get(client).equals(challengeMessage.getTarget())) {
                sendMessage(client, challengeMessage.setSource(null));
            }
        }
        // gameManager.handleChallenge(challengeMessage);
    }

    public void handleMove(MoveMessage moveMessage) {
        gameManager.handleMove(moveMessage);
    }

    public void handleGameStart(GameStartMessage gameStartMessage) {
        //TODO
    }

    @Override
    public void handleThisIsTheLobbyMessage(ThisIsTheLobbyMessage thisIsTheLobby) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void clientDisconnected(Socket connection) {
        String leaverName = this.clientUsernameMap.remove(connection);
        this.clientOosMap.remove(connection);
        broadcast(new LeaveLobbyMessage(leaverName));
    }

    //dit moet de server niet doen, dit is nl een message dat de server vestuurd als hij ontdekt dat een client weg is
    @Override
    public void handleLeaveLobby(LeaveLobbyMessage leaveLobby) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleChatMessage(ChatMessage aThis) {
        broadcast(aThis);
    }

    @Override
    public void handleThisIsTheBoard(ThisIsTheBoardMessage aThis) {
        throw new UnsupportedOperationException("Enkel server verstuurd dit."); //To change body of generated methods, choose Tools | Templates.
    }

    public Socket getSocketFromUsername(String target) {
        for (Socket client : clientUsernameMap.keySet()) {
            if (clientUsernameMap.get(client).equals(target)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public void handleTurnMessage(TurnMessage aThis) {
        throw new UnsupportedOperationException("enkel voor client."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleAcceptChallenge(AcceptChallengeMessage aThis) {
        gameManager.handleChallengeAccepted(aThis);
    }

    public String getUserNameFromSocket(Socket socket) {
        return clientUsernameMap.get(socket);
    }
}
