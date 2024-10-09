package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.*;
import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import com.example.accountmicroservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
@ControllerExceptionHandler
@ApiResponse(responseCode = "40*", description = "Ошибка в запросе",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)) )
@Tag(name = "Authentication Controller", description = "API для аутентификации и управления токенами")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация нового пользователя", description = "Позволяет создать новую учетную запись")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))

    @PostMapping("/SignUp")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse register(@RequestBody SignUpRequest request) {

        authenticationService.signUp(request);
        return new MessageResponse("Вы успешно зарегистрировались!");
    }


    @Operation(summary = "Вход пользователя", description = "Позволяет пользователю войти в систему с использованием учетных данных")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно вошел в систему",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))

    @PostMapping("/SignIn")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody SignInRequest request) {

        return authenticationService.signIn(request);
    }


    @Operation(summary = "Выход пользователя", description = "Позволяет пользователю выйти из системы")
    @PreAuthorize("isAuthenticated()")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно вышел из системы",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))

    @PostMapping("/SignOut")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse logout(HttpServletRequest request){

        authenticationService.signOut(request);
        return new MessageResponse("Вы успешно вышли из системы!");
    }


    @Operation(summary = "Проверка валидности токена", description = "Позволяет проверить, валиден ли переданный токен")
    @ApiResponse(responseCode = "200", description = "Результат проверки токена",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))

    @GetMapping("/Validate")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse validate(@RequestParam String accessToken){

        Boolean isValid = authenticationService.validate(accessToken);
        return new MessageResponse(isValid ? "Токен валиден" : "Токен не валиден");
    }


    @Operation(summary = "Обновление токена", description = "Позволяет обновить access-токен с помощью refresh-токена")
    @ApiResponse(responseCode = "200", description = "Новый access-токен",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))

    @PostMapping("/Refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse refresh(@RequestBody RefreshRequest refreshToken){

        return authenticationService.refresh(refreshToken.getRefreshToken());
    }

}
