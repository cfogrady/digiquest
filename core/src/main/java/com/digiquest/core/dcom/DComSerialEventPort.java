package com.digiquest.core.dcom;

import com.github.cfogrady.dcom.serial.EventDrivenSerialPort;

public interface DComSerialEventPort extends EventDrivenSerialPort {
    void openPort();
    void closePort();
    boolean isPortOpen();
    void addDisconnectHandler(String key, Runnable handler);
    void removeDisconnectHandler(String key);
}
