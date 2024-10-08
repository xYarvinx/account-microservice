package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.JwtAuthentication;
import com.example.accountmicroservice.dto.AccountResponse;
import com.example.accountmicroservice.dto.AccountRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.dto.UpdateAccountRequest;
import com.example.accountmicroservice.models.Role;
import com.example.accountmicroservice.models.AccountEntity;
import com.example.accountmicroservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    private void createDefaultAccount(){
        if(accountRepository.findByUsername("admin").isEmpty()){
            AccountEntity user = AccountEntity.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .username("admin")
                    .roles(Collections.singleton(Role.ADMIN))
                    .build();
            accountRepository.save(user);
        }
        if(accountRepository.findByUsername("user").isEmpty()){
            AccountEntity user = AccountEntity.builder()
                    .firstName("user")
                    .lastName("user")
                    .password(passwordEncoder.encode("user"))
                    .username("user")
                    .roles(Collections.singleton(Role.USER))
                    .build();
            accountRepository.save(user);
        }
        if(accountRepository.findByUsername("doctor").isEmpty()){
            AccountEntity user = AccountEntity.builder()
                    .firstName("doctor")
                    .lastName("doctor")
                    .password(passwordEncoder.encode("doctor"))
                    .username("doctor")
                    .roles(Collections.singleton(Role.DOCTOR))
                    .build();
            accountRepository.save(user);
        }
        if(accountRepository.findByUsername("manager").isEmpty()){
            AccountEntity user = AccountEntity.builder()
                    .firstName("manager")
                    .lastName("manager")
                    .password(passwordEncoder.encode("manager"))
                    .username("manager")
                    .roles(Collections.singleton(Role.MANAGER))
                    .build();
            accountRepository.save(user);
        }
    }

    public Optional<AccountEntity> getByUsername(String username){
         return accountRepository.findByUsername(username);
    }

    public void createAccount(SignUpRequest request){
        AccountEntity user = AccountEntity.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .username(request.getUsername())
                        .roles(Collections.singleton(Role.USER))
                        .build();

        accountRepository.save(user);
    }

    public AccountResponse infoMe() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Optional<AccountEntity> account = accountRepository.findByUsername(jwtAuthentication.getUsername());

        Set<String> roleStrings = account.get().getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        return AccountResponse.builder()
                                    .id(account.get().getId())
                                    .firstName(account.get().getFirstName())
                                    .lastName(account.get().getLastName())
                                    .roles(roleStrings)
                                    .username(account.get().getUsername())
                                    .build();
    }

    public void updateAccount(UpdateAccountRequest request) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Optional<AccountEntity> account = accountRepository.findByUsername(jwtAuthentication.getUsername());
        if(account.isPresent()) {
            if (request.getFirstName() != null) {
                account.get().setFirstName(request.getFirstName());
            }

            if (request.getLastName() != null) {
                account.get().setLastName(request.getLastName());
            }

            if (request.getPassword() != null) {
                account.get().setPassword(passwordEncoder.encode(request.getPassword()));
            }

        accountRepository.save(account.get());
        } else {
            throw new RuntimeException("{\"error\": \"Пользователь не найден\"}");
        }
    }

    public List<AccountResponse> getAccounts(Integer from, Integer count) {
        try {
            return accountRepository.findAll().stream()
                    .skip(from)
                    .limit(count)
                    .map(account -> AccountResponse.builder()
                            .id(account.getId())
                            .firstName(account.getFirstName())
                            .lastName(account.getLastName())
                            .username(account.getUsername())
                            .roles(account.getRoles().stream()
                                    .map(Role::getAuthority)
                                    .collect(Collectors.toSet()))
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new RuntimeException("{\"error\": \"Ошибка при получении списка пользователей\"}");
        }

    }

    public void createAccountByAdmin(AccountRequest request) {
        AccountEntity user = AccountEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .roles(request.getRoles().stream()
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()))
                .build();

        accountRepository.save(user);
    }

    public void updateAccountByAdmin(Long userId, AccountRequest request) {
        Optional<AccountEntity> account = accountRepository.findById(userId);
        if(account.isPresent()){
            if (request.getFirstName() != null) {
                account.get().setFirstName(request.getFirstName());
            }

            if (request.getLastName() != null) {
                account.get().setLastName(request.getLastName());
            }

            if (request.getPassword() != null) {
                account.get().setPassword(passwordEncoder.encode(request.getPassword()));
            }

            if (request.getUsername() != null) {
                account.get().setUsername(request.getUsername());
            }

            if(request.getRoles() != null) {
                account.get().setRoles(request.getRoles().stream()
                        .map(Role::valueOf)
                        .collect(Collectors.toSet()));
            }
        } else {
            throw new RuntimeException("{\"error\": \"Аккаунт пользователя не найден\"}");
        }
    }

    public void deleteAccount(Long userId) {
        Optional<AccountEntity> account = accountRepository.findById(userId);
        if(account.isPresent()){
            accountRepository.delete(account.get());
        } else {
            throw new RuntimeException("{\"error\": \"Аккаунт пользователя не найден\"}");
        }
    }


}
