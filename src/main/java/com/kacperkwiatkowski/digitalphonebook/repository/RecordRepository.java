package com.kacperkwiatkowski.digitalphonebook.repository;

import com.kacperkwiatkowski.digitalphonebook.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

    boolean existsByName(String name);

    boolean existsByNumber(String number);

    long countByName(String name);

    long countByNumber(String number);

    Record findByNameOrNumber(String name, String number);

    boolean existsByNameAndNumber(String name, String number);

    int countByNameAndNumber(String name, String number);

    int countByNameOrNumber(String name, String number);

    boolean existsByNameOrNumber(String name, String number);
}
