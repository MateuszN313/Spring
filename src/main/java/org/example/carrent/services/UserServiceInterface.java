package org.example.carrent.services;

import org.example.carrent.models.User;

import java.util.List;

public interface UserServiceInterface {

    List<User> findAllUsers();

    User findById(String id);

    void deleteUser(String id, String loggedUserId);

    User findByLogin(String login);
}