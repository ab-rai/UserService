package com.ab.UserService.services;

import com.ab.UserService.dtos.LoginRequestDto;
import com.ab.UserService.dtos.SignUpRequestDto;
import com.ab.UserService.dtos.SignUpResponseDto;
import com.ab.UserService.models.Token;
import com.ab.UserService.models.User;
import com.ab.UserService.repositories.TokenRepository;
import com.ab.UserService.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public Token login(LoginRequestDto loginRequestDto) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        if(optionalUser.isEmpty())
            throw new Exception("User Id is invalid");
        User user = optionalUser.get();
        boolean passwordMatched = bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getHashedPassword());
        if(passwordMatched){
            String tokenValue = generateToken(loginRequestDto);
            LocalDate sevenDayAfterTime = LocalDate.now().plusDays(7);
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.HOUR_OF_DAY, 1);
//            Date oneHourLater = calendar.getTime();
            Token tokenObj = Token.builder()
                    .value(tokenValue)
                    .user(user)
                    .expiryAt(Date.from(sevenDayAfterTime.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .build();
            return tokenRepository.save(tokenObj);
        }
        throw new Exception("Id Password is invalid");
    }

    @Override
    public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto) throws Exception {
        if(validateUserExists(signUpRequestDto)){
            throw new Exception("User already exist with mail id:"+ signUpRequestDto.getEmail());
        }
        User user = User.builder()
                .name(signUpRequestDto.getName())
                .hashedPassword(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()))
                .email(signUpRequestDto.getEmail())
                .build();
        return toSignUpResponseDto( userRepository.save(user));

    }

    @Override
    @Transactional
    public ResponseEntity<Void> logout(Token token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(token.getValue(), false);
        if(optionalToken.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Token systemToken = optionalToken.get();
        systemToken.setDeleted(true);
        tokenRepository.save(systemToken);
        return ResponseEntity.ok().build(); // always return a valid response
    }

//    @Override
//    public User validateToken(String token) throws Exception {
//        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(token, false);
//        if(optionalToken.isEmpty()){
//            throw new Exception("Not found token:"+token);
////            return ResponseEntity.notFound().build();
//        }
//        Token tokenObj = optionalToken.get();
//        if(tokenObj.getExpiryAt().after(new Date()))
//            return optionalToken.get().getUser();
//        else throw new Exception("Expired token:"+token);
//
//    }

@Transactional
public User validateToken(String token) throws Exception {
    Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(token, false);
    if (optionalToken.isEmpty()) {
        throw new Exception("Not found token:" + token);
    }

    Token tokenObj = optionalToken.get();
    if (tokenObj.getExpiryAt().after(new Date())) {
        return tokenObj.getUser();
    } else {
        throw new Exception("Expired token:" + token);
    }
}

    private String  generateToken(LoginRequestDto loginRequestDto){
//        return loginRequestDto.toString();
        return RandomStringUtils.randomAlphanumeric(128);
    }


    public SignUpResponseDto toSignUpResponseDto(User user) {
        SignUpResponseDto dto = new SignUpResponseDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setEmailVerified(user.isEmailVerified());
        return dto;
    }

    private boolean validateUserExists(SignUpRequestDto signUpRequestDto){
        return userRepository.findByEmail(signUpRequestDto.getEmail()) != null;
    }
}
