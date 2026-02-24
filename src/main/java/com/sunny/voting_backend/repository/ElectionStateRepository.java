package com.sunny.voting_backend.repository;


import com.sunny.voting_backend.model.ElectionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ElectionStateRepository extends JpaRepository<ElectionState , Long> {
}
