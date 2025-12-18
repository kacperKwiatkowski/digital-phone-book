package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.mapper.EntryMapper;
import com.kacperkwiatkowski.digitalphonebook.domain.Entry;
import com.kacperkwiatkowski.digitalphonebook.repository.EntryRepository;
import com.kacperkwiatkowski.digitalphonebook.validator.EntryValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final EntryMapper entryMapper;
    private final EntryValidator entryValidator;

    public PromptResponse create(Map<String, String> data) {

        entryValidator.isEntryNonExistent(data);

        Entry entry = entryRepository.save(entryMapper.fromDtoToEntry(data));
        EntryDto entryDto = entryMapper.fromEntityToDto(entry);
        return PromptResponse.builder()
                .entry(entryDto)
                .operation(Operation.CREATE)
                .build();
    }

    public PromptResponse find(Map<String, String> data) {
        entryValidator.doesEntryExistsInRepository(data);
        entryValidator.isEntryUnique(data);

        Entry entry = entryRepository.findByNameOrNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue()));

        return PromptResponse.builder()
                .entry(entryMapper.fromEntityToDto(entry))
                .operation(Operation.READ)
                .build();
    }

    public PromptResponse update(Map<String, String> data) {
        entryValidator.isOneOfTheDataValuesPresent(data);
        entryValidator.doesEntryExistsInRepository(data);
        entryValidator.isEntryUnique(data);

        Entry entry = entryRepository.findByNameOrNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue()));

        if(!data.get(PromptKeys.NAME.getValue()).isEmpty()) entry.setName(data.get(PromptKeys.NAME.getValue()));
        if(!data.get(PromptKeys.NUMBER.getValue()).isEmpty()) entry.setNumber(data.get(PromptKeys.NUMBER.getValue()));

        return PromptResponse.builder()
                .entry(entryMapper.fromEntityToDto(entryRepository.save(entry)))
                .operation(Operation.UPDATE)
                .build();
    }

    public PromptResponse delete(Map<String, String> data) {
        entryValidator.doesEntryExistsInRepository(data);
        entryValidator.isEntryUnique(data);

        Entry entry = entryRepository.findByNameOrNumber(data.get(PromptKeys.NAME.getValue()), PromptKeys.NUMBER.getValue());
        entryRepository.delete(entry);

        return PromptResponse.builder()
                .entry(entryMapper.fromEntityToDto(entry))
                .operation(Operation.DELETE)
                .build();
    }

    @Override
    public List<EntryDto> findAll() {
        return entryRepository.findAll().stream().map(entryMapper::fromEntityToDto).toList();
    }
}
