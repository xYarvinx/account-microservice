package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.AccountResponse;
import com.example.accountmicroservice.dto.ErrorResponse;
import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import com.example.accountmicroservice.models.AccountEntity;
import com.example.accountmicroservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Doctors")
@AllArgsConstructor
@ControllerExceptionHandler
@Tag(name = "Doctor Controller", description = "API для управления данными о докторах")
@SecurityRequirement(name = "bearerAuth")
@ApiResponse(responseCode = "40*", description = "Ошибка в запросе",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)) )
public class DoctorController {
    private final AccountService accountService;

    @Operation(summary = "Получение списка докторов", description = "Позволяет получить список докторов с фильтрацией по имени и ограничением выборки.")
    @ApiResponse(responseCode = "200", description = "Список докторов успешно получен",
            content = @Content(schema = @Schema(implementation = AccountEntity.class)))

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<AccountResponse> getDoctors(
            @Parameter(description = "Фильтр по имени доктора", example = "John", required = true)
            @RequestParam String nameFilter,

            @Parameter(description = "Начальный индекс выборки", example = "0", required = true)
            @RequestParam Integer from,

            @Parameter(description = "Количество записей для выборки", example = "10", required = true)
            @RequestParam Integer count){

        return accountService.getDoctors(nameFilter,from,count);
    }


    @Operation(summary = "Получение данных о конкретном докторе", description = "Позволяет получить информацию о докторе по его идентификатору.")
    @ApiResponse(responseCode = "200", description = "Информация о докторе успешно получена",
            content = @Content(schema = @Schema(implementation = AccountEntity.class)))

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AccountResponse getDoctor(
            @Parameter(description = "Идентификатор доктора", example = "1", required = true)
            @PathVariable Long id){

        return accountService.getDoctor(id);
    }
}
