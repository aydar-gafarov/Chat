package edu.school21.sockets.server;

import edu.school21.sockets.services.UserService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ServerThread extends Thread {
    private UserService userService;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    private String userName;

    public ServerThread(Socket socket, UserService userService) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.userService = userService;
        start();
    }
    @Override
    public void run() {
        String[] sendPass = new String[4];
        sendPass[0] = "Hello from Server!\n";
        sendPass[1] = "1. signIn\n";
        sendPass[2] = "2. signUp\n";
        sendPass[3] = "3. exit\n";
        try {
            for (int i = 0; i < 4; i++) {
                bufferedWriter.write(sendPass[i]);
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            boolean flag = false;
            do {
                String string = bufferedReader.readLine();
                if (string.equals("3")) {
                    socket.close();
                } else if (string.equals("2")) {
                    flag = changingFunction(string);
                } else if (string.equals("1")) {
                    flag = changingFunction(string);
                }
                if (!flag) {
                    String sendErrorPass = "Error with parameters\n";
                    bufferedWriter.write(sendErrorPass);
                    bufferedWriter.flush();
                }
            }
            while (!flag);
        }
        catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String[] roomMenu = new String[3];
        roomMenu[0] = "1.	Create room\n";
        roomMenu[1] = "2.	Choose room\n";
        roomMenu[2] = "3.	Exit\n";
        try {
            for (int i = 0; i < 3; i++) {
                bufferedWriter.write(roomMenu[i]);
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            boolean flag = false;
            do {
                String string = bufferedReader.readLine();
                if (string.equals("3")) {
                    socket.close();
                } else if (string.equals("2")) {
                    flag = changingFunction(string);
                } else if (string.equals("1")) {
                    flag = changingFunction(string);
                }
                if (!flag) {
                    String sendErrorPass = "Error with parameters\n";
                    bufferedWriter.write(sendErrorPass);
                    bufferedWriter.flush();
                }
            }
            while (!flag);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                bufferedWriter.flush();
                String string = bufferedReader.readLine();
                if (string != null)
                {
                    if (string.equals("exit")) {
                        socket.close();
                        bufferedReader.close();
                        bufferedWriter.close();
                        break;
                    }
                    String[] arrStr = string.split(" ");
                    String forTimestamp = arrStr[0] + " " + arrStr[1];
                    Timestamp timestamp = Timestamp.valueOf(forTimestamp);
                    String sender = arrStr[2].substring(0, arrStr[2].length() - 1);
                    String text = arrStr[3];
                    userService.register(text, timestamp, sender);
                    for (ServerThread serverThread : Server.serverThreads) {
                        serverThread.bufferedWriter.write(string + "\n");
                        serverThread.bufferedWriter.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean changingFunction(String mode) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.out.println("User choose " + mode);
        String sendEmail = "Enter name\n";
        bufferedWriter.write(sendEmail);
        bufferedWriter.flush();
        String name = bufferedReader.readLine();
        String sendPass = "Enter password\n";
        bufferedWriter.write(sendPass);
        bufferedWriter.flush();
        String password = bufferedReader.readLine();
        try {
            if (mode.equals("signUp")) {
                if (!userService.signUp(name, password)) {
                    return false;
                }
            } else if (mode.equals("signIn")) {
                if (!userService.signIn(name, password)) {
                    return false;
                }
            }
            userName = name;
            String successStr = "Successful" + userName + "\n";
            System.out.println(successStr);
            bufferedWriter.write(successStr);
            bufferedWriter.flush();
        }
        catch (SQLException e) {
            String errorStr = "Error with " + mode;
            bufferedWriter.write(errorStr);
            bufferedWriter.flush();
        }
        return true;
    }
}

