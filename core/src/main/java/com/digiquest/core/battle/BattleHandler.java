package com.digiquest.core.battle;

import com.digiquest.core.digimon.Digimon;

import java.util.concurrent.CompletableFuture;

public interface BattleHandler {
    CompletableFuture<BattleResultWrapper> runBattle(Digimon digimon, float attackQuality);
}
