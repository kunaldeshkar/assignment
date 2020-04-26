package com.amdocs.media.userservice.controller;

import com.amdocs.media.userservice.payload.ProfilePayload;
import com.amdocs.media.userservice.service.UserProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UserProfileControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private UserProfileService userProfileService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();

    }

    @Test
    public void givenProfilePayloadAndServiceExecuteSuccess_whenCreateNewProfile_thenExpectedOKStatus() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"user\",\n" +
                "\"phoneNumber\":\"0111111111\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";

        ArgumentCaptor<ProfilePayload> profile = ArgumentCaptor.forClass(ProfilePayload.class);
        mockMvc.perform(
                post("/profile")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userProfileService, times(1)).createProfile(profile.capture());

        ProfilePayload payload = profile.getValue();
        assertEquals("user", payload.getUserName());
        assertEquals("0111111111", payload.getPhoneNumber());
        assertEquals("Abc Def", payload.getAddress());
    }

    @Test
    public void givenServiceExecuteError_whenCreateNewProfile_thenExpectedStatusCode400() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"user\",\n" +
                "\"phoneNumber\":\"0111111111\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";

        doThrow(new RuntimeException("Any error")).when(userProfileService).createProfile(any());
        mockMvc.perform(
                post("/profile")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }
}