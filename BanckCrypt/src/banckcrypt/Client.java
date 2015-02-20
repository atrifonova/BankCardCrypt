/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Toni
 */
public class Client extends JFrame {

    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private String server; // host server for this application
    private Socket client; // socket to communicate with server
    private JButton btnDecrypt;
    private JButton btnEncrypt;
    private JTextField txtEnterField;
    private JLabel lblInput;
    private JLabel lblResult;
    private JTextField txtResult;
    private JPasswordField txtPassword;
    private JTextField txtUsername;
    private JButton btnLogin;
    private JLabel lblPassword;
    private JLabel lblUsername;
    private boolean isTerminated;

    public Client(String host) {
        super("Client");

        isTerminated = false;
        server = host; // set server to which this client connects

        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!isTerminated) {
                    sendData("terminate");
                }

            }
        });
    }

    private void initComponents() {

        txtEnterField = new JTextField();
        lblInput = new JLabel();
        txtResult = new JTextField();
        lblResult = new JLabel();
        btnEncrypt = new JButton();
        btnDecrypt = new JButton();

        lblUsername = new JLabel();
        lblPassword = new JLabel();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton();

        openLogin();

        setTextFieldEditable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        lblInput.setText("input:");

        lblResult.setText("result:");

        btnEncrypt.setText("Encrypt");
        btnEncrypt.addActionListener(
                new ActionListener() {
                    // send message to server
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        String inputText = txtEnterField.getText();
                        if (inputText.matches("^[0-9]{16}$")) {
                            sendData('e' + txtEnterField.getText());
                        } else {
                            JOptionPane.showMessageDialog(null, "Input must be 16-digit");
                        }
                        txtEnterField.setText("");
                    }
                }
        );

        btnDecrypt.setText("Decrypt");
        btnDecrypt.addActionListener(
                new ActionListener() {
                    // send message to server
                    public void actionPerformed(ActionEvent event) {
                        String inputText = txtEnterField.getText();
                        if (inputText.matches("^[0-9]{16}$")) {
                            sendData('d' + txtEnterField.getText());
                        } else {
                            JOptionPane.showMessageDialog(null, "Input must be 16-digit number");
                        }
                        txtEnterField.setText("");
                    }
                });

        lblUsername.setText("username:");

        lblPassword.setText("password:");

        btnLogin.setText("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData("l" + txtUsername.getText() + "%" + txtPassword.getText());
                txtUsername.setText("");
                txtPassword.setText("");
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblResult)
                                .addComponent(lblInput))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEnterField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(lblPassword))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(lblUsername)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(89, 89, 89)
                                        .addComponent(btnEncrypt)
                                        .addGap(44, 44, 44)
                                        .addComponent(btnDecrypt))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(160, 160, 160)
                                        .addComponent(btnLogin)))
                        .addContainerGap(127, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblUsername))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblPassword))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtEnterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblInput))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblResult)
                                .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addComponent(btnLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnEncrypt)
                                .addComponent(btnDecrypt))
                        .addGap(55, 55, 55))
        );

        setResizable(false);
        pack();
        setVisible(true);
    }

    // connect to server and process messages from server
    public void runClient() {
        try // connect to server, get streams, process connection
        {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        } catch (EOFException eofException) {
            displayMessage("\nClient terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection(); // close connection
        }
    }

    // connect to server
    private void connectToServer() throws IOException {
        // create Socket to make connection to server
        client = new Socket(InetAddress.getByName(server), 12345);
    }

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());
    }

    // process connection with server
    private void processConnection() throws IOException {
        do {
            try {
                message = (String) input.readObject(); // read new message

                if (message.startsWith("login")) {
                    if (message.contains("false")) {
                        JOptionPane.showMessageDialog(this, "Invalid username or password!!!");
                        txtPassword.setEditable(false);
                        txtUsername.setEditable(false);
                        btnLogin.setEnabled(false);
                        isTerminated = true;
                        sendData("terminate");

                    } else {
                        closeLogin();
                        setTextFieldEditable(true);

                    }
                } else if (message.equals("ddenied")) {
                    JOptionPane.showMessageDialog(this, "You don't have permission to decrypt!!!");
                }

                if (message.equals("edenied")) {
                    JOptionPane.showMessageDialog(this, "You don't have permission to encrypt!!!");
                }

                if (message.matches("^[0-9]+$")) {
                    displayMessage(message);
                }

            } catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            }

        } while (!message.equals("terminate"));
    }

    // close streams and socket
    private void closeConnection() {
        setTextFieldEditable(false); // disable enterField

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // send message to server
    private void sendData(String message) {
        try {
            output.writeObject(message);
            output.flush(); // flush data to output
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error writing object");
        }
    }

    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates result
                    {
                        txtResult.setText(messageToDisplay);
                    }
                });
    }

    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        txtEnterField.setEditable(editable);
                        txtResult.setEditable(editable);
                        btnEncrypt.setEnabled(editable);
                        btnDecrypt.setEnabled(editable);
                    }
                });
    }

    private void closeLogin() {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        lblUsername.setVisible(false);
                        lblPassword.setVisible(false);
                        txtPassword.setVisible(false);
                        txtUsername.setVisible(false);
                        btnLogin.setVisible(false);

                        txtEnterField.setVisible(true);
                        lblInput.setVisible(true);
                        txtResult.setVisible(true);
                        lblResult.setVisible(true);
                        btnEncrypt.setVisible(true);
                        btnDecrypt.setVisible(true);
                    }
                });

    }

    private void openLogin() {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        txtEnterField.setVisible(false);
                        lblInput.setVisible(false);
                        txtResult.setVisible(false);
                        lblResult.setVisible(false);
                        btnEncrypt.setVisible(false);
                        btnDecrypt.setVisible(false);

                        lblUsername.setVisible(true);
                        lblPassword.setVisible(true);
                        txtPassword.setVisible(true);
                        txtUsername.setVisible(true);
                        btnLogin.setVisible(true);
                    }
                });
    }

}
