package com.sunny.voting_backend.controller;

import com.sunny.voting_backend.model.Candidate;
import com.sunny.voting_backend.repository.CandidateRepository;
import com.sunny.voting_backend.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/candidates") // The base URL for this controller
public class CandidateController {

    private final CandidateService candidateService;
    private final CandidateRepository candidateRepository;

    public CandidateController(CandidateService candidateService, CandidateRepository candidateRepository) {
        this.candidateService = candidateService;
        this.candidateRepository = candidateRepository;
    }

    // POST request to create a candidate
    // URL: http://localhost:8080/api/candidates/create
    @PostMapping("/create")
    public ResponseEntity<Candidate> createCandidate(@Valid @RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateService.addCandidate(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }

    // GET request to fetch all candidates
    // URL: http://localhost:8080/api/candidates/all
    @GetMapping("/all")
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Candidate is available at this id"));
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id){
        Candidate candidate = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidate);
    }

}