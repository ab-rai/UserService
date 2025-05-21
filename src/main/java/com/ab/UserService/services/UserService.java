package com.ab.UserService.services;


import com.ab.UserService.dtos.LoginRequestDto;
import com.ab.UserService.dtos.SignUpRequestDto;
import com.ab.UserService.dtos.SignUpResponseDto;
import com.ab.UserService.models.Token;
import com.ab.UserService.models.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public Token login(LoginRequestDto loginRequestDto) throws Exception;
    public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto) throws Exception;
    public ResponseEntity<Void> logout(Token token );
    public User validateToken(String token) throws Exception;
}
