package com.kacperkwiatkowski.digitalphonebook.dto;

import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptResponse {

    private RecordDto record;
    private Operation operation;
    private String message;

}