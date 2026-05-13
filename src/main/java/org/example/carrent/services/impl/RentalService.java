package org.example.carrent.services.impl;

import org.example.carrent.repositories.RentalRepository;
import org.example.carrent.repositories.UserRepository;
import org.example.carrent.services.RentalServiceInterface;
import org.example.carrent.models.Rental;
import org.example.carrent.models.User;
import org.example.carrent.models.Vehicle;
import org.example.carrent.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalService implements RentalServiceInterface {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findAllRentals(){
        return this.rentalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findUserRentals(String userId){
        List<Rental> all = this.rentalRepository.findAll();
        List<Rental> user = new ArrayList<>();
        for (Rental r : all){
            if(r.getUserId().equals(userId))
                user.add(r);
        }
        return user;
    }

    @Override
    public Rental rentVehicle(String userId, String vehicleId){
        if(findActiveRentalByUserId(userId).isPresent())
            throw new IllegalStateException("user is already renting");

        if(this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent())
            throw new IllegalStateException("this vehicle is already rented");

        Vehicle vehicle = this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("no vehicle with such ID"));

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("no user with such ID"));

        Rental rental = new Rental(null, vehicle, user, LocalDateTime.now().toString(), null);
        return this.rentalRepository.save(rental);
    }

    @Override
    public Rental returnVehicle(String userId){
        Optional<Rental> opt = findActiveRentalByUserId(userId);
        if(opt.isEmpty())
            throw new IllegalStateException("user doesn't have active rentals");

        Rental rental = opt.get();
        rental.setReturnDateTime(LocalDateTime.now().toString());
        return this.rentalRepository.save(rental);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean vehicleHasActiveRental(String vehicleId){
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasActiveRental(String userId){
        return this.rentalRepository.findAll().stream()
                .anyMatch(rental -> rental.getUserId().equals(userId) && rental.isActive());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rental> findActiveRentalByUserId(String userId){
        return this.rentalRepository.findAll().stream()
                .filter(rental -> rental.getUserId().equals(userId))
                .filter(Rental::isActive)
                .findFirst();
    }
}
