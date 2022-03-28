package com.digiquest.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.cfogrady.dcom.digimon.Attribute;
import com.github.cfogrady.dcom.digimon.Stage;
import com.github.cfogrady.dcom.digimon.dm20.DM20Attack;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Digimon.DigimonBuilder.class)
public class Digimon {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String dubName;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final Attribute attribute;
    @JsonProperty
    private final Stage stage;
    @JsonProperty
    private final double defaultStageStrength;
    @JsonProperty
    private final String artCredit;
    @JsonProperty
    private final Set<String> digimonEntryCredits;
    @JsonProperty
    private final DM20Attack strongAttackDM20;
    @JsonProperty
    private final DM20Attack weakAttackDM20;
}
