package com.example.onlinebookstore.dao;


import java.util.Optional;

import com.example.onlinebookstore.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.onlinebookstore.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserprofileDAO extends JpaRepository<UserProfile, Integer> {

    Optional<UserProfile> findByUsername(String username);
    Optional<UserProfile> findById(int id);
}
