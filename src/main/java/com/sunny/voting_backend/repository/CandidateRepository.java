package com.sunny.voting_backend.repository;

import com.sunny.voting_backend.model.ApplicationStatus;
import com.sunny.voting_backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    List<Candidate> findByStatus(ApplicationStatus status);


    Optional<Candidate> findByIdAndStatus(Long id, ApplicationStatus status);
}
