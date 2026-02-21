package com.sunny.voting_backend.controller;


import com.sunny.voting_backend.dto.VoteRequest;
import com.sunny.voting_backend.dto.VoteResponse;
import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.model.Vote;
import com.sunny.voting_backend.repository.VoteResult;
import com.sunny.voting_backend.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {


    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }


    @PostMapping("/cast")
    public ResponseEntity<VoteResponse> castVote(@Valid @RequestBody VoteRequest voteRequest , Principal principal){
        // 1. Service does the hard work, returns the Entity
        String currentUsername = principal.getName();
        Vote vote = voteService.castVote(voteRequest , currentUsername);

        // 2. CONTROLLER converts Entity -> DTO (The Translation Layer)
        VoteResponse voteResponse = new VoteResponse(vote.getId() ,vote.getUser().getUsername() , vote.getCandidate().getName() , vote.getCandidate().getParty() , vote.getTimestamp() );
        return ResponseEntity.status(HttpStatus.CREATED).body(voteResponse);
    }

    @GetMapping("/results")
    public ResponseEntity<List<VoteResult>> getCandidate(){
         List<VoteResult> result = voteService.getResults();
         return ResponseEntity.ok(result);
    }
}
