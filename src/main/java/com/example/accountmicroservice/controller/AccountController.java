package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.*;
import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import com.example.accountmicroservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Account Controller", description = "API для управления учетными записями пользователей")
@ApiResponse(responseCode = "40*", description = "Ошибка в запросе",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)) )
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "Информация о текущем пользователе", description = "Получает информацию об аутентифицированном пользователе.")
    @ApiResponse(responseCode = "200", description = "Успешное получение информации",
            content = @Content(schema = @Schema(implementation = AccountResponse.class)))

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/Me")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse infoMe(){

        return accountService.infoMe();
    }


    @Operation(summary = "Обновление информации аккаунта", description = "Обновляет информацию текущего пользователя.")
    @ApiResponse(responseCode = "202", description = "Аккаунт успешно обновлен",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/Update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponse updateAccount(@RequestBody @Validated UpdateAccountRequest request){

        accountService.updateAccount(request);
        return new MessageResponse("Аккаунт успешно обновлен");
    }


    @Operation(summary = "Получение списка пользователей", description = "Получает список учетных записей пользователей, доступно только администраторам.")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка пользователей",
            content = @Content(schema = @Schema(implementation = AccountResponse.class)))

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAccounts(
            @Parameter(description = "Начальный индекс выборки", example = "0", required = true)
            @RequestParam Integer from,

            @Parameter(description = "Количество записей для выборки", example = "10", required = true)
            @RequestParam Integer count){

        return accountService.getAccounts(from, count);
    }


    @Operation(summary = "Создание нового аккаунта", description = "Создает новый аккаунт пользователя, доступно только администраторам.")
    @ApiResponse(responseCode = "201", description = "Аккаунт успешно создан",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createAccountByAdmin(@RequestBody @Validated AccountRequest request){

        accountService.createAccountByAdmin(request);
        return new MessageResponse("Аккаунт пользователя успешно создан");
    }


    @Operation(summary = "Обновление аккаунта пользователя", description = "Обновляет аккаунт пользователя по его идентификатору, доступно только администраторам.")
    @ApiResponse(responseCode = "202", description = "Аккаунт успешно обновлен",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponse updateAccountByAdmin(
            @Parameter(description = "Идентификатор пользователя", example = "1", required = true)
            @PathVariable Long userId,

            @RequestBody AccountRequest request){

        accountService.updateAccountByAdmin(userId, request);
        return new MessageResponse("Аккаунт пользователя успешно обновлен");
    }


    @Operation(summary = "Удаление аккаунта пользователя", description = "Удаляет аккаунт пользователя по его идентификатору, доступно только администраторам.")
    @ApiResponse(responseCode = "202", description = "Аккаунт успешно удален",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponse deleteAccount(
            @Parameter(description = "Идентификатор пользователя", example = "1", required = true)
            @PathVariable Long userId){

        accountService.deleteAccount(userId);
        return new MessageResponse("Аккаунт пользователя успешно удален");
    }

}
