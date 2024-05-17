package com.example.onlinebookstore.service.User;

import java.util.Optional;


import com.example.onlinebookstore.model.UserProfile;
import com.example.onlinebookstore.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.onlinebookstore.dao.*;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.service.UserProfile.UserProfileService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private userDAO userDAO;

    @Autowired
    private UserprofileDAO userprofileDAO;

    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDAO.save(user);
    }


    @Override
    public boolean isUserPresent(User user) {
        Optional<User> storedUser = userDAO.findByUsername(user.getUsername());
        return storedUser.isPresent();
    }

    @Override
    public Optional<User> findById(String username) {
        Optional<User> storedUser = userDAO.findByUsername(username);
        return storedUser;
    }


    // Method defined in Spring Security UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // orElseThrow method of Optional container that throws an exception if Optional result  is null
        return userDAO.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(
                        String.format("USER_NOT_FOUND %s", username)
                ));
    }
}