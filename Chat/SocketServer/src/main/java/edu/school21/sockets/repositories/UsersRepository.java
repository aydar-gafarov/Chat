package edu.school21.sockets.repositories;

import java.sql.Timestamp;
import java.util.Optional;

public interface UsersRepository<T> extends CrudRepository<T>{
    public Optional<T> findByEmail(String email);
    public void registrationMessage(String text, Timestamp timestamp, String name);
    public void resetSequence();
}
