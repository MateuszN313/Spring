package org.example.User;

public class User {
    private String login;
    private String password;
    private Role role;
    private String rentedVehicleId;

    public User(String login, String password, Role role, String rentedVehicleId) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedVehicleId = rentedVehicleId;
    }
    public User(User user){
        this.login = user.login;
        this.password = user.password;
        this.role = user.role;
        this.rentedVehicleId = user.rentedVehicleId;
    }

    public String getLogin() {
        return this.login;
    }
    public String getPassword() {
        return this.password;
    }
    public Role getRole() {
        return role;
    }
    public String getRentedVehicleId() {
        return this.rentedVehicleId;
    }

    public void setRentedVehicleId(String rentedVehicleId) {
        this.rentedVehicleId = rentedVehicleId;
    }

    @Override
    public String toString() {
        return "login: " + this.login + ", role: " + this.role + ", rented vehicle ID: " + this.rentedVehicleId;
    }
    public String toCSV(){
        return this.login + ";" + password + ";" + this.role + ";" + this.rentedVehicleId;
    }
}
