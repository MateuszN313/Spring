package org.example.carrent.repositories.impl.jpa;

import org.example.carrent.models.User;
import org.example.carrent.repositories.UserJpaRepository;
import org.example.carrent.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class UserRepositoryJpaAdapter implements UserRepository {
    private final UserJpaRepository delegate;

    public UserRepositoryJpaAdapter(UserJpaRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
