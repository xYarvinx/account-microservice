package com.example.accountmicroservice.service;

import com.example.accountmicroservice.models.UserEntity;
import com.example.accountmicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserEntity> getByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
