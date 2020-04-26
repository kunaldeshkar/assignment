package com.amdocs.media.assignement.authservice.controller;

import com.amdocs.media.assignement.authservice.payload.ProfilePayload;
import com.amdocs.media.assignement.authservice.service.KafkaProducerService;
import com.amdocs.media.assignement.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserProfileGatewayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileGatewayController.class);

    @Value("${user-service.host}")
    private String userServiceHost;

    @Value("${user-service.profileEndpoint}")
    private String profileEndpoint;

    @Autowired
    private UserService userService;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewProfile(@RequestBody ProfilePayload profilePayload, Authentication authentication) {
        String currentUsername = authentication.getName();
        String updateUserName = profilePayload.getUserName();
        if (!userService.isValidUser(currentUsername, updateUserName)) {
            return new ResponseEntity(String.format("Your current login user is: %s, and you have no permission to create profile for: %s", currentUsername, updateUserName), HttpStatus.UNAUTHORIZED);
        }

        LOGGER.info("Calling user-profile-service to create new profile...");
        ResponseEntity<String> response = sendRequestToUserService(profilePayload);
        return new ResponseEntity(response.getBody(), response.getStatusCode());

    }

    private ResponseEntity<String> sendRequestToUserService(ProfilePayload profilePayload) {
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String createProfileUrl = userServiceHost + profileEndpoint;
        try {
            HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString
                    (profilePayload), headers);
            return restTemplate.postForEntity(createProfileUrl, request, String.class);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/profile")
    public ResponseEntity<String> updateProfile(@RequestBody ProfilePayload profilePayload, Authentication authentication) {
        String currentUsername = authentication.getName();
        String updateUserName = profilePayload.getUserName();
        if (!userService.isValidUser(currentUsername, updateUserName)) {
            return new ResponseEntity(String.format("Your current login user is: %s, and you have no permission to update profile for: %s", currentUsername, updateUserName), HttpStatus.UNAUTHORIZED);
        }

        LOGGER.info("Sending UPDATE event with profile data to Kafka...");
        kafkaProducerService.updateProfile(profilePayload);
        return new ResponseEntity("UPDATE event has been sent successfully to Kafka to further process. Username to be updated: " + profilePayload.getUserName(), HttpStatus.OK);

    }

    @DeleteMapping(value = "/profile")
    public ResponseEntity<String> deleteProfile(Authentication authentication) {
        LOGGER.info("Sending DELETE event with profile data to Kafka...");
        String userName = authentication.getName();
        kafkaProducerService.deleteProfile(userName);
        return new ResponseEntity("DELETE event has been sent successfully to Kafka to further process. Username to be deleted: " + userName, HttpStatus.OK);
    }
}
