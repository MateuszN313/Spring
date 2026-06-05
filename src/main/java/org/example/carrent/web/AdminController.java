package org.example.carrent.web;

import lombok.RequiredArgsConstructor;
import org.example.carrent.models.Rental;
import org.example.carrent.models.User;
import org.example.carrent.services.RentalServiceInterface;
import org.example.carrent.services.UserServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    RentalServiceInterface rentalService;
    UserServiceInterface userService;

    @GetMapping("/rentals")
    public List<Rental> rentalsList(){
        return this.rentalService.findAllRentals();
    }

    @GetMapping("/users")
    public List<User> usersList(){
        return this.userService.findAllUsers();
    }
}
