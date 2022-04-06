package com.digiquest.core.battle.dm20;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.cfogrady.dcom.digimon.Attribute;
import com.github.cfogrady.dcom.digimon.Stage;
import com.github.cfogrady.dcom.digimon.dm20.DM20IndexEntry;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = DM20IndexEntryDTO.DM20IndexEntryDTOBuilder.class)
public class DM20IndexEntryDTO {
    @JsonProperty
    private final int index;
    @JsonProperty
    private final Stage stage;
    @JsonProperty
    private final Attribute attribute;
    @JsonProperty
    private final int power;
    @JsonProperty
    private final int hashedLowerCaseName;

    public DM20IndexEntry toDM20IndexEntry() {
        return DM20IndexEntry.builder()
                .index(index)
                .stage(stage)
                .attribute(attribute)
                .power(power)
                .hashedLowerCaseName(hashedLowerCaseName)
                .build();
    }
}
