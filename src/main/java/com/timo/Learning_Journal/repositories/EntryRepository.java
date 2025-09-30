package com.timo.Learning_Journal.repositories;

import com.timo.Learning_Journal.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByAuthorId(Long authorId);
    Long id(Long id);
}


