package org.example.carrent.repositories;

import org.example.carrent.models.Rental;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface UserJpaRepository extends JpaRepository<Rental, String> {
}
