package com.sunny.voting_backend.service;

import com.sunny.voting_backend.dto.SignupRequest;
import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignupRequest signupRequest){


        User savedUser = new User();
        savedUser.setUsername(signupRequest.getUsername());
        savedUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        savedUser.setRole(signupRequest.getRole());
        return userRepository.save(savedUser);

    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){userRepository.deleteById(id);}

    public User getUserById(Long id){return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));}

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isHasVoted()) {
            throw new RuntimeException("Cannot delete account: You have already cast a vote in the active election.");
        }
        userRepository.deleteByUsername(username);

    }
}
