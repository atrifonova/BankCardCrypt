/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame {
    
    private JTextArea displayArea;
    private OutputStream output;
    private InputStream input;
    private ServerSocket server;
    private Socket connection;
    private int count;
    
    private final int WIDTH = 300;
    private final int HEIGHT = 150;

    public Server() {
        super ("Server");
        
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        setSize(WIDTH, HEIGHT);
        
        setVisible(true);
    }
    
    public void runServer() {
        
        displayMessage("Waiting for connection\n");
        
        try {
            server = new ServerSocket(12345);
            
            while(true) {
                connection = server.accept();
                
                displayMessage("Connection " + count + " received from: "
                        + connection.getInetAddress().getHostName());
                count++;
                
                ServerConnection serverConnection = new ServerConnection(connection, this, count);
                new Thread(serverConnection).start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
    
    private void closeConnection() {
        displayMessage("\nClose connection");
        
        try {
            
            output.close();
            input.close();
            connection.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                displayArea.append(messageToDisplay);
            }
        });
    }
    
}
