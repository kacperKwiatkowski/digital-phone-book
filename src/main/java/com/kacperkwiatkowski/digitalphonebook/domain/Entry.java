package com.kacperkwiatkowski.digitalphonebook.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "Entry")
@AllArgsConstructor
@NoArgsConstructor
public class Entry {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String number;
}
