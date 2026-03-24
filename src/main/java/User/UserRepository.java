package User;

import Vehicle.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRepository implements IUserRepository {
    private final List<User> users;
    public UserRepository(){
        this.users = new ArrayList<>();
        load();
    }
    @Override
    public User getUser(String login) {
        for(User u : this.users){
            if(u.getLogin().equals(login)) return u;
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> copy = new ArrayList<>();
        for(User u : this.users){
            copy.add(new User(u));
        }
        return copy;
    }

    @Override
    public boolean update(User user) {
        for(User u : this.users){
            if(u.getLogin().equals(user.getLogin())){
                u.setRentedVehicleId(user.getRentedVehicleId());
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(User user){
        if(getUser(user.getLogin()) == null){
            this.users.add(user);
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(String login){
        for(int i = 0; i < this.users.size(); i++){
            User u = this.users.get(i);
            if(u.getLogin().equals(login)){
                if(!u.getRentedVehicleId().isEmpty()){
                    System.out.println("Nie można usunąć użytkownika bo ma wypożyczony pojazd");
                    return false;
                }
                this.users.remove(i);
                save();
                System.out.println("Usunięto użytkownika");
                return true;
            }
        }

        System.out.println("Nie znaleziono użytkownika z podanym loginem");
        return false;
    }

    public void save() {
        try{
            PrintWriter writer = new PrintWriter("src/main/resources/users.csv");
            for(User u : this.users){
                writer.println(u.toCSV());
            }
            writer.close();
        }catch (FileNotFoundException e){
            System.out.println("Błąd zapisu pliku");
        }
    }

    public void load() {
        try{
            File file = new File("src/main/resources/users.csv");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] tokens = line.split(";");

                String login = tokens[0];
                String password = tokens[1];
                Role role = Role.valueOf(tokens[2]);
                String rentedVehicleId = "";

                if(tokens.length == 4){
                    rentedVehicleId = tokens[3];
                }


                this.users.add(new User(login, password, role, rentedVehicleId));
            }
            scanner.close();
        }catch (FileNotFoundException e){
            System.out.println("Nie znaleziono pliku");
        }
    }
}
