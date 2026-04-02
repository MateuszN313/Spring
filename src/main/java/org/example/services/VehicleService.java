package org.example.services;

import org.example.models.Vehicle;
import org.example.repositories.VehicleRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void showVehicles(){
        List<Vehicle> copy = this.vehicleRepository.findAll();
        for(Vehicle vehicle : copy){
            System.out.println(vehicle);
        }
    }

    public boolean checkVehicle(String vehicleId){
        return this.vehicleRepository.findById(vehicleId).isPresent();
    }

    public Vehicle addVehicle(String category, String brand, String model, int year, String plate, double price, Map<String, Object> attributes){
        Vehicle vehicle = new Vehicle(null, category, brand, model, year, plate, price, attributes);
        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle deleteVehicle(String vehicleId) {
        Optional<Vehicle> opt = this.vehicleRepository.findById(vehicleId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("No vehicle with such ID");
        }
        this.vehicleRepository.deleteById(vehicleId);
        return opt.get();
    }
}
