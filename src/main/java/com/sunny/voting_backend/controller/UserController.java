package com.sunny.voting_backend.controller;

import com.sunny.voting_backend.model.User;


import com.sunny.voting_backend.repository.UserRepository;
import com.sunny.voting_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

//    @PostMapping("/create")
//    public ResponseEntity<User> createUser(@RequestBody User user){
//        User savedUser = userService.RegisterUser(user);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
//    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("The id is not found"));
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<String> deleteMyAccount(Principal principal){
        String currentUsername = principal.getName();
        userService.deleteUserByUsername(currentUsername);
        return ResponseEntity.ok("Account Deleted Successfully");
    }

}
