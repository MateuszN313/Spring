public class Car extends Vehicle{
    public Car(String id, String brand, String model, int year, double price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    public Car(Car car){
        super(car.id, car.brand, car.model, car.year, car.price, car.rented);
    }

    @Override
    public String toCSV() {
        return "CAR;" + super.toCSV();
    }
}
