import Client.Client;
import User.IUserRepository;
import User.UserRepository;

import Vehicle.IVehicleRepository;
import Vehicle.VehicleRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        IVehicleRepository vehicleRepository = new VehicleRepositoryImpl();
        IUserRepository userRepository = new UserRepository();
        Client client = new Client(vehicleRepository, userRepository);
        client.UI();
    }
}