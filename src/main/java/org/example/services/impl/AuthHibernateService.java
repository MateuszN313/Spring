package org.example.services.impl;

import org.example.models.User;
import org.example.services.AuthServiceInterface;

import java.util.Optional;

public class AuthHibernateService implements AuthServiceInterface {
    @Override
    public boolean register(String login, String rawPassword) {
        return false;
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        return Optional.empty();
    }
}
