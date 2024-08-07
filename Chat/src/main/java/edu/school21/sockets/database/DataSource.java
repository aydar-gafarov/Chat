package edu.school21.sockets.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DataSource {

    @Autowired
    private HikariDataSource hikariDataSource;

    public void createTable() {
        try (Connection connection = hikariDataSource.getConnection()) {
            String createTableString = "DROP SCHEMA IF EXISTS day09 cascade;\n"
                    + "DROP TABLE IF EXISTS day09.users;\n"
                    + "CREATE SCHEMA IF NOT EXISTS day09;\n"
                    + "CREATE TABLE IF NOT EXISTS day09.users ( \n"
                    + "\tid SERIAL PRIMARY KEY NOT NULL,\n\t"
                    + "Email VARCHAR(30),\n\t"
                    + "Password VARCHAR(100)\n\t"
                    + ");"
                    + "CREATE TABLE IF NOT EXISTS day09.chatrooms ( \n"
                    + "\tid SERIAL PRIMARY KEY NOT NULL,\n\t"
                    + "Name VARCHAR(30) \n\t"
                    + ");";

            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableString);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
