package com.sunny.voting_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
    @Table(name = "election_state")

public class ElectionState {
    @Id
    private Long id=1L;

    @Column(name="election_name")

    private String electionName;

    @Column(name="start_time")
    private LocalDateTime startTime;

    public ElectionState() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("electionName")
    public String getElectionName() {
        return electionName;
    }
    @JsonProperty("electionName")
    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Column(name="end_time")
    private LocalDateTime endTime;


}
