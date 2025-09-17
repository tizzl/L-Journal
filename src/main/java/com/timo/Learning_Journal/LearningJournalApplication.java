package com.timo.Learning_Journal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LearningJournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningJournalApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PersonRepository personRepository) {
        return (args) -> {
            System.out.println("Halo");
            personRepository.save(Person.builder().email("a@b.de").password("c").name("Timo").build());

        };
    }
    @Bean
    public CommandLineRunner commandLineRunner2(EntryRepository entryRepository) {
        return (args) -> {
            System.out.println("Halo2");

            entryRepository.save(Entry.builder().title("test").body("What the heck").build());
        };
    }
}
