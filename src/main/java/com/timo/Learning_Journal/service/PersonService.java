package com.timo.Learning_Journal.service;


import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person findByEmail(String formemail) {

        return personRepository.findByEmail(formemail);

    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);

    }
}
