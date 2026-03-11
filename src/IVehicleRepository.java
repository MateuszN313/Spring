import java.util.List;
public interface IVehicleRepository {
    void rentVehicle(int id);
    void returnVehicle(int id);
    List<Vehicle> getVehicles();
    void save();
    void load();
}
