package com.example.alex.jarvis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by alex on 04/12/2016.
 */

public class Sender extends Thread{

    private int port;
    private String ip;
    private Command command;

    public Sender(int port,String ip) {
        this.port = port;
        this.ip = ip;
    }

    public void sendCommand(Command command) {
        this.command = command;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        try {

            //Open socket
            Socket socket = new Socket(this.ip, this.port);

            //send command
            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            outToServer.writeObject(command);

            /* Pas besoin de reponse
            //Read response
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
            String response = inFromServer.readUTF();

            System.out.println("From server : " + response);*/

            //close socket
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
