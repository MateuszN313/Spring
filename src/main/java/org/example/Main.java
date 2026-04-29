package org.example;

import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleCategoryConfigRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.*;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.UserService;
import org.example.services.VehicleCategoryConfigService;
import org.example.services.VehicleService;
import org.example.services.VehicleValidator;

public class Main {
    public static void main(String[] args) {
        VehicleCategoryConfigRepository categoryConfigRepository = new VehicleCategoryConfigJsonRepository();

        VehicleRepository vehicleRepository;
        UserRepository userRepository;
        RentalRepository rentalRepository;

        if(args.length == 0){
            throw new RuntimeException("No repository type given");
        }
        if(args[0].equals("json")){
            vehicleRepository = new VehicleJsonRepository();
            userRepository = new UserJsonRepository();
            rentalRepository = new RentalJsonRepository();
        }else if(args[0].equals("jdbc")){
            vehicleRepository = new VehicleJdbcRepository();
            userRepository = new UserJdbcRepository();
            rentalRepository = new RentalJdbcRepository();
        }else{
            throw new RuntimeException("No repository type given");
        }

        AuthService authService = new AuthService(userRepository);
        VehicleCategoryConfigService categoryConfigService = new VehicleCategoryConfigService(categoryConfigRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(categoryConfigService);
        VehicleService vehicleService = new VehicleService(vehicleRepository, rentalRepository, vehicleValidator);
        RentalService rentalService = new RentalService(rentalRepository, vehicleRepository);
        UserService userService = new UserService(userRepository, rentalService);

        UI ui = new UI(
                authService,
                vehicleService,
                rentalService,
                userService,
                categoryConfigService
        );

        ui.start();
    }
}