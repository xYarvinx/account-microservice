package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Doctors")
@AllArgsConstructor
@ControllerExceptionHandler
public class DoctorController {
}
