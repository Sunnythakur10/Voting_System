package com.sunny.voting_backend.service;

import com.sunny.voting_backend.model.Candidate;
import com.sunny.voting_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    // Constructor Injection: Spring automatically gives us the Repository instance
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    // Method to save a new candidate
    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    // Method to get all candidates
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public void deleteCandidate(Long id){
        candidateRepository.deleteById(id);
    }

    public Candidate getCandidateById(Long id){
        return candidateRepository.findById(id).orElseThrow(()-> new RuntimeException("Candidate is not found"));
    }
}
