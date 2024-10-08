package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.AccountResponse;
import com.example.accountmicroservice.dto.AccountRequest;
import com.example.accountmicroservice.dto.UpdateAccountRequest;
import com.example.accountmicroservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/Me")
    public ResponseEntity<?> infoMe(){
        AccountResponse info = accountService.infoMe();
        return ResponseEntity.ok(info);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/Update")
    public ResponseEntity<?> updateAccount(@RequestBody @Validated UpdateAccountRequest request){
        try {
            accountService.updateAccount(request);
            return ResponseEntity.ok("{\"message\": \"Данные пользователя обновлены\"}");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getAccounts(@RequestParam Integer from, @RequestParam Integer count){
        try {
            return ResponseEntity.ok(accountService.getAccounts(from, count));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> createAccountByAdmin(@RequestBody @Validated AccountRequest request){
        try {
            accountService.createAccountByAdmin(request);
            return ResponseEntity.ok("{\"message\": \"Аккаунт пользователя успешно создан\"}");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "{userId}")
    public ResponseEntity<?> updateAccountByAdmin(@PathVariable Long userId, @RequestBody AccountRequest request){
        try{
            accountService.updateAccountByAdmin(userId, request);
            return ResponseEntity.ok("{\"message\": \"Аккаунт пользователя успешно обновлен\"}");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "{userId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId){
        try {
            accountService.deleteAccount(userId);
            return ResponseEntity.ok("{\"message\": \"Аккаунт пользователя успешно удален\"}");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
