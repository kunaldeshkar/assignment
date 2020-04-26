package com.amdocs.media.assignement.authservice.service.impl;

import com.amdocs.media.assignement.authservice.dao.UserDAO;
import com.amdocs.media.assignement.authservice.model.UserCredentials;
import com.amdocs.media.assignement.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    public static final String USER = "USER";

    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials userCredentials = findByUserName(username);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(USER));
        return new User(userCredentials.getUserName(), userCredentials.getPassword(), authorities);

    }

    @Override
    public UserCredentials findByUserName(String username) {
        return userDAO.findByUserName(username).get();
    }

    @Override
    public boolean isValidUser(String currentUserName, String updateUserName) {
        Optional<UserCredentials> userCredentials = userDAO.findByUserName(currentUserName);
        if (!userCredentials.isPresent()) {
            return false; // user invalid
        }

        if (!currentUserName.equals(updateUserName)) {
            return false;
        }

        return true;
    }
}
