package org.example;

import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleCategoryConfigRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.*;
import org.example.services.impl.AuthSimpleService;
import org.example.services.impl.RentalSimpleService;
import org.example.services.impl.UserSimpleService;
import org.example.services.VehicleCategoryConfigService;
import org.example.services.impl.VehicleSimpleService;
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
            rentalRepository = new RentalJdbcRepository(vehicleRepository, userRepository);
        }else{
            throw new RuntimeException("Wrong repository type given");
        }

        AuthSimpleService authService = new AuthSimpleService(userRepository);
        VehicleCategoryConfigService categoryConfigService = new VehicleCategoryConfigService(categoryConfigRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(categoryConfigService);
        VehicleSimpleService vehicleService = new VehicleSimpleService(vehicleRepository, rentalRepository, vehicleValidator);
        RentalSimpleService rentalService = new RentalSimpleService(rentalRepository, vehicleRepository, userRepository);
        UserSimpleService userService = new UserSimpleService(userRepository, rentalService);

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