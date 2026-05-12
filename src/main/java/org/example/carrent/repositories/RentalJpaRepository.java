package org.example.carrent.repositories;

import org.example.carrent.models.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface RentalJpaRepository extends JpaRepository<User, String> {
}
