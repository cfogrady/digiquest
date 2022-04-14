package com.digiquest.core.battle.dm20;

import com.github.cfogrady.dcom.digimon.Stage;
import com.github.cfogrady.dcom.digimon.dm20.DM20IndexEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DM20RomFactoryTest {
    @Test
    void testThatConstructorOrdersElements() {
        // GIVEN
        List<DM20IndexEntry> indexEntryList = new ArrayList<>();
        indexEntryList.add(DM20IndexEntry.builder().index(0).stage(Stage.ADULT).power(10).build());
        indexEntryList.add(DM20IndexEntry.builder().index(1).stage(Stage.ADULT).power(11).build());
        indexEntryList.add(DM20IndexEntry.builder().index(2).stage(Stage.ADULT).power(8).build());
        indexEntryList.add(DM20IndexEntry.builder().index(3).stage(Stage.ADULT).power(7).build());
        indexEntryList.add(DM20IndexEntry.builder().index(4).stage(Stage.ADULT).power(9).build());
        indexEntryList.add(DM20IndexEntry.builder().index(5).stage(Stage.PERFECT).power(20).build());
        indexEntryList.add(DM20IndexEntry.builder().index(6).stage(Stage.PERFECT).power(19).build());
        // WHEN
        Map<Stage, List<DM20IndexEntry>> indexEntryMap = DM20RomFactory.buildMapFromIndexEntryList(indexEntryList);
        // THEN
        List<DM20IndexEntry> adultEntries = indexEntryMap.get(Stage.ADULT);
        Assertions.assertEquals(3, adultEntries.get(0).getIndex(), "Order isn't correct");
        Assertions.assertEquals(2, adultEntries.get(1).getIndex(), "Order isn't correct");
        Assertions.assertEquals(4, adultEntries.get(2).getIndex(), "Order isn't correct");
        Assertions.assertEquals(0, adultEntries.get(3).getIndex(), "Order isn't correct");
        Assertions.assertEquals(1, adultEntries.get(4).getIndex(), "Order isn't correct");
        List<DM20IndexEntry> perfectEntries = indexEntryMap.get(Stage.PERFECT);
        Assertions.assertEquals(6, perfectEntries.get(0).getIndex(), "Order isn't correct");
        Assertions.assertEquals(5, perfectEntries.get(1).getIndex(), "Order isn't correct");
    }
}
