package com.kacperkwiatkowski.digitalphonebook.advisor;

import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(InvalidPromptException.class)
    public ResponseEntity<PromptResponse> invalidPromptExceptionHandler(InvalidPromptException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(PromptResponse.builder()
                        .operation(Operation.ERROR)
                        .message(e.getMessage())
                        .build()
                );
    }
}
