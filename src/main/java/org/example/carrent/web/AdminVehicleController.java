package org.example.carrent.web;

import lombok.RequiredArgsConstructor;
import org.example.carrent.models.Vehicle;
import org.example.carrent.services.VehicleServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/vehicles")
@RequiredArgsConstructor
public class AdminVehicleController {
    VehicleServiceInterface vehicleService;

    @PostMapping
    public Vehicle create(@RequestBody Vehicle vehicle){
        return vehicleService.addVehicle(vehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        this.vehicleService.removeVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
