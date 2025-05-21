package com.ab.UserService.repositories;

import com.ab.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<User, Long> {
}
