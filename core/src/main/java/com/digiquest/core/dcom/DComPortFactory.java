package com.digiquest.core.dcom;

import java.util.Optional;

public interface DComPortFactory {
    Optional<DComSerialEventPort> createDComPort();

    Optional<DComSerialEventPort> createDComPort(int portNumber);

    int getConnectedPorts();
}
