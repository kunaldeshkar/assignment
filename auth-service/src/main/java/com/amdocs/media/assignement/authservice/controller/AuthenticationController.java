package com.amdocs.media.assignement.authservice.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    // endpoint to get current logged-in user
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCurrentUser(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        }
        return "Current user is not login yet!";
    }
}
