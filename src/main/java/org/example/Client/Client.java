package org.example.Client;

import org.example.User.User;
import org.example.User.Authentication;
import org.example.User.IUserRepository;
import org.example.User.Role;

import org.example.Vehicle.Vehicle;
import org.example.Vehicle.Car;
import org.example.Vehicle.Motorcycle;
import org.example.Vehicle.DrivingLicenceCategory;
import org.example.Vehicle.IVehicleRepository;

import java.util.List;
import java.util.Scanner;

import static org.example.User.Role.USER;

public class Client {
    private final IVehicleRepository vehicleRepository;
    private final IUserRepository userRepository;
    private final Scanner scanner;
    private final Authentication authentication;
    private User user;

    public Client(IVehicleRepository vehicleRepository, IUserRepository userRepository){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.scanner = new Scanner(System.in);
        this.authentication = new Authentication(userRepository);
    }

    public void UI() {
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
                while(!login());
                return true;
            }else if(choice.equals("2")){
                while(!register());
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
        String password = Authentication.hashPassword(this.scanner.nextLine());

        this.user = new User(login, password, USER, "");
        if(!this.userRepository.add(this.user)) {
            System.out.println("ten login jest już zajęty");
            return false;
        }
        return true;
    }
    private boolean login(){
        System.out.println("Podaj login");
        String login = this.scanner.nextLine();
        System.out.println("Podaj hasło");
        String password = this.scanner.nextLine();

        this.user = this.authentication.authenticate(login, password);
        if(this.user == null){
            System.out.println("błędny login lub hasło");
            return false;
        }
        return true;
    }
    private void showVehicles(){
        List<Vehicle> copy = this.vehicleRepository.getVehicles();
        for(Vehicle v : copy){
            System.out.println(v.toString());
        }
    }
    private void rent(){
        if(!this.user.getRentedVehicleId().isEmpty()){
            System.out.println("Masz już wypożyczony pojazd");
        }

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

        Vehicle v = this.vehicleRepository.getVehicle(id);
                if(v == null){
                    System.out.println("Nie znaleziono pojazdu z podanym ID");
                    return;
                }

        System.out.println(v);
        System.out.println("Czy napewno chcesz usunąć ten pojazd?\n" +
                "T - tak | Inne - nie");
        String tmp = this.scanner.nextLine();
        if(tmp.equals("T") || tmp.equals("t")){
            this.vehicleRepository.remove(id);
        }
    }
    private void showUsers(){
        List<User> copy = this.userRepository.getUsers();
        for(User u : copy){
            System.out.println(u.toString());
        }
    }

    private void removeUser(){
        System.out.println("Podaj login");
        String login = this.scanner.nextLine();

        User u = this.userRepository.getUser(login);
        if(u == null){
            System.out.println("Nie znaleziono użytkownika z podanym loginem");
            return;
        }

        System.out.println(u);
        System.out.println("Czy napewno chcesz usunąć tego użytkownika?\n" +
                "T - tak | Inne - nie");
        String tmp = this.scanner.nextLine();
        if(tmp.equals("T") || tmp.equals("t")){
            this.userRepository.remove(login);
        }
    }
}
