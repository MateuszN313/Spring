package org.example.carrent.repositories.impl.jpa;

import org.example.carrent.models.Vehicle;
import org.example.carrent.repositories.VehicleJpaRepository;
import org.example.carrent.repositories.VehicleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class VehicleRepositoryJpaAdapter implements VehicleRepository {
    private final VehicleJpaRepository delegate;

    public VehicleRepositoryJpaAdapter(VehicleJpaRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Vehicle> findAll() {
        return null;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
