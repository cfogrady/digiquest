package com.digiquest.core.digimon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DigimonLibrary {
    public static final String RELATIVE_LOCATION = "/digimon_library.json";

    private final ObjectMapper objectMapper;
    //private final SpriteLoader spriteLoader;
    private final String userLocation;

    @Getter
    private Map<String, Digimon> digimonByNames;
    @Getter
    @Setter
    private Digimon currentDigimon;

    @Getter
    private Map<String, String> existingCredits = new HashMap<>();

    public void initializeLibrary() {
        File file = new File(userLocation + RELATIVE_LOCATION);
        if(file.exists()) {
            try(FileInputStream inputStream = new FileInputStream(file)) {
                List<Digimon> digimon = objectMapper.readValue(inputStream, new TypeReference<>() {});
                digimonByNames = digimon.stream().collect(Collectors.toMap(d -> d.getName().toLowerCase(), d->d));
                initializeExistingCredits();
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        } else {
            initializeEmptyLibrary();
        }
    }

    public List<Digimon> getSortedDigimon() {
        return digimonByNames.values().stream()
                .sorted(Comparator.comparing(Digimon::getName))
                .collect(Collectors.toList());
    }

    public void saveLibrary(OutputStream outputStream) {
        try {
            objectMapper.writeValue(outputStream, digimonByNames.values());
            // re-initialize in case new credits were added.
            initializeExistingCredits();
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public Digimon searchByHash(int hashcode) {
        for(Digimon digimon : digimonByNames.values()) {
            if(digimon.getName().toLowerCase().hashCode() == hashcode) {
                return digimon;
            }
        }
        return null;
    }

    private void initializeExistingCredits() {
        existingCredits.clear();
        for(Digimon digimon : digimonByNames.values()) {
            if(digimon.getArtCredit() != null) {
                existingCredits.put(digimon.getArtCredit().toLowerCase(), digimon.getArtCredit());
            }
            if(digimon.getDigimonEntryCredits() != null) {
                for(String entryCredit : digimon.getDigimonEntryCredits()) {
                    existingCredits.put(entryCredit.toLowerCase(), entryCredit);
                }
            }
        }
    }

    private void exportDigimonLibrary(File file) {


    }

    public void initializeEmptyLibrary() {
        digimonByNames = new HashMap<>();
    }

    public boolean isLoaded() {
        return digimonByNames != null;
    }

    public void addDigimon(Digimon digimon) {
        digimonByNames.put(digimon.getName().toLowerCase(Locale.ROOT), digimon);
    }

}
