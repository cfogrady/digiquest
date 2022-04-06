package com.digiquest.desktop.dcom;

import com.digiquest.core.dcom.DComPortFactory;
import com.digiquest.core.dcom.DComSerialEventPort;
import com.fazecast.jSerialComm.SerialPort;

import java.util.Optional;

public class DesktopDComPortFactory implements DComPortFactory {

    @Override
    public Optional<DComSerialEventPort> createDComPort() {
        return createDComPort(0);
    }

    @Override
    public Optional<DComSerialEventPort> createDComPort(int portNumber) {
        SerialPort[] ports = SerialPort.getCommPorts();
        if(ports == null || ports.length == 0) {
            return Optional.empty();
        }
        DComPort dComPort = new DComPort(ports[portNumber]);
        return Optional.of(dComPort);
    }

    @Override
    public int getConnectedPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        if(ports == null) {
            return 0;
        }
        return ports.length;
    }
}
