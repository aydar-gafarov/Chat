package edu.school21.sockets.server;

import edu.school21.sockets.chatrooms.Chatroom;
import edu.school21.sockets.services.ChatRoomService;
import edu.school21.sockets.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
@Component
public class Server {
    public static List<ServerThread> serverThreads = new ArrayList<>();

    public void runProgram (int port, UserService userService, ChatRoomService<Chatroom> chatRoomService) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    serverThreads.add(new ServerThread(clientSocket, userService, chatRoomService));
                } catch (IOException e) {
                    clientSocket.close();
                    break;
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error with create server");
        }
    }
}
