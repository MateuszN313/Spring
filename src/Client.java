import java.util.List;
import java.util.Scanner;

public class Client {
    public static void UI(VehicleRepositoryImpl repo) {
        int choice;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("Wybierz akcję:\n" +
                    "1 - Wyświetl pojazdy\n" +
                    "2 - Wypożycz pojazd\n" +
                    "3 - Zwróć pojazd\n" +
                    "4 - Wyjdź");
            choice = scanner.nextInt();

            if(choice == 1){
                List<Vehicle> copy = repo.getVehicles();
                for(Vehicle v : copy){
                    System.out.println(v.toString());
                }
            }else if(choice == 2){
                System.out.println("Podaj id");
                int id = scanner.nextInt();
                repo.rentVehicle(id);
            }else if(choice == 3){
                System.out.println("Podaj id");
                int id = scanner.nextInt();
                repo.returnVehicle(id);
            }
        }while(choice != 4);
    }
}
