package com.digiquest.core.battle.dm20;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cfogrady.dcom.digimon.dm20.DM20IndexEntry;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DM20IndexPowerFactory {
    public static final String FILE_NAME = "DM20Indexes.json";

    private final ObjectMapper objectMapper;

    private List<DM20IndexEntry> powerByIndex = null;

    public List<DM20IndexEntry> getPowerByIndex() {
        if(powerByIndex == null) {
            initializeFromResouces();
        }
        return powerByIndex;
    }

    public void initializeFromResouces() {
        try(InputStream inputStream = ClassLoader.getSystemResourceAsStream(FILE_NAME)) {
            List<DM20IndexEntryDTO> dtoEntries = objectMapper.readValue(inputStream, new TypeReference<>() {});
            powerByIndex = dtoEntries.stream().map(DM20IndexEntryDTO::toDM20IndexEntry).collect(Collectors.toList());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
}
