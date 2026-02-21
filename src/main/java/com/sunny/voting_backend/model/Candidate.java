package com.sunny.voting_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Candidate party is required")
    @Column(nullable = false)
    private String party;

    @Column(nullable = false)
    @Min(value=18 , message = "Candidate must be above 18 year")
    private int age;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;



    public Candidate(){

    }

    public Candidate(String name , String party, int age){
        this.name = name;
        this.party = party;
        this.age = age;


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


    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age=age;
    }
    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}

