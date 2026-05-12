package org.example.carrent.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import org.example.carrent.db.JsonFileStorage;
import org.example.carrent.models.Vehicle;
import org.example.carrent.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("json")
public class VehicleJsonRepository implements VehicleRepository {
    private final List<Vehicle> vehicles;
    private final JsonFileStorage<Vehicle> storage;

    public VehicleJsonRepository(@Value("${carrent.json.vehicles-file}") String filename) {
        this.storage = new JsonFileStorage<>(filename, new TypeToken<List<Vehicle>>() {}.getType());
        this.vehicles = new ArrayList<>(this.storage.load());
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> copy = new ArrayList<>();
        for(Vehicle vehicle : this.vehicles){
            copy.add(vehicle.copy());
        }
        return copy;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return this.vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .map(Vehicle::copy);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if(vehicle == null)
            throw new IllegalArgumentException("vehicle cannot be null");

        Vehicle toSave = vehicle.copy();
        if(toSave.getId() == null || toSave.getId().isBlank())
            toSave.setId(UUID.randomUUID().toString());
        else
            this.vehicles.removeIf(v -> v.getId().equals(toSave.getId()));

        this.vehicles.add(toSave);
        this.storage.save(this.vehicles);
        return toSave.copy();
    }

    @Override
    public void deleteById(String id) {
        this.vehicles.removeIf(vehicle ->vehicle.getId().equals(id));
        this.storage.save(vehicles);
    }
}
