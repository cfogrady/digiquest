package com.digiquest.core.digimon;

import com.digiquest.core.util.ByteSequenceMatcher;
import com.digiquest.core.util.RingBuffer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
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

    public void saveLibrary() {
        File file = new File(userLocation + RELATIVE_LOCATION);
        try {
            objectMapper.writeValue(file, digimonByNames.values());
            // re-initialize in case new credits were added.
            initializeExistingCredits();
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    static final byte[] END_JSON = {'E', 'N', 'D', 0, 0};

    public CompletableFuture<Void> exportLibrary(String fileStr, SpritePackager spritePackager) {
        File file = new File(fileStr);
        ObjectWriter streamWriter = objectMapper.writerFor(Digimon.class).without(StreamWriteFeature.AUTO_CLOSE_TARGET);
        CompletableFuture<Void> exportTask = CompletableFuture.runAsync(() -> {
            try(DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file))) {
                outputStream.writeInt(digimonByNames.size());
                for(Digimon digimon : digimonByNames.values()) {
                    log.info("Exporting Digimon {}", digimon.getName());
                    streamWriter.writeValue((OutputStream) outputStream, digimon);
                    outputStream.write(END_JSON);
                    spritePackager.addSpriteToStream(digimon.getName(), outputStream);
                    log.info("Done Exporting Digimon {}", digimon.getName());
                }
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });
        return exportTask;
    }

    private static final byte[] PNG_END = {'I', 'E', 'N', 'D', (byte) 0xae, (byte) 0x42, (byte) 0x60, (byte) 0x82};

    public CompletableFuture<Void> importLibrary(String fileStr, SpritePackager spritePackager) {
        File file = new File(fileStr);
        ObjectReader streamReader = objectMapper.readerFor(Digimon.class).without(StreamReadFeature.AUTO_CLOSE_SOURCE);
        return CompletableFuture.runAsync(() -> {
            Map<String, Digimon> importedDigimon = new HashMap<>();
            try(DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                int size = inputStream.readInt();
                log.info("Imported Library contains {} digimon", size);
                for(int i = 0; i < size; i++) {
                    byte[] json = readUntilSignature(inputStream, END_JSON);
                    json = Arrays.copyOf(json, json.length - END_JSON.length);
                    Digimon digimon = streamReader.readValue(json, Digimon.class);
                    log.info("Import: {}", digimon.getName());
                    byte[] image = readUntilSignature(inputStream, PNG_END);
                    // log.info("{} {} {} {}", Integer.toHexString(inputStream.read()), Integer.toHexString(inputStream.read()), Integer.toHexString(inputStream.read()), Integer.toHexString(inputStream.read()));
                    spritePackager.importSpriteFromStream(digimon.getName(), new ByteArrayInputStream(image));
                    importedDigimon.put(digimon.getName().toLowerCase(), digimon);
                }
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
            digimonByNames = importedDigimon;
            saveLibrary();
        });
    }

    private byte[] readUntilSignature(InputStream inputStream, byte[] signature) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteSequenceMatcher byteSequenceMatcher = new ByteSequenceMatcher(signature);
        boolean dataRemains = true;
        byte[] buffer = new byte[1024];
        while(dataRemains) {
            inputStream.mark(1024);
            int readBytes = inputStream.read(buffer);
            if(readBytes == -1) {
                throw new IllegalArgumentException("Signature Never Found!");
            }
            int sequenceEnd = byteSequenceMatcher.checkBytes(buffer, readBytes);
            if(readBytes != buffer.length && sequenceEnd == -1) {
                throw new IllegalArgumentException("Signature Never Found!");
            }
            if(sequenceEnd != -1) {
                inputStream.reset();
                readBytes = inputStream.read(buffer, 0, sequenceEnd+1);
                dataRemains = false;
            }
            byteArrayOutputStream.write(buffer, 0, readBytes);
        }
        return byteArrayOutputStream.toByteArray();
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
