package com.amdocs.media.assignement.authservice.service;

import com.amdocs.media.assignement.authservice.model.UserProfile;
import com.amdocs.media.assignement.authservice.payload.ProfilePayload;
import com.amdocs.media.assignement.authservice.service.impl.KafkaProducerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class KafkaProducerServiceImplTest {

    KafkaProducerService kafkaProducerService;
    KafkaTemplate<String, UserProfile> kafkaTemplate;

    @Before
    public void init() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaProducerService = new KafkaProducerServiceImpl(kafkaTemplate);
    }


    @Test
    public void testDeleteProfile() {
        String username = "anyusername";
        kafkaProducerService.deleteProfile(username);

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserProfile> profile = ArgumentCaptor.forClass(UserProfile.class);
        verify(kafkaTemplate, times(1)).send(topic.capture(), profile.capture());

        assertEquals("profile", topic.getValue());
        assertEquals("anyusername", profile.getValue().getUserName());
        assertEquals("DELETE", profile.getValue().getType());
    }

    @Test
    public void testUpdateProfile() {
        ProfilePayload payload = new ProfilePayload();
        payload.setAddress("aaa");
        payload.setUserName("bbb");
        payload.setPhoneNumber("123");
        kafkaProducerService.updateProfile(payload);

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserProfile> profile = ArgumentCaptor.forClass(UserProfile.class);
        verify(kafkaTemplate, times(1)).send(topic.capture(), profile.capture());

        assertEquals("profile", topic.getValue());
        UserProfile profileVal = profile.getValue();
        assertEquals("bbb", profileVal.getUserName());
        assertEquals("UPDATE", profileVal.getType());
        assertEquals("aaa", profileVal.getAddress());
        assertEquals("123", profileVal.getPhoneNumber());
    }
}
