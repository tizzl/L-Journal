package com.timo.Learning_Journal.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String email;
    private String name;
    private String password;
    private String adminCode;


    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Course> coursesByTeacher = new ArrayList<>();
    // TODO beide courses relogisieren

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

    //Name readable machen, hoffentlich
    @Override
    public String toString(){
        return name;
    }
}
