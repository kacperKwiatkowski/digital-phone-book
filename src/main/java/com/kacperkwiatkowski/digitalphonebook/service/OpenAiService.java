package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;

public interface OpenAiService {

    StructuredCommand interpret(String userInput);
}
