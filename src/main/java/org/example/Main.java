package org.example;

import org.example.Client.Client;
import org.example.User.IUserRepository;
import org.example.User.UserRepository;

import org.example.Vehicle.IVehicleRepository;
import org.example.Vehicle.VehicleRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        IVehicleRepository vehicleRepository = new VehicleRepositoryImpl();
        IUserRepository userRepository = new UserRepository();
        Client client = new Client(vehicleRepository, userRepository);
        client.UI();
    }
}