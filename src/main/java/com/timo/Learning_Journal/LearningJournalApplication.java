package com.timo.Learning_Journal;

import com.timo.Learning_Journal.entity.Entry;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.repositories.EntryRepository;
import com.timo.Learning_Journal.repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LearningJournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningJournalApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            System.out.println("Halo");
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("Timo").build());

        };
    }
}
