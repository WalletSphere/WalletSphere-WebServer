package com.khomishchak.cryproportfolio.services;

import com.khomishchak.cryproportfolio.exceptions.UserNotFoundException;
import com.khomishchak.cryproportfolio.model.User;
import com.khomishchak.cryproportfolio.model.enums.UserRole;
import com.khomishchak.cryproportfolio.model.requests.LoginRequest;
import com.khomishchak.cryproportfolio.model.requests.RegistrationRequest;
import com.khomishchak.cryproportfolio.model.response.LoginResult;
import com.khomishchak.cryproportfolio.model.response.RegistrationResult;
import com.khomishchak.cryproportfolio.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public RegistrationResult registerUser(RegistrationRequest registrationRequest) {

        LocalDateTime currentMoment = LocalDateTime.now();

        User newUser = User.builder()
                .username(registrationRequest.username())
                .password(passwordEncoder.encode(registrationRequest.password()))
                .email(registrationRequest.email())
                .createdTime(currentMoment)
                .lastLoginTime(currentMoment)
                .userRole(UserRole.NORMAL)
                .build();

        return getRegistrationResult(userRepository.save(newUser));
    }

    @Override
    public LoginResult authenticateUser(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(
                () -> new UserNotFoundException(String.format("User with username %s was not found", loginRequest.username()))
        );

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResult(loginRequest.username());
    }

    private RegistrationResult getRegistrationResult(User createdUser) {

        return new RegistrationResult(createdUser.getUsername(), createdUser.getEmail(),
                createdUser.getUserRole());
    }
}