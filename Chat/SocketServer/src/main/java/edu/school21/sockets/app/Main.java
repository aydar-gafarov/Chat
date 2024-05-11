package edu.school21.sockets.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.services.UserService;
import edu.school21.sockets.services.UserServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, IllegalAccessException {
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
//        createTable(context);
        Server server = context.getBean("server", Server.class);
        UserService userService = context.getBean("userServiceImpl", UserService.class);
        server.runProgram(6666, userService);

  }
    public static void createTable(ApplicationContext context) throws SQLException {
        HikariDataSource hikariDataSource = context.getBean("hikariDataSource", HikariDataSource.class);
        try (Connection connection = hikariDataSource.getConnection()) {
            String createTableString = "DROP SCHEMA IF EXISTS day09 cascade;\n"
                    + "DROP TABLE IF EXISTS day09.users;\n"
                    + "DROP TABLE IF EXISTS day09.messages;\n"
                    + "CREATE SCHEMA IF NOT EXISTS day09;\n"
                    + "CREATE TABLE IF NOT EXISTS day09.users ( \n"
                    + "\tid SERIAL PRIMARY KEY NOT NULL,\n\t"
                    + "Email VARCHAR(30),\n\t"
                    + "Password VARCHAR(100)\n\t"
                    + ");"
                    + "CREATE TABLE IF NOT EXISTS day09.messages ( \n"
                    + "\ttime TIMESTAMP,\n\t"
                    + "sender VARCHAR(30),\n\t"
                    + "text VARCHAR(1000)\n\t"
                    + ");";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableString);
        }
    }
}
