package Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehicleRepositoryImpl implements IVehicleRepository {
    private final List<Vehicle> vehicles;
    public VehicleRepositoryImpl() {
        this.vehicles = new ArrayList<>();
        this.load();
    }
    @Override
    public boolean rentVehicle(String id) {
        for(Vehicle v : this.vehicles){
            if(v.getId().equals(id)) {
                if(v.isRented()){
                    System.out.println("Ten pojazd jest już wypożyczony");
                    return false;
                }
                v.setRented(true);
                save();
                System.out.println("Wypożyczono pojazd");
                return true;
            }
        }
        System.out.println("Nie znaleziono pojazdu z podanym id");
        return false;
    }

    @Override
    public boolean returnVehicle(String id) {
        for(Vehicle v : this.vehicles){
            if(v.getId().equals(id)) {
                if(!v.isRented()){
                    System.out.println("Ten pojazd nie jest wypożyczony");
                    return false;
                }
                v.setRented(false);
                save();
                System.out.println("Zwrócono pojazd");
                return true;
            }
        }
        System.out.println("Nie znaleziono pojazdu z podanym ID");
        return false;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> copy = new ArrayList<>();
        for(Vehicle v : this.vehicles){
            if(v instanceof Car) {
                Car car = (Car) v;
                copy.add(new Car(car));
            }else if(v instanceof Motorcycle){
                Motorcycle motorcycle = (Motorcycle) v;
                copy.add(new Motorcycle(motorcycle));
            }
        }
        return copy;
    }

    @Override
    public void save() {
        try{
            PrintWriter writer = new PrintWriter("src/main/resources/vehicles.csv");
            for(Vehicle v : this.vehicles){
                writer.println(v.toCSV());
            }
            writer.close();
        }catch (FileNotFoundException e){
            System.out.println("Błąd zapisu pliku");
        }
    }

    public void load() {
        try{
            File file = new File("src/main/resources/vehicles.csv");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] tokens = line.split(";");

                String id = tokens[1];
                String brand = tokens[2];
                String model = tokens[3];
                int year = Integer.parseInt(tokens[4]);
                double price = Double.parseDouble(tokens[5]);
                boolean rented = Boolean.parseBoolean(tokens[6]);

                if(tokens[0].equals("CAR"))
                    this.vehicles.add(new Car(id, brand, model, year, price, rented));
                else if(tokens[0].equals("MOTORCYCLE")){
                    DrivingLicenceCategory licence = DrivingLicenceCategory.valueOf(tokens[7]);
                    this.vehicles.add(new Motorcycle(id, brand, model, year, price, rented, licence));
                }
            }
            scanner.close();
        }catch (FileNotFoundException e){
            System.out.println("Nie znaleziono pliku");
        }
    }

    public Vehicle getVehicle(String id) {
        for(Vehicle v : this.vehicles){
            if(v.getId().equals(id)) {
                if(v instanceof Car) return new Car((Car) v);
                if(v instanceof Motorcycle) return new Motorcycle((Motorcycle) v);
            }
        }
        return null;
    }

    @Override
    public boolean add(Vehicle vehicle) {
        if(vehicle == null){
            System.out.println("Błąd w dodawaniu pojazdu");
            return false;
        }

        for(Vehicle v : this.vehicles){
            if(v.getId().equals(vehicle.getId())){
                System.out.println("Istnieje pojazd z podanym ID");
                return false;
            }
        }

        this.vehicles.add(vehicle);
        save();
        System.out.println("Dodano pojazd");
        return true;
    }

    @Override
    public boolean remove(String id) {
        for(int i = 0; i < this.vehicles.size(); i++){
            Vehicle v = this.vehicles.get(i);
            if(v.getId().equals(id)){
                if(v.isRented()){
                    System.out.println("Nie można usunąć pojazdu bo jest wypożyczony");
                    return false;
                }
                this.vehicles.remove(i);
                save();
                System.out.println("Usunięto pojazd");
                return true;
            }
        }

        System.out.println("Nie znaleziono pojazdu z podanym ID");
        return false;
    }
}
