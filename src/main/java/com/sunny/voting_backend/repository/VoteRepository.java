package com.sunny.voting_backend.repository;

import com.sunny.voting_backend.model.Candidate;
import com.sunny.voting_backend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;



public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserId(Long userId);
    @Query("SELECT c.name as candidateName, c.party as party, COUNT(v) as voteCount " +
            "FROM Vote v " +
            "JOIN v.candidate c " +
            "GROUP BY c.id, c.name, c.party " +
            "Order BY voteCount DESC")
    List<VoteResult> countVotesByCandidate();
}
