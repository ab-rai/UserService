package com.ab.UserService.dtos;

import com.ab.UserService.models.Role;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignUpResponseDto{
    private String name;
    private String email;

    private boolean isEmailVerified;
}
