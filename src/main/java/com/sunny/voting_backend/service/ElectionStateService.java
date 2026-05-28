package com.sunny.voting_backend.service;

import com.sunny.voting_backend.dto.ElectionTimeRequest;
import com.sunny.voting_backend.model.ElectionState;
import com.sunny.voting_backend.repository.ElectionStateRepository;
import org.springframework.stereotype.Service;


@Service
public class ElectionStateService {
    private final ElectionStateRepository electionStateRepository;

    public ElectionStateService(ElectionStateRepository electionStateRepository) {
        this.electionStateRepository = electionStateRepository;
    }


    public ElectionState configureElection(ElectionTimeRequest request){
        ElectionState electionState = electionStateRepository.findById(1L).orElseThrow(()->new RuntimeException("Election State is not initialized yet!"));

        if(request.getEnd_time().isBefore(request.getStartTime())){
            throw new RuntimeException("End time cannot come before Start time!");
        }

        electionState.setStartTime(request.getStartTime());
        electionState.setEndTime(request.getEnd_time());
        electionState.setElectionName(request.getElectionName());

        return electionStateRepository.save(electionState);

    }


}
