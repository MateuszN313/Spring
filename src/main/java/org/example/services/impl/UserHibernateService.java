package org.example.services.impl;

import org.example.models.User;
import org.example.services.UserServiceInterface;

import java.util.List;

public class UserHibernateService implements UserServiceInterface {
    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public void deleteUser(String id, String loggedUserId) {

    }
}
