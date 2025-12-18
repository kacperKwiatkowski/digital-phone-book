package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;

public interface OpenAiService {

    public StructuredCommand interpret(String userInput);
}
