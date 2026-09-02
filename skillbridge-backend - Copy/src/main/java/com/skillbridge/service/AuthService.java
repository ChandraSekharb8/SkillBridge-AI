package com.skillbridge.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillbridge.dto.AuthResponse;
import com.skillbridge.dto.LoginRequest;
import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.model.User;
import com.skillbridge.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already registered", null, null, null);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                true,
                "User registered successfully",
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return new AuthResponse(false, "User not found", null, null, null);
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return new AuthResponse(false, "Invalid password", null, null, null);
        }

        return new AuthResponse(
                true,
                "Login successful",
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}