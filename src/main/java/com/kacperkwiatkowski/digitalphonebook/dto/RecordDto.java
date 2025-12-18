package com.kacperkwiatkowski.digitalphonebook.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {
    private String name;
    private String number;
}
