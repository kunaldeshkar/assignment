package com.amdocs.media.assignement.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/assignment")
    public String home() {
        return "Hello from auth-service";
    }


}
