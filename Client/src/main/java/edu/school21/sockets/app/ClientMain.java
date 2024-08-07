package edu.school21.sockets.app;

import edu.school21.sockets.config.ClientApplicationConfig;
import edu.school21.sockets.logic.Logic;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.Socket;
public class ClientMain {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientApplicationConfig.class);
        Socket socket = context.getBean("socket", Socket.class);
        Logic logic = context.getBean("logic", Logic.class);
        if (socket.isConnected()) {
            logic.startProgram(socket);
        }
        else {
            socket.close();
            throw new RuntimeException("Error with socket");
        }
    }

}
