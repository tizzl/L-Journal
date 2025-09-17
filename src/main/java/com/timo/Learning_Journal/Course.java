package com.timo.Learning_Journal;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToMany(mappedBy = "courses")
    private Set<Person> persons;

}
