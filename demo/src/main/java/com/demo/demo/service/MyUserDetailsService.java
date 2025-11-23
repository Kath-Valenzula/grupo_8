package com.demo.demo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.demo.models.User;
import com.demo.demo.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("=== Intentando cargar usuario: {} ===", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("Usuario no encontrado: {}", username);
            throw new UsernameNotFoundException(username);
        }
        logger.info("Usuario encontrado: {}, password hash: {}...", user.getUsername(), user.getPassword().substring(0, 20));
        return user;
    }

}