package org.example.services;

import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }

    public User deleteUser(String userId){
        Optional<User> opt = this.userRepository.findById(userId);
        if(opt.isEmpty())
            throw new IllegalArgumentException("No user with such ID");

        this.userRepository.deleteById(userId);
        return opt.get();
    }
}
