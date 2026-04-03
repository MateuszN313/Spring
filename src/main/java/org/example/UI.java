package org.example;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Role;

import org.example.models.Vehicle;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.UserService;
import org.example.services.VehicleService;

import java.util.*;

public class UI {
    private final AuthService authService;
    private final RentalService rentalService;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final Scanner scanner;
    private User user;

    public UI(AuthService authService, RentalService rentalService, UserService userService, VehicleService vehicleService){
        this.authService = authService;
        this.rentalService = rentalService;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.scanner = new Scanner(System.in);
        this.user = null;
    }

    public void start() {
        while(true){
            System.out.println("1 - Log in\n" +
                    "2 - Register\n" +
                    "3 - Leave");
            String choice = this.scanner.nextLine();

            if(choice.equals("1")){
                if(login())
                    break;
            }else if(choice.equals("2")){
                if(register())
                    break;
            }else if(choice.equals("3")){
                return;
            }
        }

        String choice;
        do{
            System.out.println("\n1 - Show vehicles\n" +
                    "2 - Show your rentals\n" +
                    "3 - Rent vehicle\n" +
                    "4 - End rental\n" +
                    "5 - Leave");
            if(this.user.getRole() == Role.ADMIN){
                System.out.println("\nADMIN:\n" +
                        "6 - Show all vehicles\n" +
                        "7 - Add vehicle\n" +
                        "8 - Remove vehicle\n" +
                        "9 - Show users\n" +
                        "10 - Remove user\n" +
                        "11 - Show all rentals");
            }
            choice = this.scanner.nextLine();

            switch(choice){
                case "1" -> showNotRentedVehicles();
                case "2" -> showRentals();
                case "3" -> rent();
                case "4" -> ret();
            }

            if(this.user.getRole() == Role.ADMIN){
                switch(choice){
                    case "6" -> showAllVehicles();
                    case "7" -> addVehicle();
                    case "8" -> removeVehicle();
                    case "9" -> showUsers();
                    case "10" -> removeUser();
                    case "11" -> showAllRentals();
                }
            }
        }while(!choice.equals("5"));
    }

    private boolean register(){
        System.out.println("Login:");
        String login = this.scanner.nextLine();
        System.out.println("Password");
        String password = this.scanner.nextLine();

        try{
            this.user = this.authService.register(login, password);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        return this.user != null;
    }

    private boolean login(){
        System.out.println("Login:");
        String login = this.scanner.nextLine();
        System.out.println("Password:");
        String password = this.scanner.nextLine();

        try{
            this.user = this.authService.login(login, password);
        }catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        return this.user != null;
    }

    private void showNotRentedVehicles(){
        List<Vehicle> vehicles = this.vehicleService.getVehicles();
        for(Vehicle vehicle : vehicles){
            if(!rentalService.isVehicleRented(vehicle.getId()))
                System.out.println(vehicle);
        }
    }

    private void showAllVehicles(){
        List<Vehicle> vehicles = this.vehicleService.getVehicles();
        for(Vehicle vehicle : vehicles){
            System.out.println(vehicle);
        }
    }

    private void showUsers(){
        List<User> users = this.userService.getUsers();
        for(User user : users){
            System.out.println(user);
        }
    }

    private void showRentals(){
        List<Rental> rentals = this.rentalService.getRentals();
        for(Rental rental : rentals){
            if(rental.getUserId().equals(this.user.getId()) && rental.isActive())
                System.out.println(rental);
        }
    }

    private void showAllRentals(){
        List<Rental> rentals = this.rentalService.getRentals();
        for(Rental rental : rentals){
            System.out.println(rental);
        }
    }

    private void rent(){
        System.out.println("Vehicle ID:");
        String id = this.scanner.nextLine();
        if(!this.vehicleService.checkVehicle(id))
            System.err.println("No vehicle with such ID");

        Rental rental = null;
        try{
            rental = rentalService.startRental(id, this.user.getId());
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        if(rental != null)
            System.out.println("Started rental " + rental);
    }

    private void ret(){
        System.out.println("Rental ID:");
        String id = this.scanner.nextLine();
        Rental rental = null;
        try{
            rental = rentalService.endRental(id);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        if(rental != null)
            System.out.println("Ended rental " + rental);
    }

    private void addVehicle(){
        System.out.println("Category:");
        String category = this.scanner.nextLine();

        System.out.println("Brand:");
        String brand = this.scanner.nextLine();

        System.out.println("Model");
        String model = this.scanner.nextLine();

        System.out.println("Year of production:");
        int year;
        try{
            year = Integer.parseInt(this.scanner.nextLine());
        }catch (NumberFormatException e){
            System.err.println("Year must be a number");
            return;
        }

        System.out.println("Plate");
        String plate = this.scanner.nextLine();

        System.out.println("Price:");
        double price;
        try{
            price = Double.parseDouble(this.scanner.nextLine());
        }catch (NumberFormatException e){
            System.err.println("Price must be a number");
            return;
        }

        System.out.println("Attributes:");
        Map<String, Object> attributes = new HashMap<>();
        while(true){
            System.out.println("Name | \"end\" - leave");
            String name = this.scanner.nextLine();

            if(name.equals("end"))
                break;

            System.out.println("Value");
            Object value = null;
            if(this.scanner.hasNextLine())
                value = this.scanner.nextLine();
            else if(this.scanner.hasNextInt())
                value = Integer.parseInt(this.scanner.nextLine());
            else if(this.scanner.hasNextDouble())
                value = Double.parseDouble(this.scanner.nextLine());

            attributes.put(name, value);
        }

        Vehicle vehicle = this.vehicleService.addVehicle(category, brand, model, year, plate, price, attributes);
        System.out.println("Added vehicle " + vehicle);
    }

    private void removeVehicle(){
        System.out.println("Vehicle ID");
        String id = this.scanner.nextLine();

        if(this.rentalService.isVehicleRented(id)){
            System.err.println("Cannot remove vehicle because of active rental");
            return;
        }

        Vehicle vehicle = null;
        try{
            vehicle = this.vehicleService.deleteVehicle(id);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        if(vehicle != null)
            System.out.println("Deleted vehicle " + vehicle);
    }

    private void removeUser(){
        System.out.println("User ID:");
        String id = this.scanner.nextLine();

        if(this.rentalService.isUserRenting(id)){
            System.err.println("Cannot remove user because of active rental");
            return;
        }

        User user = null;
        try{
            user = this.userService.deleteUser(id);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        if(user != null)
            System.out.println("Deleted user " + user);
    }
}
