package com.amdocs.media.assignement.authservice.service;

import com.amdocs.media.assignement.authservice.payload.ProfilePayload;

public interface KafkaProducerService {

    void updateProfile(ProfilePayload profilePayload);

    void deleteProfile(String userName);
}
