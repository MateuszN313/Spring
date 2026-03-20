package Vehicle;

public class Motorcycle extends Vehicle {
    private DrivingLicenceCategory drivingLicenceCategory;

    public Motorcycle(String id, String brand, String model, int year, double price, boolean rented, DrivingLicenceCategory drivingLicenceCategory) {
        super(id, brand, model, year, price, rented);
        this.drivingLicenceCategory = drivingLicenceCategory;
    }

    public Motorcycle(Motorcycle motorcycle){
        super(motorcycle.id, motorcycle.brand, motorcycle.model, motorcycle.year, motorcycle.price, motorcycle.rented);
        this.drivingLicenceCategory = motorcycle.drivingLicenceCategory;
    }

    @Override
    public String toCSV(){
        return "MOTORCYCLE;" + super.toCSV() + ";" + this.drivingLicenceCategory;
    }

    @Override
    public String toString(){
        return super.toString() + ", driving licence category: " + this.drivingLicenceCategory;
    }
}
