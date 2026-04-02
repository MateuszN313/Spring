package org.example.services;

import org.example.models.User;
import org.example.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void showUsers(){
        List<User> copy = this.userRepository.findAll();
        for(User user : copy){
            System.out.println(user);
        }
    }

    public User deleteUser(String userId){
        Optional<User> opt = this.userRepository.findById(userId);
        if(opt.isEmpty())
            throw new IllegalArgumentException("No user with such ID");

        this.userRepository.deleteById(userId);
        return opt.get();
    }
}
