package com.kacperkwiatkowski.digitalphonebook.mapper;

import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.domain.Entry;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EntryMapper {

    public Entry fromDtoToEntry(Map<String, String> data) {
        return Entry.builder()
                .name(data.get("name"))
                .number(data.get("number"))
                .build();
    }

    public EntryDto fromEntityToDto(Entry entry) {
        return EntryDto.builder()
                .name(entry.getName())
                .number(entry.getNumber())
                .build();
    }
}
