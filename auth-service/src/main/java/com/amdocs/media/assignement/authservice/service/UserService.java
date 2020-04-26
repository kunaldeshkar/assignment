package com.amdocs.media.assignement.authservice.service;

import com.amdocs.media.assignement.authservice.model.UserCredentials;

public interface UserService {

    UserCredentials findByUserName(String username);

    boolean isValidUser(String username, String userName);

}
