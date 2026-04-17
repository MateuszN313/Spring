package org.example.services;

import org.example.models.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VehicleService {
    private final VehicleRepository vehicleRepository;

    private final RentalRepository rentalRepository;

    private final VehicleValidator vehicleValidator;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository, VehicleValidator vehicleValidator) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.vehicleValidator = vehicleValidator;
    }

    public List<Vehicle> findAllVehicles(){
        return this.vehicleRepository.findAll();
    }

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

    public Vehicle findById(String vehicleId){
        return this.vehicleRepository.findById(vehicleId).get();
    }

    public boolean isVehicleRented(String vehicleId){
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    public Vehicle addVehicle(Vehicle vehicle){
        this.vehicleValidator.validate(vehicle);
        return this.vehicleRepository.save(vehicle);
    }

    public void removeVehicle(String vehicleId) {
        if(isVehicleRented(vehicleId))
            throw new IllegalArgumentException("This vehicle is rented");

        Optional<Vehicle> opt = this.vehicleRepository.findById(vehicleId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("No vehicle with such ID");
        }
        this.vehicleRepository.deleteById(vehicleId);
    }
}
