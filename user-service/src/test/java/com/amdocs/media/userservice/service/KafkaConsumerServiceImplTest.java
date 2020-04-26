package com.amdocs.media.userservice.service;

import com.amdocs.media.userservice.payload.ProfilePayload;
import com.amdocs.media.userservice.service.impl.KafkaConsumerServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class KafkaConsumerServiceImplTest {
    private KafkaConsumerServiceImpl consumerService;
    private UserProfileService userProfileService;


    @Before
    public void init() {
        userProfileService = mock(UserProfileService.class);
        consumerService = new KafkaConsumerServiceImpl(userProfileService);
    }

    @Test
    public void givenUpdateAction_whenReceiveEvent_thenCallUpdateMethod() {
        ProfilePayload message = new ProfilePayload();
        message.setUserName("aaa");
        message.setType("UPDATE");
        consumerService.consume(message);
        verify(userProfileService, times(1)).updateProfile(message);
    }

    @Test
    public void givenDeleteAction_whenReceiveEvent_thenCallDeleteMethod() {
        ProfilePayload message = new ProfilePayload();
        message.setUserName("aaa");
        message.setType("DELETE");
        consumerService.consume(message);
        verify(userProfileService, times(1)).deleteProfile(message.getUserName());
    }
}
