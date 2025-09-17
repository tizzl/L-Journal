package com.timo.Learning_Journal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByRole(Role role);

    Person findByEmail(String email);

    String email(String email);
}
