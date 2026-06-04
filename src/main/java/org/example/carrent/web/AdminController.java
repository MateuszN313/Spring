package org.example.carrent.web;

import lombok.RequiredArgsConstructor;
import org.example.carrent.models.Rental;
import org.example.carrent.models.User;
import org.example.carrent.models.Vehicle;
import org.example.carrent.services.RentalServiceInterface;
import org.example.carrent.services.UserServiceInterface;
import org.example.carrent.services.VehicleServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    VehicleServiceInterface vehicleService;
    RentalServiceInterface rentalService;
    UserServiceInterface userService;

    @PostMapping("/vehicles")
    public Vehicle vehicleCreate(@RequestBody Vehicle vehicle){
        return vehicleService.addVehicle(vehicle);
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> vehicleDelete(@PathVariable String id){
        this.vehicleService.removeVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rentals")
    public List<Rental> rentalsList(){
        return this.rentalService.findAllRentals();
    }

    @GetMapping("/users")
    public List<User> usersList(){
        return this.userService.findAllUsers();
    }
}
