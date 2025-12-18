package com.kacperkwiatkowski.digitalphonebook.mapper;

import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import com.kacperkwiatkowski.digitalphonebook.domain.Record;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RecordMapper {

    public Record fromDtoToRecord(Map<String, String> data) {
        return Record.builder()
                .name(data.get("name"))
                .number(data.get("number"))
                .build();
    }

    public RecordDto fromEntityToDto(Record record) {
        return RecordDto.builder()
                .name(record.getName())
                .number(record.getNumber())
                .build();
    }
}
