package edu.school21.sockets.services;

import edu.school21.sockets.repositories.UsersRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    private UsersRepository<User> usersRepository;

    public UserServiceImpl(UsersRepository<User> usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean signUp(String email, String password) throws NoSuchFieldException, IllegalAccessException, SQLException {
        User user = new User(email, password);
        Optional<User>optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) return false;
        usersRepository.save(user);
        return true;
    }

    @Override
    public boolean signIn(String email, String password) {
        Optional<User> optionalUser= usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    @Override
    public void register(String text, Timestamp timestamp, String name) {
        usersRepository.registrationMessage(text, timestamp, name);
    }

}
