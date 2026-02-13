package com.sunny.voting_backend.repository;

public interface VoteResult {
    String getCandidateName();
    String getParty();
    Long getVoteCount();

}
