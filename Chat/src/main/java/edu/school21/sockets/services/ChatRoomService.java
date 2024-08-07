package edu.school21.sockets.services;

import java.util.List;

public interface ChatRoomService<T> {

    T findById(Long id);

    List<T> findAll();

    T save(T entity);
}
