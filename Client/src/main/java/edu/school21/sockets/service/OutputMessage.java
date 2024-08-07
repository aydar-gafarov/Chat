package edu.school21.sockets.service;

import edu.school21.sockets.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class OutputMessage extends Thread {

    @Autowired
    private final Socket socket;

    @Autowired
    private final Logic logic;

    @Autowired
    public OutputMessage(Socket socket, Logic logic) {
        this.socket = socket;
        this.logic = logic;
    }

    @Override
    public void run() {
        while(!logic.isFlagStop()) {
            try {
                String string = logic.getReader().readLine();
                if (string != null) {
                    if (string.equals("exit")) {
                        logic.close(socket);
                        break;
                    }
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.now();
                    String date = dateTimeFormatter.format(dateTime);
                    logic.getOut().write(date + " " + logic.getNickName() + ": " + string + "\n");
                    logic.getOut().flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}