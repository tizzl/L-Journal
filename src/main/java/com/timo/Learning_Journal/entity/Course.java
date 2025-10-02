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
    private String courseDescription;

    @ManyToOne
    private Person teacher; //Teacher/Admin

    @ManyToMany
    @JoinTable(
            name = "courses persons", // Name der Zwischentabelle
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn (name = "person_id"))
    private Set<Person> students = new HashSet<>();

}
