package com.timo.Learning_Journal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseID;

    private String courseName;

    @ManyToOne
    private Person teacher; //Teacher/Admin

    @ManyToMany(mappedBy = "courses")
    private Set<Person> students = new HashSet<>();

}
