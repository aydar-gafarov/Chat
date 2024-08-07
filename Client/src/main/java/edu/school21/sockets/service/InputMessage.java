package edu.school21.sockets.service;

import edu.school21.sockets.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.Socket;


public class InputMessage extends Thread {

    @Autowired
    private final Socket socket;

    @Autowired
    private final Logic logic;

    @Autowired
    public InputMessage(Socket socket, Logic logic) {
        this.socket = socket;
        this.logic = logic;
    }

    @Override
    public void run() {
        while(!logic.isFlagStop()) {
            try {
                String string = logic.getIn().readLine();
                if (string != null) {
                    if (string.equals("exit")) {
                        logic.close(socket);
                        break;
                    }
                    System.out.println(string + "\n");
                }
            } catch (IOException e) {
                System.out.println("Socket closed");
            }
        }
    }
}