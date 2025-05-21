package com.ab.UserService.repositories;

import com.ab.UserService.models.Token;
import com.ab.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    public void deleteByValue(String value);
    public Optional<Token> findByValueAndDeletedEquals(String value, Boolean isDeleted);
}
