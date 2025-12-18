package com.kacperkwiatkowski.digitalphonebook.dto;

import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StructuredCommand {

    private Operation operation;
    private String entity;
    private Map<String, String> data;
}