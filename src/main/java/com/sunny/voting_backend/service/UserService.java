package com.sunny.voting_backend.service;

import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){userRepository.deleteById(id);}

    public User getUserById(Long id){return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));}
}
