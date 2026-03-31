package org.example;

import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.UserJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepository = new VehicleJsonRepository();
        UserRepository userRepository = new UserJsonRepository();
        UI client = new UI(vehicleRepository, userRepository);
        client.start();
    }
}