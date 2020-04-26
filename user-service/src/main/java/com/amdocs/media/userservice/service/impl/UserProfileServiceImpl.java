package com.amdocs.media.userservice.service.impl;

import com.amdocs.media.userservice.dao.UserProfileDAO;
import com.amdocs.media.userservice.model.UserProfile;
import com.amdocs.media.userservice.payload.ProfilePayload;
import com.amdocs.media.userservice.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private UserProfileDAO userProfileDAO;

    @Autowired
    public UserProfileServiceImpl(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }


    @Override
    public void updateProfile(ProfilePayload profile) {
        String userName = profile.getUserName();
        Optional<UserProfile> userOptional = userProfileDAO.findByUserName(userName);
        if (userOptional.isPresent()) {
            UserProfile userProfile = userOptional.get();
            userProfile.setAddress(profile.getAddress());
            userProfile.setPhoneNumber(profile.getPhoneNumber());
            userProfileDAO.save(userProfile);
            LOGGER.info("Profile has been updated successfully: {}", userProfile);
        } else {
            LOGGER.error("Profile does not exists: userName={}, ignore the request...", userName);
        }
    }

    @Override
    public void deleteProfile(String userName) {
        Optional<UserProfile> userProfile = userProfileDAO.findByUserName(userName);
        if (userProfile.isPresent()) {
            userProfileDAO.delete(userProfile.get());
            LOGGER.info("Profile has been deleted successfully: {}", userName);
        } else {
            LOGGER.error("Profile does not exists: userName={}, ignore the request...", userName);
        }

    }

    @Override
    public void createProfile(ProfilePayload profilePayload) {
        String userName = profilePayload.getUserName();
        Optional<UserProfile> existedUser = userProfileDAO.findByUserName(userName);
        if (existedUser.isPresent()) {
            throw new RuntimeException("Profile with username=" + userName + " already existed in the system.");
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(userName);
        userProfile.setAddress(profilePayload.getAddress());
        userProfile.setPhoneNumber(profilePayload.getPhoneNumber());
        userProfileDAO.save(userProfile);
        LOGGER.info("Profile has been created successfully: {}", userProfile);

    }
}
