package com.timo.Learning_Journal.entity;

import jakarta.persistence.*;
import lombok.*;


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
    private List<Integer> coursesID;
    private String password;
    private String adminCode;


    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private Set<Entry> entries;

    @ManyToMany
    @JoinTable(
            name = "person courses", // Name der Zwischentabelle
            joinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Course> courses;

    //Name readable machen, hoffentlich
    @Override
    public String toString(){
        return name;
    }
}
