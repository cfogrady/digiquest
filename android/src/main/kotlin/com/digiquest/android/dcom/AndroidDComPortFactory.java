package com.digiquest.android.dcom;

import com.digiquest.core.dcom.DComPortFactory;
import com.digiquest.core.dcom.DComSerialEventPort;

import java.util.Optional;

public class AndroidDComPortFactory implements DComPortFactory {
    @Override
    public Optional<DComSerialEventPort> createDComPort() {
        return Optional.empty();
    }

    @Override
    public Optional<DComSerialEventPort> createDComPort(int portNumber) {
        return Optional.empty();
    }

    @Override
    public int getConnectedPorts() {
        return 0;
    }
}
