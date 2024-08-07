package edu.school21.sockets.server;

import edu.school21.sockets.chatrooms.Chatroom;
import edu.school21.sockets.services.ChatRoomService;
import edu.school21.sockets.services.ChatRoomServiceImpl;
import edu.school21.sockets.services.UserService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    private UserService userService;

    private final BufferedReader bufferedReader;

    private final BufferedWriter bufferedWriter;

    private final Socket socket;

    private String userName;

    private List<Chatroom> chatrooms = new ArrayList<>();

    private final ChatRoomService<Chatroom> chatRoomService;

    public String chatName;

    public ServerThread(Socket socket, UserService userService, ChatRoomService<Chatroom> chatRoomService) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        start();
    }

    public void sendHelloMessage() throws IOException {
        String message = "Hello from Server!" + System.lineSeparator() +
                "1. signIn" + System.lineSeparator() +
                "2. signUp" + System.lineSeparator() +
                "3. exit" + System.lineSeparator();
        sendMessageToClient(message);
    }
    public void sendRoomsMessage() throws IOException {
        String message = "1.\tCreate room" + System.lineSeparator() +
                "2.\tChoose room" + System.lineSeparator() +
                "3.\tExit" + System.lineSeparator();
        sendMessageToClient(message);
    }
    @Override
    public void run() {
        try {
            sendHelloMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            boolean flag = false;
            do {
                String string = bufferedReader.readLine();
                int mode = Integer.parseInt(string);
                if (mode == 3) {
                    socket.close();
                    break;
                } else if (mode == 2) {
                    flag = changingFunction(2);
                } else if (mode == 1) {
                    flag = changingFunction(1);
                }
                if (!flag) {
                    String sendErrorPass = "Error with parameters\n";
                    sendMessageToClient(sendErrorPass);
                }
            }
            while (!flag);
        }
        catch (IOException | NoSuchFieldException | IllegalAccessException e) {
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
                    for (ServerThread serverThread : Server.serverThreads) {
                        if (serverThread.chatName.equals(chatName)) {
                            serverThread.sendMessageToClient(string + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean changingFunction(int mode) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.out.println("User choose " + mode);
        String sendEmail = "Enter name\n";
        sendMessageToClient(sendEmail);
        String name = bufferedReader.readLine();
        String sendPass = "Enter password\n";
        sendMessageToClient(sendPass);
        String password = bufferedReader.readLine();
        try {
            if (mode == 2) {
                if (!userService.signUp(name, password)) {
                    return false;
                }
            } else if (mode == 1) {
                if (!userService.signIn(name, password)) {
                    return false;
                }
            }
            userName = name;
            String successStr = "Successful " + userName + "\n";
            System.out.println(successStr);
            sendMessageToClient(successStr);
        }
        catch (SQLException e) {
            String errorStr = "Error with " + mode;
            sendMessageToClient(errorStr);
        }
        sendRoomsMessage();
        String readAnswerRoomString = bufferedReader.readLine();
        int readAnswerRoom = Integer.parseInt(readAnswerRoomString);
        if (readAnswerRoom == 1) {
            createChatRoom();
        }
        else if (readAnswerRoom == 2) {
            printChatRooms();
            chooseChatRoom();
        }
        return true;
    }

    public void createChatRoom(){
        try {
            String name = bufferedReader.readLine();
            Chatroom chatroom = new Chatroom(name);
            chatRoomService.save(chatroom);
            chatName = chatroom.getName();
            String string = chatName + " ---" + System.lineSeparator();
            sendMessageToClient(string);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void chooseChatRoom() {
        try {
            String string = bufferedReader.readLine();
            int number = Integer.parseInt(string);
            if (number == chatrooms.size() + 1) {
                socket.close();
                return;
            }
            if (number < 1 || number > chatrooms.size()) {
                return;
            }
            Chatroom chatroom = chatrooms.get(number - 1);
            chatName = chatroom.getName();
            string = chatName + " ---" + System.lineSeparator();
            sendMessageToClient(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printChatRooms() throws IOException {
        int cnt = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("Rooms:").append(System.lineSeparator());
        chatrooms = chatRoomService.findAll();
        for (Chatroom chatroom : chatrooms) {
            sb.append(cnt).append(".\t").append(chatroom.getName()).append(System.lineSeparator());
            cnt++;
        }
        sb.append(cnt).append(".\tExit").append(System.lineSeparator());
        sendMessageToClient(sb.toString());
    }

    public void sendMessageToClient(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.flush();
    }
}

