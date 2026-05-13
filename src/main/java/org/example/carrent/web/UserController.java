package org.example.carrent.web;

import org.example.carrent.models.User;
import org.example.carrent.services.UserServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceInterface userService;

    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> list(){
        return this.userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable String id){
        return this.userService.findById(id);
    }
}
