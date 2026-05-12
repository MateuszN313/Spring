package org.example.carrent.repositories.impl.jpa;

import org.example.carrent.models.Rental;
import org.example.carrent.repositories.RentalJpaRepository;
import org.example.carrent.repositories.RentalRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class RentalRepositoryJpaAdapter implements RentalRepository {
    private RentalJpaRepository delegate;

    public RentalRepositoryJpaAdapter(RentalJpaRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Rental> findAll() {
        return null;
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return Optional.empty();
    }
}
