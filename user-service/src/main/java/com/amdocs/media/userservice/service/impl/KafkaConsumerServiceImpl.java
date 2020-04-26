package com.amdocs.media.userservice.service.impl;

import com.amdocs.media.userservice.payload.ProfilePayload;
import com.amdocs.media.userservice.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);
    public static final String UPDATE_ACTION = "UPDATE";
    public static final String DELETE_ACTION = "DELETE";

    @Autowired
    public KafkaConsumerServiceImpl(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    private UserProfileService userProfileService;

    @KafkaListener(topics = "profile")
    public void consume(ProfilePayload message) {
        LOGGER.info("Message received: {}", message);
        String messageType = message.getType();
        if (UPDATE_ACTION.equals(messageType)) {
            userProfileService.updateProfile(message);
        } else if (DELETE_ACTION.equals(messageType)) {
            userProfileService.deleteProfile(message.getUserName());
        } else {
            LOGGER.warn("Invalid message type: {}, ignore update or delete", messageType);
        }
    }
}
