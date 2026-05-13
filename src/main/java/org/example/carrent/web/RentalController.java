package org.example.carrent.web;

import org.example.carrent.models.Rental;
import org.example.carrent.services.RentalServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalServiceInterface rentalService;

    public RentalController(RentalServiceInterface rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<Rental> list(){
        return this.rentalService.findAllRentals();
    }

    @GetMapping("/{id}")
    public List<Rental> listByUserId(@PathVariable String id){
        return this.rentalService.findUserRentals(id);
    }

    @PostMapping("/users/{userId}/rent/{vehicleId}")
    public Rental rentVehicle(@PathVariable String userId, @PathVariable String vehicleId){
        return this.rentalService.rentVehicle(userId, vehicleId);
    }

    @PutMapping("/return/{userId}")
    public Rental returnVehicle(@PathVariable String userId){
        return this.rentalService.returnVehicle(userId);
    }
}
