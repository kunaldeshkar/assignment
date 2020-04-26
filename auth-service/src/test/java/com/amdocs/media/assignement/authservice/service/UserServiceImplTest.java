package com.amdocs.media.assignement.authservice.service;

import com.amdocs.media.assignement.authservice.dao.UserDAO;
import com.amdocs.media.assignement.authservice.model.UserCredentials;
import com.amdocs.media.assignement.authservice.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserDAO userDAO;

    @Before
    public void init() {
        userDAO = mock(UserDAO.class);
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    public void testIsValidUser() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUserName("myuser");
        Mockito.when(userDAO.findByUserName(anyString())).thenReturn(Optional.of(userCredentials));

        boolean validUser = userService.isValidUser("myuser", "myuser");
        assertTrue(validUser);
        validUser = userService.isValidUser("myuser", "diffuser");
        assertFalse(validUser);
    }

    @Test
    public void testFindUserName() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUserName("myuser");
        when(userDAO.findByUserName(anyString())).thenReturn(Optional.of(userCredentials));

        String myuser = "myuser";
        UserCredentials byUserName = userService.findByUserName(myuser);
        verify(userDAO, times(1)).findByUserName("myuser");
        assertEquals(byUserName, userCredentials);
    }

    @Test
    public void testLoadUserName() {
        UserCredentials user = new UserCredentials();
        user.setUserName("myuser");
        user.setPassword("mypassword");
        Mockito.when(userDAO.findByUserName(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("myuser");
        assertEquals("myuser", userDetails.getUsername());
        assertEquals("mypassword", userDetails.getPassword());
        assertEquals("USER", userDetails.getAuthorities().iterator().next().getAuthority());

    }
}
