package com.sunny.voting_backend.repository;

import com.sunny.voting_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//This repository files are used for data access that's why this is the part of data access layer
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
