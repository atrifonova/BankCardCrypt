/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ServerConnection(Socket client, Server server, int connectionNumber) {
        this.client = client;
        this.server = server;
        this.connectionNumber = connectionNumber;

        getStreams();
    }

    private void getStreams() {
        try {
            // set up output stream for objects
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush(); // flush output buffer to send header information

            // set up input stream for objects
            input = new ObjectInputStream(client.getInputStream());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } // end method getStreams

    @Override
    public void run() {
        String message = null;
        String messageToServer = null;

        while (true) {
            try {

                message = (String) input.readObject(); // display message
                if (message.equals("terminate")) {
                    break;
                }

                if (message.charAt(0) == 'e') {
                    if (user.getCanEncrypt()) {
                        messageToServer = encrypt(message.substring(1));
                    } else {
                        messageToServer = "edenied";
                    }
                } else if (message.charAt(0) == 'd') {
                    if (user.getCanDecrypt()) {
                        messageToServer = decrypt(message.substring(1));
                    } else {
                        messageToServer = "ddenied";
                    }
                } else if (message.charAt(0) == 'l') {
                    boolean success = authenticate(message.substring(1));
                    messageToServer = "login" + success;
                }

                server.displayMessage("\n" + "message:" + message + "\n"); // display message

                output.writeObject(messageToServer);
                output.flush(); // flush output to client
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }

        closeConnection();
    }

    private void closeConnection() {
        server.displayMessage("\nTerminating connection\n");
        //setTextFieldEditable( false ); // disable enterField

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method closeConnection

    private String encrypt(String input) {
        int SHIFT_LENGTH = 5;
        char[] arr = input.toCharArray();
        for (int i = 0; i < input.length(); i++) {
            arr[i] = (char) ((arr[i] + SHIFT_LENGTH - '0') % 10 + '0');
        }
        return new String(arr);
    }

    private String decrypt(String input) {
        int SHIFT_LENGTH = 5;
        char[] arr = input.toCharArray();
        for (int i = 0; i < input.length(); i++) {
            arr[i] = (char) (arr[i] - SHIFT_LENGTH);
            if (arr[i] < '0') {
                arr[i] = (char) (arr[i] + 10);
            }
        }

        return new String(arr);
    }

    private boolean authenticate(String message) {

        this.user = null;

        String[] str = message.split("%", 2);
        server.displayMessage("Username: " + str[0]);
        server.displayMessage("Password: " + str[1]);
        String username = str[0];
        String password = str[1];

        XStream xstream = new XStream();
        xstream.alias("users", ListOfUsers.class);
        xstream.alias("user", User.class);
        try {
            FileInputStream fileInputStream = new FileInputStream("users.xml");

            ListOfUsers users = (ListOfUsers) xstream.fromXML(fileInputStream);
            System.out.println(users);

            for (User user : users.getList()) {
                if (user.getUserName().equals(username)
                        && user.getPassword().equals(password)) {
                    this.user = user;
                    return true;
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
