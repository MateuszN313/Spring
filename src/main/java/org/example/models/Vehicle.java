package org.example.Vehicle;

public abstract class Vehicle {
    protected String id;
    protected String brand;
    protected String model;
    protected int year;
    protected double price;
    protected boolean rented;

    public Vehicle(String id, String brand, String model, int year, double price, boolean rented) {
        try{
            Integer.parseInt(id);
        }catch (NumberFormatException e){
            System.out.println("ID musi być liczbą");
            return;
        }

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public String getId() {
        return id;
    }
    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented){
        this.rented = rented;
    }

    public String toCSV(){
        return this.id + ";" + this.brand + ";" + this.model + ";" + this.year + ";" + this.price + ";" + this.rented;
    }
    @Override
    public String toString() {
        return "id: " + this.id + ", brand: " + this.brand + ", model: " + this.model + ", year: " + this.year + ", price: " + this.price + ", rented: " + this.rented;
    }
}
