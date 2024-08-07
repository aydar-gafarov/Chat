package edu.school21.sockets.app;

import edu.school21.sockets.chatrooms.Chatroom;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.database.DataSource;
import edu.school21.sockets.server.Server;
import edu.school21.sockets.services.ChatRoomService;
import edu.school21.sockets.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        SocketsApplicationConfig config = context.getBean("socketsApplicationConfig", SocketsApplicationConfig.class);
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
//        dataSource.createTable();
        Server server = context.getBean("server", Server.class);
        UserService userService = context.getBean("userServiceImpl", UserService.class);
        ChatRoomService<Chatroom> chatRoomService = context.getBean("chatRoomServiceImpl", ChatRoomService.class);
        int port = config.getPort();
        server.runProgram(port, userService, chatRoomService);

  }
//    public static void createTable(ApplicationContext context) throws SQLException {
//        HikariDataSource hikariDataSource = context.getBean("hikariDataSource", HikariDataSource.class);
//        try (Connection connection = hikariDataSource.getConnection()) {
//            String createTableString = "DROP SCHEMA IF EXISTS day09 cascade;\n"
//                    + "DROP TABLE IF EXISTS day09.users;\n"
//                    + "CREATE SCHEMA IF NOT EXISTS day09;\n"
//                    + "CREATE TABLE IF NOT EXISTS day09.users ( \n"
//                    + "\tid SERIAL PRIMARY KEY NOT NULL,\n\t"
//                    + "Email VARCHAR(30),\n\t"
//                    + "Password VARCHAR(100)\n\t"
//                    + ");"
//                    + "CREATE TABLE IF NOT EXISTS day09.chatrooms ( \n"
//                    + "\tid SERIAL PRIMARY KEY NOT NULL,\n\t"
//                    + "Name VARCHAR(30) \n\t"
//                    + ");";
//
//            Statement statement = connection.createStatement();
//            statement.executeUpdate(createTableString);
//        }
//    }
}
