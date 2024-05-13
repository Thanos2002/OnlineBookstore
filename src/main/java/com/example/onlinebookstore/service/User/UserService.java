package com.example.onlinebookstore.service.User;

import org.springframework.stereotype.Service;

import com.example.onlinebookstore.model.User;

import java.util.Optional;

@Service
public interface UserService {
    public void saveUser(User user);
    public boolean isUserPresent(User user);
    public Optional<User> findById(String username);

}
