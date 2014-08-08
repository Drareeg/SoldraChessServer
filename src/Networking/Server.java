/*
 * The MIT License
 *
 * Copyright 2014 Dries Weyme.
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

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dries
 */
public class Server {

    private ServerSocket providerSocket;
    private Socket connection = null;

    public Server() {
        try {
            providerSocket = new ServerSocket(12345, 10);
            System.out.println("Beep Boop: server is running");
        } catch (Exception e) {
            e.printStackTrace();
        }
        run();
    }

    private void run() {
        while (true) {
            try {
                connection = providerSocket.accept();
                System.out.println("Connection received from " + connection.getInetAddress().getHostName());
                new LobbyRequestThread(connection).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
