package com.example.accountmicroservice.config;

import com.example.accountmicroservice.models.AccountEntity;
import com.example.accountmicroservice.models.Role;
import com.example.accountmicroservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@AllArgsConstructor
public class AccountConfiguration {
    private final PasswordEncoder passwordEncoder;
    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository) {
        return args -> {
             if (accountRepository.findByUsername("admin").isEmpty()) {
                 AccountEntity account = AccountEntity.builder()
                         .firstName("admin")
                         .lastName("admin")
                         .password(passwordEncoder.encode("admin"))
                         .username("admin")
                         .roles(Collections.singleton(Role.ADMIN))
                         .build();
                 accountRepository.save(account);
             }
             if (accountRepository.findByUsername("user").isEmpty()) {
                 AccountEntity account = AccountEntity.builder()
                         .firstName("user")
                         .lastName("user")
                         .password(passwordEncoder.encode("user"))
                         .username("user")
                         .roles(Collections.singleton(Role.USER))
                         .build();
                 accountRepository.save(account);
             }
             if (accountRepository.findByUsername("doctor").isEmpty()) {
                 AccountEntity user = AccountEntity.builder()
                         .firstName("doctor")
                         .lastName("doctor")
                         .password(passwordEncoder.encode("doctor"))
                         .username("doctor")
                         .roles(Collections.singleton(Role.DOCTOR))
                         .build();
                 accountRepository.save(user);
             }
             if(accountRepository.findByUsername("manager").isEmpty()) {
                 AccountEntity user = AccountEntity.builder()
                         .firstName("manager")
                         .lastName("manager")
                         .password(passwordEncoder.encode("manager"))
                         .username("manager")
                         .roles(Collections.singleton(Role.MANAGER))
                         .build();
                 accountRepository.save(user);
             }
        };
    }
}
