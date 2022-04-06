package com.digiquest.core.battle;

import com.digiquest.core.digimon.Digimon;
import com.github.cfogrady.dcom.digimon.battle.BattleResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BattleResultWrapper {
    private final BattleResult battleResult;
    private final Digimon deviceDigimon;
}
