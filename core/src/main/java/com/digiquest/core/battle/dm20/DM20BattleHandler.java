package com.digiquest.core.battle.dm20;

import com.digiquest.core.battle.BattleHandler;
import com.digiquest.core.battle.BattleResultWrapper;
import com.digiquest.core.battle.DeviceROMWrapper;
import com.digiquest.core.dcom.DComManager;
import com.digiquest.core.digimon.Digimon;
import com.digiquest.core.digimon.DigimonLibrary;
import com.github.cfogrady.dcom.digimon.dm20.DM20IndexEntry;
import com.github.cfogrady.dcom.digimon.dm20.DM20Rom;
import com.github.cfogrady.dcom.digimon.dm20.battle.DM20BattleRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
public class DM20BattleHandler implements BattleHandler {
    private final DComManager dComManager;
    private final DM20BattleRunner dm20BattleRunner;
    private final DM20RomFactory dm20RomFactory;
    private final List<DM20IndexEntry> possibleDeviceDigimon;
    private final DigimonLibrary digimonLibrary;

    public CompletableFuture<BattleResultWrapper> runBattle(Digimon digimon, int attackQuality) {
        DM20Rom rom = dm20RomFactory.createDM20Rom(digimon, attackQuality);
        return runBattle(rom);
    }

    @Override
    public CompletableFuture<BattleResultWrapper> runBattle(Digimon digimon, float attackQuality) {
        DM20Rom rom = dm20RomFactory.createDM20Rom(digimon, attackQuality);
        return runBattle(rom);
    }

    @Override
    public CompletableFuture<BattleResultWrapper> runBattle(DeviceROMWrapper deviceROMWrapper) {
        if(deviceROMWrapper instanceof DM20ROMWrapper) {
            return runBattle(((DM20ROMWrapper) deviceROMWrapper).getDm20Rom());
        } else {
            throw new IllegalArgumentException("DM20BattleHandler needs a DM20ROMWrapper");
        }
    }

    CompletableFuture<BattleResultWrapper> runBattle(DM20Rom rom) {
        log.info("Running Battle Against: Attribute {}, Power {}, Index {}, Attack {}", rom.getFirstDigimon().getAttribute(),
                rom.getFirstDigimon().getPower(), rom.getFirstDigimon().getIndex(), rom.getAttack());
        CompletableFuture<BattleResultWrapper> result = new CompletableFuture<>();
        dComManager.getDComPort().thenAccept(dcomPort -> {
            dm20BattleRunner.startBattle(dcomPort, rom).thenAccept(br -> {
                result.complete(BattleResultWrapper.builder()
                        .battleResult(br)
                        .deviceDigimon(getDigimonFromDeviceIndex(br.getDeviceIndex()))
                        .build());
            });
        });
        return result;
    }

    private Digimon getDigimonFromDeviceIndex(int index) {
        DM20IndexEntry indexEntry = getIndexEntryForIndex(index);
        Digimon digimon = digimonLibrary.searchByHash(indexEntry.getHashedLowerCaseName());
        return digimon == null ? Digimon.builder().name(UNKNOWN_DIGIMON).build() : digimon;
    }

    private DM20IndexEntry getIndexEntryForIndex(int index) {
        if(index < possibleDeviceDigimon.size()) {
            return possibleDeviceDigimon.get(index);
        }
        return null;
    }
}
