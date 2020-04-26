package com.amdocs.media.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentController {

    @GetMapping("/assignment")
    public String home() {
        return "Hello from user-service";
    }

}
