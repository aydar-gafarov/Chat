package edu.school21.sockets.server;

import edu.school21.sockets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class Server {
    public static List<ServerThread> serverThreads = new ArrayList<>();
    public void runProgram (int port, UserService userService) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    serverThreads.add(new ServerThread(clientSocket, userService));
                } catch (IOException e) {
                    clientSocket.close();
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error with create server");
        }
    }
}
