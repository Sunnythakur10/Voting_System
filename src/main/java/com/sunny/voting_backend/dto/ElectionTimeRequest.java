package com.sunny.voting_backend.dto;

import java.time.LocalDateTime;

public class ElectionTimeRequest {
    private String electionName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEnd_time() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
