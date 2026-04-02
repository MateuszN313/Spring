package org.example;

import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.UserJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.UserService;
import org.example.services.VehicleService;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepository = new VehicleJsonRepository();
        UserRepository userRepository = new UserJsonRepository();
        RentalRepository rentalRepository = new RentalJsonRepository();

        AuthService authService = new AuthService(userRepository);
        RentalService rentalService = new RentalService(rentalRepository);
        UserService userService = new UserService(userRepository);
        VehicleService vehicleService = new VehicleService(vehicleRepository);

        UI client = new UI(authService, rentalService, userService, vehicleService);
        client.start();
    }
}