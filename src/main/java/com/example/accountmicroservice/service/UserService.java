package com.example.accountmicroservice.service;

import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.models.Role;
import com.example.accountmicroservice.models.UserEntity;
import com.example.accountmicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserEntity> getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void createUser(SignUpRequest request){
        UserEntity user = UserEntity.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .username(request.getUsername())
                        .roles(Collections.singleton(Role.USER))
                        .build();

        userRepository.save(user);
    }
}
