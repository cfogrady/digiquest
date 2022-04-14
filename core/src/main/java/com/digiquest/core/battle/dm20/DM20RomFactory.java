package com.digiquest.core.battle.dm20;

import com.digiquest.core.digimon.Digimon;
import com.github.cfogrady.dcom.digimon.Attribute;
import com.github.cfogrady.dcom.digimon.Stage;
import com.github.cfogrady.dcom.digimon.dm20.DM20Attack;
import com.github.cfogrady.dcom.digimon.dm20.DM20IndexEntry;
import com.github.cfogrady.dcom.digimon.dm20.DM20Operation;
import com.github.cfogrady.dcom.digimon.dm20.DM20Rom;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DM20RomFactory {
    public static final int MAX_ATTACK_LEVEL = 14;

    private final Map<Stage, List<DM20IndexEntry>> indexEntriesByStage;

    public DM20RomFactory(List<DM20IndexEntry> dm20IndexEntries) {
        indexEntriesByStage = buildMapFromIndexEntryList(dm20IndexEntries);
    }

    public static Map<Stage, List<DM20IndexEntry>> buildMapFromIndexEntryList(List<DM20IndexEntry> dm20IndexEntries) {
        HashMap<Stage, List<DM20IndexEntry>> indexEntriesByStage = new HashMap<>();
        dm20IndexEntries.forEach(entry -> {
            if(!indexEntriesByStage.containsKey(entry.getStage())) {
                List<DM20IndexEntry> stageList = new ArrayList<>();
                stageList.add(entry);
                indexEntriesByStage.put(entry.getStage(), stageList);
            } else {
                indexEntriesByStage.get(entry.getStage()).add(entry);
            }
        });
        // Order the entries
        for(Stage stage : indexEntriesByStage.keySet()) {
            indexEntriesByStage.get(stage).sort(Comparator.comparingInt(DM20IndexEntry::getPower));
        }
        return indexEntriesByStage;
    }

    public Set<Integer> getPowersAtStage(Stage stage) {
        return indexEntriesByStage.get(stage).stream().map(DM20IndexEntry::getPower).collect(Collectors.toSet());
    }

    public DM20IndexEntry getIndexForStageAndPower(Stage stage, int power) {
        Optional<DM20IndexEntry> matchingIndexEntry = indexEntriesByStage.get(stage).stream().filter(entry -> entry.getPower() == power).findFirst();
        if(matchingIndexEntry.isEmpty()) {
            throw new IllegalArgumentException("Given power is not valid for any monster on the DM20");
        }
        return matchingIndexEntry.get();
    }

    public DM20IndexEntry getIndexForStageAndRelativePower(Stage stage, double relativePower) {
        if(relativePower > 1.0 || relativePower < 0.0) {
            throw new IllegalArgumentException("Relative power out of bounds. Must be between 0 and 1");
        }
        List<DM20IndexEntry> entriesForStage = indexEntriesByStage.get(stage);
        int index = (int) Math.round((entriesForStage.size()-1) * relativePower);
        return entriesForStage.get(index);
    }

    public DM20Rom createDM20Rom(Digimon digimon, float attackLevel) {
        int attack = Math.round(attackLevel * MAX_ATTACK_LEVEL);
        return createDM20Rom(digimon, attack);
    }

    public DM20Rom createDM20Rom(Digimon digimon, int attack, int strength) {
        DM20IndexEntry indexEntry = getIndexForStageAndRelativePower(digimon.getStage(), digimon.getDefaultStageStrength());
        return createDM20Rom(digimon.getName(), digimon.getStage(), digimon.getAttribute(), indexEntry, attack, strength, digimon.getStrongAttackDM20(), digimon.getWeakAttackDM20());
    }

    public DM20Rom createDM20Rom(String name, Stage stage, Attribute attribute, DM20IndexEntry indexEntry, int attack, int strength, DM20Attack strongAttack, DM20Attack weakAttack) {
        int strengthPower = strength * 4;
        DM20Rom rom = DM20Rom.builder()
                .attack(attack)
                .name(name.substring(0, 4).toCharArray())
                .initiator(false)
                .operation(DM20Operation.BATTLE)
                .version(0)
                .firstDigimon(DM20Rom.DigiStats.builder()
                        .attribute(attribute)
                        .index(indexEntry.getIndex())
                        .power(indexEntry.getPower() + strengthPower)
                        .strongShot(strongAttack.ordinal())
                        .weakShot(weakAttack.ordinal())
                        .build())
                .build();
        return rom;
    }
}
