package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;

import java.util.List;
import java.util.Map;

public interface EntryService {
    public PromptResponse create(Map<String, String> data);
    public PromptResponse find(Map<String, String> data);
    public PromptResponse update(Map<String, String> data);
    public PromptResponse delete(Map<String, String> data);
    public List<EntryDto> findAll();
}
