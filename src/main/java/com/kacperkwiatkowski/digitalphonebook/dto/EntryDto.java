package com.kacperkwiatkowski.digitalphonebook.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryDto {
//    private Long id;
    private String name;
    private String number;
}
