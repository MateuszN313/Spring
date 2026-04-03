package org.example.services;

import org.example.models.Rental;
import org.example.repositories.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> getRentals(){
        return this.rentalRepository.findAll();
    }

    public Rental startRental(String vehicleId, String userId){
        if(this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent())
            throw new IllegalArgumentException("this vehicle is already rented");

        Rental rental = new Rental(null, vehicleId, userId, LocalDateTime.now().toString(), null);
        return this.rentalRepository.save(rental);
    }

    public Rental endRental(String rentalId){
        Optional<Rental> opt = this.rentalRepository.findById(rentalId);
        if(opt.isEmpty())
            throw new IllegalArgumentException("No rental with such ID");

        Rental rental = opt.get();
        rental.setReturnDateTime(LocalDateTime.now().toString());
        return this.rentalRepository.save(rental);
    }

    public boolean isVehicleRented(String vehicleId){
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    public boolean isUserRenting(String userId){
        return this.rentalRepository.findAll().stream()
                .filter(rental -> rental.getUserId().equals(userId))
                .filter(Rental::isActive)
                .findFirst()
                .map(Rental::copy)
                .isPresent();
    }
}
