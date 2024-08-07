package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    private UsersRepository<User> usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository<User> usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean signUp(String email, String password) throws NoSuchFieldException, IllegalAccessException, SQLException {
        User user = new User(email, passwordEncoder.encode(password));
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
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
