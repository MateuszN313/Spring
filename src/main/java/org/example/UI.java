package org.example;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Role;

import org.example.models.Vehicle;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.UserService;
import org.example.services.VehicleService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

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
                    "2 - Rent vehicle\n" +
                    "3 - Return vehicle\n" +
                    "4 - Leave");
            if(this.user.getRole() == Role.ADMIN){
                System.out.println("\nADMIN:\n" +
                        "5 - Add vehicle\n" +
                        "6 - Remove vehicle\n" +
                        "7 - Show users\n" +
                        "8 - Remove user");
            }
            choice = this.scanner.nextLine();

            switch(choice){
                case "1" -> this.vehicleService.showVehicles();
                case "2" -> rent();
                case "3" -> ret();
            }

            if(this.user.getRole() == Role.ADMIN){
                switch(choice){
                    case "5" -> addVehicle();
                    case "6" -> removeVehicle();
                    case "7" -> this.userService.showUsers();
                    case "8" -> removeUser();
                }
            }
        }while(!choice.equals("4"));
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
            System.out.println("Started rental " + rental.getId());
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
            System.out.println("Ended rental " + rental.getId());
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
