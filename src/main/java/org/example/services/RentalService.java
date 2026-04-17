package org.example.services;

import org.example.models.Rental;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalService {
    private final RentalRepository rentalRepository;

    private final VehicleRepository vehicleRepository;

    public RentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository) {
        this.rentalRepository = rentalRepository;
        this. vehicleRepository = vehicleRepository;
    }

    public List<Rental> findAllRentals(){
        return this.rentalRepository.findAll();
    }

    public List<Rental> findUserRentals(String userId){
        List<Rental> all = this.rentalRepository.findAll();
        List<Rental> user = new ArrayList<>();
        for (Rental r : all){
            if(r.getUserId().equals(userId))
                user.add(r);
        }
        return user;
    }

    public void rentVehicle(String userId, String vehicleId){
        if(findActiveRentalByUserId(userId).isPresent())
            throw new IllegalArgumentException("user is already renting");

        if(this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent())
            throw new IllegalArgumentException("this vehicle is already rented");

        Rental rental = new Rental(null, vehicleId, userId, LocalDateTime.now().toString(), null);
        this.rentalRepository.save(rental);
    }

    public void returnVehicle(String userId){
        Optional<Rental> opt = findActiveRentalByUserId(userId);
        if(opt.isEmpty())
            throw new IllegalArgumentException("user doesn't have rentals");

        Rental rental = opt.get();
        rental.setReturnDateTime(LocalDateTime.now().toString());
        this.rentalRepository.save(rental);
    }

    public boolean vehicleHasActiveRental(String vehicleId){
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    public boolean userHasActiveRental(String userId){
        return this.rentalRepository.findAll().stream()
                .anyMatch(rental -> rental.getUserId().equals(userId) && rental.isActive());
    }

    public Optional<Rental> findActiveRentalByUserId(String userId){
        return this.rentalRepository.findAll().stream()
                .filter(rental -> rental.getUserId().equals(userId))
                .filter(Rental::isActive)
                .findFirst();
    }
}
