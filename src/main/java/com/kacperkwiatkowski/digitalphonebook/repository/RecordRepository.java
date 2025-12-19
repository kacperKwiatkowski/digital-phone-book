package com.kacperkwiatkowski.digitalphonebook.repository;

import com.kacperkwiatkowski.digitalphonebook.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findByNameOrNumber(String name, String number);

    boolean existsByNameAndNumber(String name, String number);

    boolean existsByNameOrNumber(String name, String number);

    int countByNameAndNumber(String name, String number);

    int countByNameOrNumber(String name, String number);

}
