package com.amdocs.media.userservice.controller;

import com.amdocs.media.userservice.payload.ProfilePayload;
import com.amdocs.media.userservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewProfile(@RequestBody ProfilePayload profilePayload) {
        try {
            userProfileService.createProfile(profilePayload);
            return new ResponseEntity("Create profile success!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Cannot create profile: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
