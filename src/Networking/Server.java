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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dries
 */
public class Server {

    private Collection<Socket> clients;
    private ArrayList<String> usernames;
    private ArrayList<ObjectOutputStream> ooss;
    //incoming messages from all clients
    private LinkedBlockingQueue<Message> messageQueue;

    public Server() {
        clients = new ArrayList<>();
        usernames = new ArrayList<>();
        ooss = new ArrayList<>();
        System.out.println("Beep Boop: server is running");
        new ConnectionAccepterThread(this).start();
        System.out.println("Server is now accepting connections");
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
                System.out.println("Handling a message");
                if (toHandle == null) {
                    System.out.println("Blocking queue doet niet wat ge denkt dat het doet!");
                }
                //handle message
                if (toHandle instanceof ConnectMessage) {
                    this.addUserName(((ConnectMessage) toHandle).getUsername());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //dit beter niet zomaar oproepen vanaf de connectionaccepterthread, maar wel op de "servereventthread" -> dus zo aan een messagequeue adden, of hoe dat ook alweer gaat.
    public void newConnection(Socket connection) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connection.getOutputStream());
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        new ListenToOneClientThread(connection, this).start();
        clients.add(connection);
        ooss.add(oos);
    }

    void addUserName(String username) {
        usernames.add(username);
        broadcast(new LobbyMessage(usernames));
    }

    private void broadcast(Message message) {
        System.out.println("broadcasting to " + clients.size());
        for (ObjectOutputStream oos : ooss) {
            try {
                oos.reset();
                oos.writeUnshared(message);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("broadcasting done");
    }
}
