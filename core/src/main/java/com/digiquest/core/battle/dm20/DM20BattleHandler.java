package com.digiquest.core.battle.dm20;

import com.digiquest.core.battle.BattleHandler;
import com.digiquest.core.battle.BattleResultWrapper;
import com.digiquest.core.dcom.DComManager;
import com.digiquest.core.digimon.Digimon;
import com.digiquest.core.digimon.DigimonLibrary;
import com.github.cfogrady.dcom.digimon.dm20.DM20IndexEntry;
import com.github.cfogrady.dcom.digimon.dm20.DM20Rom;
import com.github.cfogrady.dcom.digimon.dm20.battle.DM20BattleRunner;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class DM20BattleHandler implements BattleHandler {
    private final DComManager dComManager;
    private final DM20BattleRunner dm20BattleRunner;
    private final DM20RomFactory dm20RomFactory;
    private final List<DM20IndexEntry> possibleDeviceDigimon;
    private final DigimonLibrary digimonLibrary;

    @Override
    public CompletableFuture<BattleResultWrapper> runBattle(Digimon digimon, float attackQuality) {
        DM20Rom rom = dm20RomFactory.createDM20Rom(digimon, attackQuality);
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
        return indexEntry == null ? null : digimonLibrary.searchByHash(indexEntry.getHashedLowerCaseName());
    }

    private DM20IndexEntry getIndexEntryForIndex(int index) {
        if(index < possibleDeviceDigimon.size()) {
            return possibleDeviceDigimon.get(index);
        }
        return null;
    }
}
