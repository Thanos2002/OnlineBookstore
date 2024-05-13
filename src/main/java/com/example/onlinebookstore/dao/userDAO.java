package com.example.onlinebookstore.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.onlinebookstore.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface userDAO extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}