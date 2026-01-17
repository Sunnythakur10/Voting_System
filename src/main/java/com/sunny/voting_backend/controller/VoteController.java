package com.sunny.voting_backend.controller;


import com.sunny.voting_backend.dto.VoteRequest;
import com.sunny.voting_backend.model.Vote;
import com.sunny.voting_backend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
public class VoteController {


    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }


    @PostMapping
    public ResponseEntity<Vote> castVote(@RequestBody VoteRequest voteRequest){
        Vote vote = voteService.castVote(voteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(vote);
    }
}
