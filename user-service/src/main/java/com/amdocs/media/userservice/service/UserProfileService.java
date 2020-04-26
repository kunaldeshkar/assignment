package com.amdocs.media.userservice.service;

import com.amdocs.media.userservice.payload.ProfilePayload;

public interface UserProfileService {
    void updateProfile(ProfilePayload message);

    void deleteProfile(String userName);

    void createProfile(ProfilePayload profilePayload);
}
