package com.amdocs.media.assignement.authservice.service.impl;

import com.amdocs.media.assignement.authservice.model.UserProfile;
import com.amdocs.media.assignement.authservice.payload.ProfilePayload;
import com.amdocs.media.assignement.authservice.service.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);

    private static final String TOPIC = "profile";

    private KafkaTemplate<String, UserProfile> kafkaTemplate;

    @Autowired
    public KafkaProducerServiceImpl(KafkaTemplate<String, UserProfile> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updateProfile(ProfilePayload profilePayload) {
        UserProfile profile = new UserProfile();
        profile.setType("UPDATE");
        profile.setAddress(profilePayload.getAddress());
        profile.setPhoneNumber(profilePayload.getPhoneNumber());
        profile.setUserName(profilePayload.getUserName());

        LOGGER.info("Sending update profile: {} to topic: {}", profile, TOPIC);
        kafkaTemplate.send(TOPIC, profile);
        LOGGER.info("Sending successfully!");
    }

    @Override
    public void deleteProfile(String userName) {
        UserProfile profile = new UserProfile();
        profile.setType("DELETE");
        profile.setUserName(userName);

        LOGGER.info("Sending delete profile with username= {} to topic: {}", userName, TOPIC);
        kafkaTemplate.send(TOPIC, profile);
        LOGGER.info("Sending successfully!");
    }
}
