package org.example.carrent.services.impl;

import org.example.carrent.repositories.UserRepository;
import org.example.carrent.services.RentalServiceInterface;
import org.example.carrent.services.UserServiceInterface;
import org.example.carrent.models.Role;
import org.example.carrent.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final RentalServiceInterface rentalService;

    public UserService(UserRepository userRepository, RentalServiceInterface rentalService) {
        this.userRepository = userRepository;
        this.rentalService = rentalService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(String userId){
        return this.userRepository.findById(userId).get();
    }

    @Override
    public void deleteUser(String userId, String adminId){
        if(!findById(adminId).getRole().equals(Role.ADMIN))
            throw new IllegalStateException("Deleting user must be admin");

        if(rentalService.userHasActiveRental(userId))
            throw new IllegalStateException("This user is renting");

        Optional<User> opt = this.userRepository.findById(userId);
        if(opt.isEmpty())
            throw new IllegalArgumentException("No user with such ID");

        this.userRepository.deleteById(userId);
    }

    @Override
    public User findByLogin(String login) {
        return this.userRepository.findByLogin(login).get();
    }
}
