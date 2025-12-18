package com.kacperkwiatkowski.digitalphonebook.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InvalidPromptException extends RuntimeException {
    private String message;
}
