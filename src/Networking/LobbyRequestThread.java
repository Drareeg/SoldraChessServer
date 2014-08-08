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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Dries
 */
public class LobbyRequestThread extends Thread {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final Socket connection;

    public LobbyRequestThread(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            oos = new ObjectOutputStream(connection.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(connection.getInputStream());
            Object[] request = (Object[]) ois.readObject();
            Request requestMethod = (Request) request[0];
            int clientID = (int) request[1];
            System.out.println("Client connection id: " + clientID + ", with request: " + requestMethod);
            if (requestMethod.equals(Request.LOBBY_LIST_REQUEST)) {
                oos.writeObject("Iere my boy, AKA " + clientID + ", de lijste");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
