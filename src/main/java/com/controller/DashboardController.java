package com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping
    public String dashboard() {
        return "Welcome Dashboard Page";
    }
}
