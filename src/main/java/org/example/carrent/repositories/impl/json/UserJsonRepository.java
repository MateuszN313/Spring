package org.example.carrent.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import org.example.carrent.db.JsonFileStorage;
import org.example.carrent.models.User;
import org.example.carrent.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("json")
public class UserJsonRepository implements UserRepository {
    private final List<User> users;
    private final JsonFileStorage<User> storage;

    public UserJsonRepository(@Value("${carrent.json.users-file}") String filename) {
        storage = new JsonFileStorage<>(filename, new TypeToken<List<User>>() {}.getType());
        this.users = new ArrayList<>(this.storage.load());
    }

    @Override
    public List<User> findAll() {
        List<User> copy = new ArrayList<>();
        for(User user : this.users){
            copy.add(user.copy());
        }
        return copy;
    }

    @Override
    public Optional<User> findById(String id) {
        return this.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(User::copy);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return this.users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .map(User::copy);
    }

    @Override
    public User save(User user) {
        if(user == null)
            throw new IllegalArgumentException("user cannot be null");

        User toSave = user.copy();
        if(toSave.getId() == null || toSave.getId().isBlank())
            toSave.setId(UUID.randomUUID().toString());
        else
            this.users.removeIf(u -> u.getId().equals(toSave.getId()));

        this.users.add(toSave);
        this.storage.save(this.users);
        return toSave.copy();
    }

    @Override
    public void deleteById(String id) {
        this.users.removeIf(user ->user.getId().equals(id));
        this.storage.save(this.users);
    }
}
