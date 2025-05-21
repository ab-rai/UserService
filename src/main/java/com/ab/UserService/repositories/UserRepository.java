package com.ab.UserService.repositories;

import com.ab.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public User save(User user);
    public Optional<User> findByEmail(String email);
}
