package org.example.services;

import org.example.models.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public User register(String login, String password) {
        if(this.userRepository.findByLogin(login).isPresent())
            throw new IllegalArgumentException("This login is occupied");

        if(login == null || login.isBlank() || password == null || password.isBlank())
            throw new IllegalArgumentException("Login nd password cannot be blank");

        String passwordHash = hashPassword(password);
        User user = new User(null, login, passwordHash, Role.USER);
        return this.userRepository.save(user);
    }

    public User login(String login, String password) {
        Optional<User> opt = this.userRepository.findByLogin(login);
        if(opt.isEmpty())
            throw new IllegalArgumentException("No user with such login");

        User user = opt.get();
        if(!BCrypt.checkpw(password, user.getPasswordHash()))
            throw new IllegalArgumentException("Wrong password");

        return user;
    }
}
