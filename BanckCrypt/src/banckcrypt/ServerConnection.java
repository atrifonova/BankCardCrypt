/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Toni
 */
public class ServerConnection implements Runnable {

    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input;
    Socket client;
    Server server;
    int connectionNumber;
    private User user;

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
