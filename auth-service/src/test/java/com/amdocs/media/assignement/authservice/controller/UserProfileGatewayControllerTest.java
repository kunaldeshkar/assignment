package com.amdocs.media.assignement.authservice.controller;

import com.amdocs.media.assignement.authservice.payload.ProfilePayload;
import com.amdocs.media.assignement.authservice.service.KafkaProducerService;
import com.amdocs.media.assignement.authservice.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UserProfileGatewayControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private Set<GrantedAuthority> authorities;

    @MockBean
    UserService userService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    KafkaProducerService kafkaProducerService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
    }

    // TEST case for unauthorized user (not login yet)
    @Test
    public void givenUnauthorizedUser_whenCreateProfile_thenExpectForbidden() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"user\",\n" +
                "\"phoneNumber\":\"0888888888\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";
        mockMvc.perform(
                post("/profile")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void givenUnauthorizedUser_whenUpdateProfile_thenExpectForbidden() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"user\",\n" +
                "\"phoneNumber\":\"0888888888\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";
        mockMvc.perform(
                put("/profile")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void givenUnauthorizedUser_whenDeleteProfile_thenExpectForbidden() throws Exception {
        mockMvc.perform(
                delete("/profile")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    // Authorized User (login already) but request CRUD on invalid user
    @Test
    public void givenAuthorizedUser_whenCreateDifferentProfile_thenExpectNoPermission() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"incorrectuser\",\n" +
                "\"phoneNumber\":\"1111111111\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";

        when(userService.isValidUser("logginuser", "incorrectuser")).thenReturn(false);
        mockMvc.perform(
                post("/profile").with(authentication(new UsernamePasswordAuthenticationToken("logginuser", "123456", authorities)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

        verify(userService, times(1)).isValidUser("logginuser", "incorrectuser");
        verify(restTemplate, times(0)).postForEntity(anyString(), any(HttpEntity.class), any());
    }

    @Test
    public void givenAuthorizedUser_whenUpdateDifferentProfile_thenExpectNoPermission() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"incorrectuser\",\n" +
                "\"phoneNumber\":\"1111111111\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";

        when(userService.isValidUser("logginuser", "incorrectuser")).thenReturn(false);
        mockMvc.perform(
                put("/profile").with(authentication(new UsernamePasswordAuthenticationToken("logginuser", "123456", authorities)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

        verify(userService, times(1)).isValidUser("logginuser", "incorrectuser");
        verify(restTemplate, times(0)).postForEntity(anyString(), any(HttpEntity.class), any());
    }


    // SUCCESS cases
    @Test
    public void givenAuthorizedUser_whenCreateProfile_thenExpectSuccess() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"correctuser\",\n" +
                "\"phoneNumber\":\"1111111111\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";

        when(userService.isValidUser("correctuser", "correctuser")).thenReturn(true);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), any())).thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        mockMvc.perform(
                post("/profile").with(authentication(new UsernamePasswordAuthenticationToken("correctuser", "123456", authorities)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).isValidUser("correctuser", "correctuser");

        // verify calling to user-service to add new profile
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), any());

    }

    @Test
    public void givenAuthorizedUser_whenUpdateProfile_thenExpectSuccess() throws Exception {
        String requestJson = "{\n" +
                "\"userName\":\"correctuser\",\n" +
                "\"phoneNumber\":\"1111111111\",\n" +
                "\"address\":\"Abc Def\"\n" +
                "}";

        when(userService.isValidUser("correctuser", "correctuser")).thenReturn(true);

        mockMvc.perform(
                put("/profile").with(authentication(new UsernamePasswordAuthenticationToken("correctuser", "123456", authorities)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).isValidUser("correctuser", "correctuser");

        // verify that the update profile is send to kafka
        verify(kafkaProducerService, times(1)).updateProfile(any(ProfilePayload.class));

    }

    @Test
    public void givenAuthorizedUser_whenDeleteProfile_thenExpectSuccess() throws Exception {
        mockMvc.perform(
                delete("/profile").with(authentication(new UsernamePasswordAuthenticationToken("correctuser", "123456", authorities)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        // verify that the update profile is send to kafka
        verify(kafkaProducerService, times(1)).deleteProfile("correctuser");
    }
}
