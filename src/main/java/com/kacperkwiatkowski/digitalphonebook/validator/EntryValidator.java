package com.kacperkwiatkowski.digitalphonebook.validator;

import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.repository.EntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class EntryValidator {

    private final EntryRepository entryRepository;

    public void isOneOfTheDataValuesPresent(Map<String, String> data) {
        if(data.entrySet().stream().allMatch(entry -> entry.getValue()==null || entry.getValue().isEmpty())){
            throw new InvalidPromptException("Missing at least one value.");
        }
    }


    public void doesEntryExistsInRepository(Map<String, String> data) {
        if(!entryRepository.existsByNameOrNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue()))) {
            throw new InvalidPromptException("Non existent entry.");
        }
    }

    public void isEntryNonExistent(Map<String, String> data) {
        if(entryRepository.countByNameAndNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue())) != 0){
            throw new InvalidPromptException("Such entry already exists");
        }
    }

    public void isEntryUnique(Map<String, String> data) {
        if(entryRepository.countByNameOrNumber((data.get(PromptKeys.NAME.getValue())), data.get(PromptKeys.NUMBER.getValue()))!=1){
            throw new InvalidPromptException("Non unique entry.");
        }
    }
}
