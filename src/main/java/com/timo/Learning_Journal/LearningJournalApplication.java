package com.timo.Learning_Journal;

import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Role;
import com.timo.Learning_Journal.repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication(scanBasePackages = "com.timo.Learning_Journal")
public class LearningJournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningJournalApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            System.out.println("Halo");
            personRepository.save(Person.builder().email("c@c.de").password(passwordEncoder.encode("c")).name("Timo").role(Role.TEACHER).build());
            personRepository.save(Person.builder().email("a@bz.de").password(passwordEncoder.encode("c")).name("peter").role(Role.STUDENT).build());
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("hans").role(Role.STUDENT).build());
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("horst").role(Role.STUDENT).build());
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("falko").role(Role.STUDENT).build());
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("kolja").role(Role.STUDENT).build());
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("david").role(Role.STUDENT).build());
            personRepository.save(Person.builder().email("a@b.de").password(passwordEncoder.encode("c")).name("marcel").role(Role.STUDENT).build());


        };
    }

}
