package com.kacperkwiatkowski.digitalphonebook.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "Record")
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String number;
}
