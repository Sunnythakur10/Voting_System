package com.sunny.voting_backend.service;

import com.sunny.voting_backend.dto.VoteRequest;
import com.sunny.voting_backend.model.Candidate;
import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.model.Vote;
import com.sunny.voting_backend.repository.CandidateRepository;
import com.sunny.voting_backend.repository.UserRepository;
import com.sunny.voting_backend.repository.VoteRepository;
import com.sunny.voting_backend.repository.VoteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    @Autowired
    public VoteService(CandidateRepository candidateRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;

        this.voteRepository = voteRepository;
    }



    public Vote castVote(VoteRequest Request){

        //fetching user detail
        User user = userRepository.findById(Request.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

        //fetching the candidate detail
        Candidate candidate = candidateRepository.findById(Request.getCandidateId()).orElseThrow(()-> new RuntimeException("candidate not found"));

        //Checking the logic
        if(voteRepository.existsByUserId(user.getId())){
            throw new RuntimeException("User has already voted!!");

        }

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setCandidate(candidate);

        return voteRepository.save(vote);

    }

    public List<VoteResult> getResults() {
        return voteRepository.countVotesByCandidate();
    }


}
