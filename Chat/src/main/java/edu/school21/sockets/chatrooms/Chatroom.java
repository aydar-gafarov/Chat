package edu.school21.sockets.chatrooms;

import edu.school21.sockets.server.ServerThread;

import java.util.ArrayList;
import java.util.List;

public class Chatroom {
    private String name;

    private List<ServerThread> users = new ArrayList<>();
    public void addUsers(ServerThread serverThread) {
        users.add(serverThread);
    }

    public void removeUsers(ServerThread serverThread) {
        users.remove(serverThread);
    }

    public List<ServerThread> getUsers() {
        return users;
    }

    public Chatroom() {
    }

    public Chatroom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "name='" + name + '\'' +
                '}';
    }
}
