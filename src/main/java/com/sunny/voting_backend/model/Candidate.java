package com.sunny.voting_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String party;

    @Column(nullable = false)
    private int voteCount;

    @Column(nullable = false)
    @Min(value=18 , message = "Candidate must be above 18 year")
    private int age;

    public Candidate(){

    }

    public Candidate(String name , String party, int age){
        this.name = name;
        this.party = party;
        this.age = age;
        this.voteCount = 0;

    }

    public Long getId(){
        return id;
    }
    public void setId(long id){
        this.id= id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getParty(){
        return party;
    }
    public void setParty(String party){
        this.party = party;
    }

    public Integer getVoteCount(){
        return voteCount;
    }
    public void setVoteCount(int voteCount){
        this.voteCount = voteCount;
    }


    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age=age;
    }
}
