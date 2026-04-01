package org.example;

import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.UserJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.services.AuthService;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepository = new VehicleJsonRepository();
        UserRepository userRepository = new UserJsonRepository();
        RentalRepository rentalRepository = new RentalJsonRepository();
        AuthService authService = new AuthService(userRepository);

        UI client = new UI(vehicleRepository, userRepository, rentalRepository, authService);
        client.start();
    }
}