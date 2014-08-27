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

import Shared.Networking.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dries
 */
public class ListenToOneClientThread extends Thread {

    private ObjectInputStream ois;
    private final Socket connection;
    private Server server;

    //ipv server meegeven kunnen we ook messagequeue megeven.
    //voordeel van queue meegeven: dan kunnen we dat dynamisch veranderen om bv messages te accepten direct
    //in de queue van 1 game (als we later alle eventverwerking niet meer op 1 thread zouden doen, that is.)
    public ListenToOneClientThread(Socket connection, Server server) {
        this.connection = connection;
        this.server = server;
    }

    public void run() {
        try {
            ois = new ObjectInputStream(connection.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ListenToOneClientThread.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        //als de streams goed geinit zijn -> beginnen luisteren naar de ois
        int timesNoConnection = 0;
        while (timesNoConnection < 2) {
            try {
                Object incoming = ois.readObject();
                timesNoConnection = 0;
                if (incoming != null) {
                    server.addMessage(((Message) incoming).setSource(connection));
                }
            } catch (IOException ex) {
                //onder andere als de connectie is weggevallen (bv door client zijn pc die ontploft)
                System.out.println("no connection: " + timesNoConnection);
                timesNoConnection++;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListenToOneClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        server.clientDisconnected(connection);
    }
}
