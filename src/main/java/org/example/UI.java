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
            System.out.println("\nWybierz akcję:\n" +
                    "1 - Wyświetl pojazdy\n" +
                    "2 - Wypożycz pojazd\n" +
                    "3 - Zwróć pojazd\n" +
                    "4 - Wyjdź");
            if(this.user.getRole() == Role.ADMIN){
                System.out.println("\nADMIN:\n" +
                        "5 - Dodaj pojazd\n" +
                        "6 - Usuń pojazd\n" +
                        "7 - Wyświetl użytkowników\n" +
                        "8 - Usuń użytkownika");
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

        this.user = this.authService.register(login, password);
        return this.user != null;
    }

    private boolean login(){
        System.out.println("Login:");
        String login = this.scanner.nextLine();
        System.out.println("Password:");
        String password = this.scanner.nextLine();

        Optional<User> opt = this.authService.login(login, password);
        if(opt.isPresent()){
            this.user = opt.get();
            return true;
        }
        System.out.println("wrong login or password");
        return false;
    }

    private void rent(){
        System.out.println("Vehicle ID:");
        String id = this.scanner.nextLine();
        if(!this.vehicleService.checkVehicle(id))
            System.err.println("No vehicle with such ID");

        Rental rental = rentalService.startRental(id, this.user.getId());
        System.out.println("Started rental " + rental.getId());
    }

    private void ret(){
        System.out.println("Rental ID:");
        String id = this.scanner.nextLine();
        Rental rental = rentalService.endRental(id);
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
            System.out.println("Year must be a number");
            return;
        }

        System.out.println("Plate");
        String plate = this.scanner.nextLine();

        System.out.println("Price:");
        double price;
        try{
            price = Double.parseDouble(this.scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println("Price must be a number");
            return;
        }

        System.out.println("Attributes:");
        Map<String, Object> attributes = new HashMap<>();
        while(true){
            System.out.println("Name | end - leave");
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

        this.vehicleService.addVehicle(category, brand, model, year, plate, price, attributes);
    }

    private void removeVehicle(){
        System.out.println("Vehicle ID");
        String id = this.scanner.nextLine();

        Vehicle vehicle = this.vehicleService.deleteVehicle(id);
        System.out.println("Deleted vehicle " + vehicle);
    }

    private void removeUser(){
        System.out.println("User ID:");
        String id = this.scanner.nextLine();

        User user = this.userService.deleteUser(id);
        System.out.println("Deleted user " + user);
    }
}
