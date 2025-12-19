package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;

import java.util.List;
import java.util.Map;

public interface RecordService {
    PromptResponse create(Map<String, String> data);

    PromptResponse find(Map<String, String> data);

    PromptResponse update(Map<String, String> data);

    PromptResponse delete(Map<String, String> data);

    List<RecordDto> findAll();
}
