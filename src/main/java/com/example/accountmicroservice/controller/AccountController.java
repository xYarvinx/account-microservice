package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.AccountResponse;
import com.example.accountmicroservice.dto.CreateAccountRequest;
import com.example.accountmicroservice.dto.UpdateAccountRequest;
import com.example.accountmicroservice.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<?> updateMe(@RequestBody @Validated UpdateAccountRequest request){
        try {
            accountService.updateMe(request);
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
    public ResponseEntity<?> createAccountByAdmin(@RequestBody @Validated CreateAccountRequest request){
        try {
            accountService.createAccountByAdmin(request);
            return ResponseEntity.ok("{\"message\": \"Пользователь успешно создан\"}");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
