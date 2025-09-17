package com.timo.Learning_Journal;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entries")


public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate date;
    private String body;

    @ManyToOne
    @JoinColumn(name = "author_id") //Fremdschluessel in der Entry-Tabelle
    private Person author;
}
