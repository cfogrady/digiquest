package com.digiquest.core.battle;

import com.digiquest.core.digimon.Digimon;

import java.util.concurrent.CompletableFuture;

public interface BattleHandler {
    public static final String UNKNOWN_DIGIMON = "Unknownmon";

    CompletableFuture<BattleResultWrapper> runBattle(Digimon digimon, float attackQuality);
    CompletableFuture<BattleResultWrapper> runBattle(DeviceROMWrapper deviceROMWrapper);
}
