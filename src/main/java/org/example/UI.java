package org.example;

import org.example.models.User;
import org.example.models.Role;

import org.example.models.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.services.AuthService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.example.models.Role.USER;

public class UI {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final AuthService authService;
    private final Scanner scanner;
    private User user;

    public UI(VehicleRepository vehicleRepository, UserRepository userRepository, RentalRepository rentalRepository, AuthService authService){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        if(!begin())
            return;

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
                case "1" -> showVehicles();
                case "2" -> rent();
                case "3" -> ret();
            }

            if(this.user.getRole() == Role.ADMIN){
                switch(choice){
                    case "5" -> addVehicle();
                    case "6" -> removeVehicle();
                    case "7" -> showUsers();
                    case "8" -> removeUser();
                }
            }
        }while(!choice.equals("4"));
    }

    private boolean begin(){
        while(true){
            System.out.println("1 - Zaloguj się\n" +
                    "2 - Zarejestruj się\n" +
                    "3 - Wyjdź");
            String choice = this.scanner.nextLine();

            if(choice.equals("1")){
                if(login())
                    return true;
            }else if(choice.equals("2")){
                if(register())
                    return true;
            }else if(choice.equals("3")){
                return false;
            }
        }
    }
    private boolean register(){
        System.out.println("Podaj login");
        String login = this.scanner.nextLine();
        System.out.println("Podaj hasło");
        String passwordHash = this.authService.hashPassword(this.scanner.nextLine());

        Optional<User> opt = this.authService.register(login, passwordHash);
        if(opt.isPresent()){
            this.user = opt.get();
            return true;
        }
        System.out.println("Nie udało się zarejestrować");
        return false;
    }

    private boolean login(){
        System.out.println("Podaj login");
        String login = this.scanner.nextLine();
        System.out.println("Podaj hasło");
        String password = this.scanner.nextLine();

        Optional<User> opt = this.authService.login(login, password);
        if(opt.isPresent()){
            this.user = opt.get();
            return true;
        }
        System.out.println("nie udało się zalogować");
        return false;
    }

    private void showVehicles(){
        List<Vehicle> copy = this.vehicleRepository.findAll();
        for(Vehicle v : copy){
            System.out.println(v.toString());
        }
    }

    private void rent(){
        System.out.println("Podaj id");
        String id = this.scanner.nextLine();
        this.vehicleRepository.rentVehicle(id);
        this.user.setRentedVehicleId(id);
        this.userRepository.update(this.user);
    }

    private void ret(){
        this.vehicleRepository.returnVehicle(this.user.getRentedVehicleId());
        this.user.setRentedVehicleId("");
        this.userRepository.update(this.user);
    }

    private void addVehicle(){
        Vehicle v = null;

        System.out.println("Podaj dane pojazdu:\n" +
                "1 - Samochód | 2 - Motocykl");
        String type = this.scanner.nextLine();

        System.out.println("ID:");
        String id = this.scanner.nextLine();

        System.out.println("Marka:");
        String brand = this.scanner.nextLine();

        System.out.println("Model");
        String model = this.scanner.nextLine();

        System.out.println("Rok produkcji:");
        int year;
        try{
            year = Integer.parseInt(this.scanner.nextLine()); //number format
        }catch (NumberFormatException e){
            System.out.println("Rok musi być liczbą");
            return;
        }

        System.out.println("Cena:");
        double price;
        try{
            price = Double.parseDouble(this.scanner.nextLine()); //number format
        }catch (NumberFormatException e){
            System.out.println("Cena musi być liczbą");
            return;
        }

        if(type.equals("1")) {
            v = new Car(id, brand, model, year, price, false);
        }else if(type.equals("2")){
            System.out.println("Kategoria prawa jazdy:\n" +
                    "AM | A1 | A2 | B | A");
            DrivingLicenceCategory drivingLicence = null;
            try{
                drivingLicence = DrivingLicenceCategory.valueOf(this.scanner.nextLine());
            }catch (IllegalArgumentException e){
                System.out.println("Podaj prawidłową kategorię prawa jazdy");
                return;
            }
            v = new Motorcycle(id, brand, model, year, price, false, drivingLicence);
        }

        this.vehicleRepository.add(v);
    }

    private void removeVehicle(){
        System.out.println("Podaj ID");
        String id = this.scanner.nextLine();

        Optional<Vehicle> opt = this.vehicleRepository.findById(id);
        if(opt.isEmpty()){
            System.out.println("Nie znaleziono pojazdu z podanym ID");
            return;
        }
        Vehicle vehicle = opt.get();
        System.out.println(vehicle);
        System.out.println("Czy napewno chcesz usunąć ten pojazd?\n" +
                "T - tak | Inne - nie");
        String tmp = this.scanner.nextLine();
        if(tmp.equals("T") || tmp.equals("t")){
            this.vehicleRepository.deleteById(id);
        }
    }

    private void showUsers(){
        List<User> copy = this.userRepository.findAll();
        for(User u : copy){
            System.out.println(u.toString());
        }
    }

    private void removeUser(){
        System.out.println("Podaj ID");
        String id = this.scanner.nextLine();

        Optional<User> opt = this.userRepository.findById(id);
        if(opt.isEmpty()){
            System.out.println("Nie znaleziono użytkownika z podanym id");
            return;
        }
        User user = opt.get();
        System.out.println(user);
        System.out.println("Czy napewno chcesz usunąć tego użytkownika?\n" +
                "T - tak | Inne - nie");
        String tmp = this.scanner.nextLine();
        if(tmp.equals("T") || tmp.equals("t")){
            this.userRepository.deleteById(id);
        }
    }
}
