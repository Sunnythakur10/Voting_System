package com.sunny.voting_backend.controller;


import com.sunny.voting_backend.model.ApplicationStatus;
import com.sunny.voting_backend.model.Candidate;
import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.repository.CandidateRepository;
import com.sunny.voting_backend.repository.UserRepository;
import com.sunny.voting_backend.service.CandidateService;
import com.sunny.voting_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final CandidateRepository candidateRepository;
    private final CandidateService candidateService;
    private final UserRepository userRepository;

    public AdminController(UserService userService, CandidateRepository candidateRepository, CandidateService candidateService, UserRepository userRepository) {
        this.userService = userService;
        this.candidateRepository = candidateRepository;
        this.candidateService = candidateService;
        this.userRepository = userRepository;
    }

    // GET /api/admin/users
    @GetMapping("/users")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    // DELETE /api/admin/candidates/1
    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Candidate not found"));
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/admin/candidates/1/approve
    @PutMapping("/candidates/{id}/approve")
    public ResponseEntity<Candidate> approveCandidate(@PathVariable Long id){
        Candidate updateCandidate = candidateRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Candidate not found"));
        updateCandidate.setStatus(ApplicationStatus.APPROVED);
        candidateRepository.save(updateCandidate);
        return ResponseEntity.ok(updateCandidate);
    }

    // PUT /api/admin/candidates/1/reject
    @PutMapping("/candidates/{id}/reject")
    public ResponseEntity<Candidate> rejectCandidate(@PathVariable Long id){
        Candidate rejectCandidate = candidateRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Candidate not found"));
        rejectCandidate.setStatus(ApplicationStatus.REJECTED);
        candidateRepository.save(rejectCandidate);
        return ResponseEntity.ok(rejectCandidate);
    }

    @GetMapping("/candidates/pending")

    public ResponseEntity<List<Candidate>> getPendingCandidate(){
        List<Candidate> pendingCandidate = candidateService.getAllCandidates(ApplicationStatus.PENDING);
        return ResponseEntity.ok(pendingCandidate);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("The id is not found"));
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}