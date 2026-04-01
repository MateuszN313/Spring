package org.example.services;

import org.example.models.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public Optional<User> register(String login, String passwordHash) {
        if(this.userRepository.findByLogin(login).isPresent())
            return Optional.empty();

        User user = new User(UUID.randomUUID().toString(), login, passwordHash, Role.USER);
        this.userRepository.save(user);
        return Optional.of(user);
    }

    public Optional<User> login(String login, String password) {
        String passwordHash = hashPassword(password);
        Optional<User> opt = this.userRepository.findByLogin(login);
        return opt.filter(user -> user.getPasswordHash().equals(passwordHash));
    }
}
