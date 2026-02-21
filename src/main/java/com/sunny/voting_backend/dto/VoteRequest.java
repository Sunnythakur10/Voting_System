package com.sunny.voting_backend.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull(message = "Candidate ID cannot be null")
    private Long candidateId;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
}
