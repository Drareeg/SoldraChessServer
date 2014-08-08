/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
