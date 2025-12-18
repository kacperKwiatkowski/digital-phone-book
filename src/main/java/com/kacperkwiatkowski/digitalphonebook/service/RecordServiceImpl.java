package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.domain.Record;
import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.mapper.RecordMapper;
import com.kacperkwiatkowski.digitalphonebook.repository.RecordRepository;
import com.kacperkwiatkowski.digitalphonebook.validator.RecordValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;
    private final RecordValidator recordValidator;

    public PromptResponse create(Map<String, String> data) {

        recordValidator.isRecordNonExistent(data);

        Record record = recordRepository.save(recordMapper.fromDtoToRecord(data));
        RecordDto recordDto = recordMapper.fromEntityToDto(record);
        return PromptResponse.builder()
                .record(recordDto)
                .operation(Operation.CREATE)
                .build();
    }

    public PromptResponse find(Map<String, String> data) {
        recordValidator.doesRecordExistsInRepository(data);
        recordValidator.isRecordUnique(data);

        return PromptResponse.builder()
                .record(recordMapper.fromEntityToDto(recordRepository.findByNameOrNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue()))))
                .operation(Operation.READ)
                .build();
    }

    public PromptResponse update(Map<String, String> data) {
        recordValidator.isOneOfTheDataValuesPresent(data);
        recordValidator.doesRecordExistsInRepository(data);
        recordValidator.isRecordUnique(data);

        Record recordToUpdate = recordRepository.findByNameOrNumber(data.get(PromptKeys.NAME.getValue()), data.get(PromptKeys.NUMBER.getValue()));

        if(!data.get(PromptKeys.NAME.getValue()).isEmpty()) recordToUpdate.setName(data.get(PromptKeys.NAME.getValue()));
        if(!data.get(PromptKeys.NUMBER.getValue()).isEmpty()) recordToUpdate.setNumber(data.get(PromptKeys.NUMBER.getValue()));

        return PromptResponse.builder()
                .record(recordMapper.fromEntityToDto(recordRepository.save(recordToUpdate)))
                .operation(Operation.UPDATE)
                .build();
    }

    public PromptResponse delete(Map<String, String> data) {
        recordValidator.doesRecordExistsInRepository(data);
        recordValidator.isRecordUnique(data);

        Record recordToDelete = recordRepository.findByNameOrNumber(data.get(PromptKeys.NAME.getValue()), PromptKeys.NUMBER.getValue());
        recordRepository.delete(recordToDelete);

        return PromptResponse.builder()
                .record(recordMapper.fromEntityToDto(recordToDelete))
                .operation(Operation.DELETE)
                .build();
    }

    @Override
    public List<RecordDto> findAll() {
        return recordRepository.findAll().stream().map(recordMapper::fromEntityToDto).toList();
    }
}
