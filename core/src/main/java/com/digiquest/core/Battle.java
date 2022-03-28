package com.digiquest.core;

import com.github.cfogrady.dcom.serial.EventDrivenSerialPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Battle {
    private final EventDrivenSerialPort eventDrivenSerialPort;
}
