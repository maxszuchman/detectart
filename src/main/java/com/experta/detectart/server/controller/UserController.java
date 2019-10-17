package com.experta.detectart.server.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.experta.detectart.server.exception.ResourceNotFoundException;
import com.experta.detectart.server.model.User;
import com.experta.detectart.server.repository.UserRepository;

@RestController
public class UserController {

    public static final String USERS = "/users";

    @Autowired
    private UserRepository userRepository;

    @GetMapping(USERS)
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(USERS + "/{userId}")
    public User getUser(@PathVariable final Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PostMapping(USERS)
    public User createUser(@Valid @RequestBody final User user) {
        return userRepository.save(user);
    }

    @PutMapping(USERS + "/{userId}")
    public User updateUser(@PathVariable final Long userId, @Valid @RequestBody final User userRequest) {

        return userRepository.findById(userId).map(user -> {

            userRequest.copyInto(user);
            return userRepository.save(user);

        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }


    @DeleteMapping(USERS + "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable final Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + userId + " not found"));
    }

}