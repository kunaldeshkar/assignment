package com.amdocs.media.assignement.authservice.dao;

import com.amdocs.media.assignement.authservice.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserDAO extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByUserName(String userName);

}
