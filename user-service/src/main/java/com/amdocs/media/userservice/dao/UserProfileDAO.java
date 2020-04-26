package com.amdocs.media.userservice.dao;

import com.amdocs.media.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserProfileDAO extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserName(String userName);

}
