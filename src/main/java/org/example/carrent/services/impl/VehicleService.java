package org.example.carrent.services.impl;

import org.example.carrent.repositories.RentalRepository;
import org.example.carrent.services.VehicleServiceInterface;
import org.example.carrent.models.Vehicle;
import org.example.carrent.repositories.VehicleRepository;
import org.example.carrent.services.VehicleValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleService implements VehicleServiceInterface {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final VehicleValidator vehicleValidator;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository, VehicleValidator vehicleValidator) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.vehicleValidator = vehicleValidator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAllVehicles(){
        return this.vehicleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAvailableVehicles(){
        List<Vehicle> all = this.vehicleRepository.findAll();
        List<Vehicle> available = new ArrayList<>();
        for(Vehicle v : all){
            if(!isVehicleRented(v.getId())){
                available.add(v);
            }
        }
        return available;
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle findById(String vehicleId){
        return this.vehicleRepository.findById(vehicleId).get();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isVehicleRented(String vehicleId){
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle){
        this.vehicleValidator.validate(vehicle);
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        if(isVehicleRented(vehicleId))
            throw new IllegalStateException("This vehicle is rented");

        Optional<Vehicle> opt = this.vehicleRepository.findById(vehicleId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("No vehicle with such ID");
        }
        this.vehicleRepository.deleteById(vehicleId);
    }
}
