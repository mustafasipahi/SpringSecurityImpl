package com.controller;

import com.dto.UserDto;
import com.dto.UserResponse;
import com.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserDto userDto) {
        return authenticationService.register(userDto);
    }

    @PostMapping("/authenticate")
    public UserResponse authenticate(@RequestBody UserDto userDto) {
        return authenticationService.authenticate(userDto);
    }
}
