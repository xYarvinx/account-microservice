package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import com.example.accountmicroservice.models.AccountEntity;
import com.example.accountmicroservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Doctors")
@AllArgsConstructor
@ControllerExceptionHandler
public class DoctorController {
    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<AccountEntity> getDoctors(@RequestParam String nameFilter, @RequestParam Integer from, @RequestParam Integer count){
        return accountService.getDoctors(nameFilter,from,count);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AccountEntity getDoctor(@PathVariable Long id){
        return accountService.getDoctor(id);
    }
}
