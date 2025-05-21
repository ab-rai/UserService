package com.ab.UserService.controllers;

import com.ab.UserService.dtos.LoginRequestDto;
import com.ab.UserService.dtos.SignUpRequestDto;
import com.ab.UserService.dtos.SignUpResponseDto;
import com.ab.UserService.models.Token;
import com.ab.UserService.models.User;
import com.ab.UserService.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return userService.login(loginRequestDto);
//        return null;
    }
    @PostMapping("signup")
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) throws Exception {
         return userService.signup(signUpRequestDto);
//        return null;
    }
    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(@RequestBody Token token ){
         return userService.logout(token);
//        return null;
    }
    @PostMapping("validate")
    public  User validateToken(@RequestBody Token token) throws Exception {
        return userService.validateToken(token.getValue());
    }


}
