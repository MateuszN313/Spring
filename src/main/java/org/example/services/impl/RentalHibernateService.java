package org.example.services.impl;

import org.example.models.Rental;
import org.example.repositories.impl.RentalHibernateRepository;
import org.example.repositories.impl.UserHibernateRepository;
import org.example.repositories.impl.VehicleHibernateRepository;
import org.example.services.RentalServiceInterface;

import java.util.List;
import java.util.Optional;

public class RentalHibernateService implements RentalServiceInterface {
    private final RentalHibernateRepository rentalRepository;
    private final VehicleHibernateRepository vehicleRepository;
    private final UserHibernateRepository userRepository;

    public RentalHibernateService(RentalHibernateRepository rentalRepository, VehicleHibernateRepository vehicleRepository, UserHibernateRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Rental rentVehicle(String userId, String vehicleId) {
        return null;
    }

    @Override
    public Rental returnVehicle(String userId) {
        return null;
    }

    @Override
    public Optional<Rental> findActiveRentalByUserId(String userId) {
        return Optional.empty();
    }

    @Override
    public List<Rental> findAllRentals() {
        return null;
    }

    @Override
    public List<Rental> findUserRentals(String userId) {
        return null;
    }

    @Override
    public boolean userHasActiveRental(String userId) {
        return false;
    }

    @Override
    public boolean vehicleHasActiveRental(String vehicleId) {
        return false;
    }
}
