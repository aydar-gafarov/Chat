package edu.school21.sockets.services;


import java.sql.SQLException;

public interface UserService {
    public boolean signUp(String email, String password) throws NoSuchFieldException, IllegalAccessException, SQLException;
    public boolean signIn(String email, String password);
}
