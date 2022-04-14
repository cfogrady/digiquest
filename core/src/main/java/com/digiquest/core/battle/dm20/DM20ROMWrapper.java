package com.digiquest.core.battle.dm20;

import com.digiquest.core.battle.DeviceROMWrapper;
import com.github.cfogrady.dcom.digimon.dm20.DM20Rom;
import lombok.Data;

@Data
public class DM20ROMWrapper implements DeviceROMWrapper {
    private final DM20Rom dm20Rom;
}
