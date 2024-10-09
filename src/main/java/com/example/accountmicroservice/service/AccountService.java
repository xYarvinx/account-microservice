package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.JwtAuthentication;
import com.example.accountmicroservice.dto.AccountResponseDto;
import com.example.accountmicroservice.dto.AccountRequestDto;
import com.example.accountmicroservice.dto.SignUpRequestDto;
import com.example.accountmicroservice.dto.UpdateAccountRequestDto;
import com.example.accountmicroservice.exception.AccountExistException;
import com.example.accountmicroservice.exception.AccountNotFountException;
import com.example.accountmicroservice.exception.InvalidDataException;
import com.example.accountmicroservice.models.Role;
import com.example.accountmicroservice.models.AccountEntity;
import com.example.accountmicroservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountEntity getAccount(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new AccountNotFountException("Пользователь не найден")
        );
    }

    public AccountEntity getAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFountException("Пользователь не найден")
        );
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public void createAccount(SignUpRequestDto request) {
        AccountEntity account = AccountEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .roles(Collections.singleton(Role.USER))
                .build();

        accountRepository.save(account);
    }

    public AccountResponseDto infoMe() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        AccountEntity account = getAccount(jwtAuthentication.getUsername());

        Set<String> roleStrings = account.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        return AccountResponseDto.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .roles(roleStrings)
                .username(account.getUsername())
                .build();
    }

    @Transactional
    public void updateAccount(UpdateAccountRequestDto request) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        AccountEntity account = getAccount(jwtAuthentication.getUsername());
        try {
            account = account.builder()
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            accountRepository.save(account);
        } catch (Exception e) {
            throw new InvalidDataException("Ошибка в данных");
        }
    }

    public List<AccountResponseDto> getAccounts(Integer from, Integer count) {

        if (from == null || from < 0) {
            throw new InvalidDataException("Параметр from должен быть неотрицательным.");
        }
        if (count == null || count <= 0) {
            throw new InvalidDataException("Параметр count должен быть больше 0");
        }


        List<AccountResponseDto> accounts = accountRepository.findAll().stream()
                .skip(from)
                .limit(count)
                .map(account -> AccountResponseDto.builder()
                        .id(account.getId())
                        .firstName(account.getFirstName())
                        .lastName(account.getLastName())
                        .username(account.getUsername())
                        .roles(account.getRoles().stream()
                                .map(Role::getAuthority)
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        if (accounts.isEmpty()) {
            throw new AccountNotFountException("Аккаунты по заданным критериям не найдены");
        }

        return accounts;
    }

    public void createAccountByAdmin(AccountRequestDto request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new AccountExistException("Пользователь с таким username уже существует.");
        }
        try {
            AccountEntity account = new AccountEntity().builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .roles(request.getRoles().stream()
                            .map(role -> Role.valueOf(role.toUpperCase()))
                            .collect(Collectors.toSet()))
                    .build();
            accountRepository.save(account);
        } catch (Exception e) {
            throw new InvalidDataException("Ошибка в данных");
        }
    }

    @Transactional
    public void updateAccountByAdmin(Long userId, AccountRequestDto request) {
        AccountEntity account = getAccount(userId);

        if (!account.getUsername().equals(request.getUsername()) && accountRepository.existsByUsername(request.getUsername())) {
            throw new AccountExistException("Пользователь с таким username уже существует.");
        }

        try {
            account.setUsername(request.getUsername());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setFirstName(request.getFirstName());
            account.setLastName(request.getLastName());
            account.setRoles(request.getRoles().stream()
                    .map(role -> Role.valueOf(role.toUpperCase()))
                    .collect(Collectors.toSet()));
            accountRepository.save(account);
        } catch (Exception e) {
            throw new InvalidDataException("Ошибка в данных");
        }
    }

    public void deleteAccount(Long userId) {
        AccountEntity account = getAccount(userId);
        accountRepository.delete(account);
    }

    public List<AccountEntity> getDoctors(String nameFilter, Integer from, Integer count) {
        if (nameFilter == null || nameFilter.isEmpty()) {
            throw new InvalidDataException("Фильтр имени не может быть нулевым или пустым");
        }
        if (from == null || from < 0) {
            throw new InvalidDataException("Параметр from должен быть неотрицательным");
        }
        if (count == null || count <= 0) {
            throw new InvalidDataException("Параметр count должен быть больше 0");
        }


        List<AccountEntity> doctors = accountRepository.findAll().stream()
                .filter(account -> account.getRoles().contains(Role.DOCTOR))
                .filter(account -> account.getFullName().contains(nameFilter))
                .skip(from)
                .limit(count)
                .collect(Collectors.toList());


        if (doctors.isEmpty()) {
            throw new AccountNotFountException("Доктора по заданным критериям не найдены");
        }

        return doctors;
    }


    public AccountEntity getDoctor(Long id) {
        AccountEntity doctor = getAccount(id);
        if (!doctor.getRoles().contains(Role.DOCTOR)) {
            throw new AccountNotFountException("Пользователь не является доктором");
        }
        return doctor;
    }
}
