package com.kacperkwiatkowski.digitalphonebook.dto;

import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptResponse {

    private EntryDto entry;
    private Operation operation;
    private String message;

}