package com.kacperkwiatkowski.digitalphonebook.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromptKeys {

    NAME("name"),
    NUMBER("number");

    private final String value;
}