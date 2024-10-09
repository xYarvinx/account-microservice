package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.AccountResponseDto;
import com.example.accountmicroservice.dto.AccountRequestDto;
import com.example.accountmicroservice.dto.MessageResponseDto;
import com.example.accountmicroservice.dto.UpdateAccountRequestDto;
import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import com.example.accountmicroservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Accounts")
@AllArgsConstructor
@ControllerExceptionHandler
public class AccountController {
    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/Me")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDto infoMe(){
        return accountService.infoMe();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/Update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponseDto updateAccount(@RequestBody @Validated UpdateAccountRequestDto request){
        accountService.updateAccount(request);
        return new MessageResponseDto("Аккаунт успешно обновлен");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponseDto> getAccounts(@RequestParam Integer from, @RequestParam Integer count){
        return accountService.getAccounts(from, count);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto createAccountByAdmin(@RequestBody @Validated AccountRequestDto request){
        accountService.createAccountByAdmin(request);
        return new MessageResponseDto("Аккаунт пользователя успешно создан");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponseDto updateAccountByAdmin(@PathVariable Long userId, @RequestBody AccountRequestDto request){
        accountService.updateAccountByAdmin(userId, request);
        return new MessageResponseDto("Аккаунт пользователя успешно обновлен");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponseDto deleteAccount(@PathVariable Long userId){
        accountService.deleteAccount(userId);
        return new MessageResponseDto("Аккаунт пользователя успешно удален");
    }

}
