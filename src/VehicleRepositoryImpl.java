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
    public void rentVehicle(int id) {
        for(Vehicle v : this.vehicles){
            if(Integer.parseInt(v.getId()) == id) {
                if(v.isRented()){
                    System.out.println("Ten pojazd jest już wypożyczony");
                    return;
                }
                v.setRented(true);
                save();
                return;
            }
        }
        System.out.println("Nie znaleziono pojazdu z podanym id");
    }

    @Override
    public void returnVehicle(int id) {
        for(Vehicle v : this.vehicles){
            if(Integer.parseInt(v.getId()) == id) {
                if(!v.isRented()){
                    System.out.println("Ten pojazd nie jest wypożyczony");
                    return;
                }
                v.setRented(false);
                save();
                return;
            }
        }
        System.out.println("Nie znaleziono pojazdu z podanym id");
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
            PrintWriter writer = new PrintWriter("vehicles.txt");
            for(Vehicle v : this.vehicles){
                writer.println(v.toCSV());
            }
            writer.close();
        }catch (FileNotFoundException e){
            System.out.println("Błąd zapisu pliku");
        }
    }

    @Override
    public void load() {
        try{
            File file = new File("vehicles.txt");
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
}
