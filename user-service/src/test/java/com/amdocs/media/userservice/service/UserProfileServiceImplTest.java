package com.amdocs.media.userservice.service;

import com.amdocs.media.userservice.dao.UserProfileDAO;
import com.amdocs.media.userservice.model.UserProfile;
import com.amdocs.media.userservice.payload.ProfilePayload;
import com.amdocs.media.userservice.service.impl.UserProfileServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserProfileServiceImplTest {

    private UserProfileService userProfileService;
    private UserProfileDAO userProfileDAO;

    @Before
    public void init() {
        userProfileDAO = mock(UserProfileDAO.class);
        userProfileService = new UserProfileServiceImpl(userProfileDAO);
    }

    @Test(expected = RuntimeException.class)
    public void givenProfileAlreadyExisted_whenCreateProfile_thenExpectException() {
        ProfilePayload profilePayload = new ProfilePayload();
        profilePayload.setUserName("username");

        UserProfile existedUser = new UserProfile();
        existedUser.setUserName("username");

        when(userProfileDAO.findByUserName("username")).thenReturn(Optional.of(existedUser));
        userProfileService.createProfile(profilePayload);
    }

    @Test
    public void givenProfileNotExisted_whenCreateProfile_thenExpectSuccess() {
        ProfilePayload profilePayload = new ProfilePayload();
        profilePayload.setUserName("aaa");
        profilePayload.setAddress("bbb");
        profilePayload.setPhoneNumber("123");

        when(userProfileDAO.findByUserName("aaa")).thenReturn(Optional.empty());

        userProfileService.createProfile(profilePayload);

        ArgumentCaptor<UserProfile> profile = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileDAO, times(1)).save(profile.capture());

        UserProfile value = profile.getValue();
        assertEquals("aaa", value.getUserName());
        assertEquals("bbb", value.getAddress());
        assertEquals("123", value.getPhoneNumber());
    }

    @Test
    public void givenProfileNotFound_when_deleteProfile_thenDoNotCallDAO() {
        String username = "aaa";
        when(userProfileDAO.findByUserName(username)).thenReturn(Optional.empty());
        userProfileService.deleteProfile(username);
        verify(userProfileDAO, times(0)).delete(any(UserProfile.class));
    }

    @Test
    public void givenProfileExisted_when_deleteProfile_thenCallDAOToDelete() {
        String username = "aaa";
        when(userProfileDAO.findByUserName(username)).thenReturn(Optional.of(new UserProfile()));
        userProfileService.deleteProfile(username);
        verify(userProfileDAO, times(1)).delete(any(UserProfile.class));
    }

    @Test
    public void givenProfileNotFound_when_UpdateProfile_thenDoNotCallDAO() {
        ProfilePayload profilePayload = new ProfilePayload();
        profilePayload.setUserName("aaa");
        when(userProfileDAO.findByUserName("aaa")).thenReturn(Optional.empty());
        userProfileService.updateProfile(profilePayload);
        verify(userProfileDAO, times(0)).save(any(UserProfile.class));
    }

    @Test
    public void givenProfileExisted_when_UpdateProfile_thenProfileUpdateCorrectly() {
        ProfilePayload profilePayload = new ProfilePayload();
        profilePayload.setUserName("aaa");
        profilePayload.setAddress("bbb");
        profilePayload.setPhoneNumber("123");

        UserProfile oldUser = new UserProfile();
        oldUser.setUserName("aaa");
        oldUser.setAddress("qqq");
        oldUser.setPhoneNumber("999");

        when(userProfileDAO.findByUserName("aaa")).thenReturn(Optional.of(oldUser));

        userProfileService.updateProfile(profilePayload);

        ArgumentCaptor<UserProfile> updatedProfile = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileDAO, times(1)).save(updatedProfile.capture());

        UserProfile value = updatedProfile.getValue();
        assertEquals("aaa", value.getUserName());
        assertEquals("bbb", value.getAddress());
        assertEquals("123", value.getPhoneNumber());
    }
}
