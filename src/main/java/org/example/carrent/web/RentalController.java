package org.example.carrent.web;

import org.example.carrent.dto.RentalRequest;
import org.example.carrent.models.Rental;
import org.example.carrent.models.User;
import org.example.carrent.services.RentalServiceInterface;
import org.example.carrent.services.UserServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalServiceInterface rentalService;
    private final UserServiceInterface userService;

    public RentalController(RentalServiceInterface rentalService,
                            UserServiceInterface userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    public List<Rental> list(){
        return this.rentalService.findAllRentals();
    }

    @GetMapping("/users/{id}")
    public List<Rental> listByUserId(@PathVariable String id){
        return this.rentalService.findUserRentals(id);
    }

    @PostMapping("/rent")
    public ResponseEntity<Rental> rent(
            @RequestBody RentalRequest rentalRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String login = userDetails.getUsername();
        User user = this.userService.findByLogin(login);

        Rental rental = this.rentalService.rentVehicle(
                user.getId(),
                rentalRequest.vehicleId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PostMapping("/return")
    public ResponseEntity<Rental> ret(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = this.userService.findByLogin(login);

        Rental rental = this.rentalService.returnVehicle(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(rental);
    }
}
