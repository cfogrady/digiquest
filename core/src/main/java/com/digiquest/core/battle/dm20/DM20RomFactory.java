package com.digiquest.core.battle.dm20;

import com.digiquest.core.digimon.Digimon;
import com.github.cfogrady.dcom.digimon.dm20.DM20Operation;
import com.github.cfogrady.dcom.digimon.dm20.DM20Rom;

public class DM20RomFactory {
    public static final int MAX_ATTACK_LEVEL = 14;

    public DM20Rom createDM20Rom(Digimon digimon, float attackLevel) {
        int attack = Math.round(attackLevel * MAX_ATTACK_LEVEL);
        return createDM20Rom(digimon, attack);
    }

    public DM20Rom createDM20Rom(Digimon digimon, int attack) {
        DM20Rom rom = DM20Rom.builder()
                .attack(attack)
                .name(digimon.getName().substring(0, 4).toCharArray())
                .initiator(false)
                .operation(DM20Operation.BATTLE)
                .version(0)
                .firstDigimon(DM20Rom.DigiStats.builder()
                        .attribute(digimon.getAttribute())
                        .index(4)
                        .power(75+16)
                        .strongShot(digimon.getStrongAttackDM20().ordinal())
                        .weakShot(digimon.getWeakAttackDM20().ordinal())
                        .build())
                .build();
        return rom;
    }
}
