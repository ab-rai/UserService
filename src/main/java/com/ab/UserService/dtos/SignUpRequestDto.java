package com.ab.UserService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;

}
