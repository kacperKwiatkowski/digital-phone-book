package com.kacperkwiatkowski.digitalphonebook.validator;

import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.repository.RecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class RecordValidator {

    private final RecordRepository recordRepository;

    public void isOneOfTheDataValuesPresent(Map<String, String> data) {
        if(data.entrySet().stream().allMatch(entry -> entry.getValue()==null || entry.getValue().isEmpty())){
            throw new InvalidPromptException("Missing at least one value.");
        }
    }


    public void doesRecordExistsInRepository(Map<String, String> data) {
        if(!recordRepository.existsByNameOrNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue()))) {
            throw new InvalidPromptException("Non existent record.");
        }
    }

    public void isRecordNonExistent(Map<String, String> data) {
        if(recordRepository.countByNameAndNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue())) != 0){
            throw new InvalidPromptException("Such record already record");
        }
    }

    public void isRecordUnique(Map<String, String> data) {
        if(recordRepository.countByNameOrNumber((data.get(PromptKeys.NAME.getValue())), data.get(PromptKeys.NUMBER.getValue()))!=1){
            throw new InvalidPromptException("Non unique record.");
        }
    }
}
