package com.timo.Learning_Journal.service;

import com.timo.Learning_Journal.entity.Entry;
import com.timo.Learning_Journal.repositories.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;

    public Optional<Entry> findById(Long id) {
        return entryRepository.findById(id);
    }

    public Entry save(Entry entry) {
        return entryRepository.save(entry);
    }

    public List<Entry> findAll() {

        return entryRepository.findAll();
    }
}
