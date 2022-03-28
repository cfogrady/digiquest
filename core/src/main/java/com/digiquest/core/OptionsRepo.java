package com.digiquest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@RequiredArgsConstructor
@Slf4j
public class OptionsRepo {
    private final ObjectMapper objectMapper;

    @Getter
    private OptionsState optionsState;

    public void loadOptions(InputStream inputStream) throws IOException {
        this.optionsState = objectMapper.readValue(inputStream, OptionsState.class);
    }

    public void loadOptions(File file) {
        if(file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                loadOptions(inputStream);
            } catch (IOException ioe) {
                log.error("Unable to load options!");
                throw new UncheckedIOException(ioe);
            }
        } else {
            optionsState = OptionsState.builder().useDubNames(false).build();
        }
    }

    public void saveOptions(OutputStream outputStream) throws IOException {
        objectMapper.writeValue(outputStream, optionsState);
    }
}
