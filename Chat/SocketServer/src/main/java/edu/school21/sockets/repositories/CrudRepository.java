package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    public User findById(Long id) throws SQLException;
    public List<T> findAll();
    public User save(T entity) throws NoSuchFieldException, IllegalAccessException;
    public void update(T entity) throws NoSuchFieldException, IllegalAccessException;
    public void delete(Long id);
}
