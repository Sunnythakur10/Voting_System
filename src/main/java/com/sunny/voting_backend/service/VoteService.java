package com.sunny.voting_backend.service;

import com.sunny.voting_backend.dto.VoteRequest;
import com.sunny.voting_backend.exception.UserAlreadyVotedException;
import com.sunny.voting_backend.model.*;
import com.sunny.voting_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final ElectionStateRepository electionStateRepository;
    @Autowired
    public VoteService(CandidateRepository candidateRepository, UserRepository userRepository, VoteRepository voteRepository, ElectionStateRepository electionStateRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;

        this.voteRepository = voteRepository;
        this.electionStateRepository = electionStateRepository;
    }


    @Transactional
    public Vote castVote(VoteRequest request, String currentUsername){

        ElectionState electionState = electionStateRepository.findById(1L).orElseThrow(()->new RuntimeException("Election State is not initialized yet!"));
        LocalDateTime currentTime = LocalDateTime.now();

        if(currentTime.isBefore(electionState.getStartTime())){
            throw  new RuntimeException("Election is not Started yet!!");
        }
        if(currentTime.isAfter(electionState.getEndTime())){
            throw new RuntimeException("Election is already ended");
        }


        //fetching user detail
        User user = userRepository.findByUsername(currentUsername).orElseThrow(()->new RuntimeException("User not found"));

        //fetching the candidate detail
        Candidate candidate = candidateRepository. findByIdAndStatus(request.getCandidateId() , ApplicationStatus.APPROVED).orElseThrow(()-> new RuntimeException("candidate not found"));

        //Checking the logic
        if(voteRepository.existsByUserId(user.getId())){
            throw new UserAlreadyVotedException("User has already voted!!");

        }

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setCandidate(candidate);

        user.setHasVoted(true);

        return voteRepository.save(vote);

    }

    public List<VoteResult> getResults() {
        return voteRepository.countVotesByCandidate();
    }


}
