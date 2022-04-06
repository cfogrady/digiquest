package com.digiquest.core;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Data
public class PlatformProperties {
    private final String relativeLocation;
    private final boolean capableOfAddingToLibrary;
}
