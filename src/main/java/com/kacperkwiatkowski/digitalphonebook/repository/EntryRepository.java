package com.kacperkwiatkowski.digitalphonebook.repository;

import com.kacperkwiatkowski.digitalphonebook.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    boolean existsByName(String name);

    boolean existsByNumber(String number);

    long countByName(String name);

    long countByNumber(String number);

    Entry findByNameOrNumber(String name, String number);

    boolean existsByNameAndNumber(String name, String number);

    int countByNameAndNumber(String name, String number);

    int countByNameOrNumber(String name, String number);

    boolean existsByNameOrNumber(String name, String number);
}
