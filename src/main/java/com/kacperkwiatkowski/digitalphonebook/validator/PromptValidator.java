package com.kacperkwiatkowski.digitalphonebook.validator;

import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PromptValidator {

    public void isDataAvailable(StructuredCommand cmd) {
        if(cmd.getData()==null){
            throw new InvalidPromptException("No data passed in the prompt");
        }
    }

    public void areKeysValid(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            if (!key.equals(PromptKeys.NAME.getValue()) && !key.equals(PromptKeys.NUMBER.getValue())) {
                throw new InvalidPromptException("Invalid keys.");
            }
        }
    }

    public void isAnyOfTheDataValuesPresent(Map<String, String> data) {
        if(data.entrySet().stream().allMatch(entry -> entry.getValue()==null || entry.getValue().isEmpty())){
            throw new InvalidPromptException("Missing all values.");
        }
    }
}
