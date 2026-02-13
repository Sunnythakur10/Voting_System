package com.sunny.voting_backend.dto;

import java.time.LocalDateTime;

public class VoteResponse {
    private Long id;
    private String user;
    private String candidate;
    private String party;
    private LocalDateTime timeStamp;

    public VoteResponse(Long id, String user, String candidate, String party, LocalDateTime timeStamp) {
        this.id = id;
        this.user = user;
        this.candidate = candidate;
        this.party = party;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
