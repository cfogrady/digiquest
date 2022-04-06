package com.digiquest.desktop.dcom;

import com.digiquest.core.dcom.DComSerialEventPort;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DComPort implements DComSerialEventPort, SerialPortDataListener {
    private final SerialPort serialPort;
    private final Map<String, Consumer<byte[]>> listeners;
    private final Map<String, Runnable> disconnectHandlers;

    public DComPort(SerialPort serialPort) {
        this.serialPort = serialPort;
        serialPort.addDataListener(this);
        this.listeners = new HashMap<>();
        disconnectHandlers = new HashMap<>();
    }

    @Override
    public void addListener(String key, Consumer<byte[]> listener) {
        listeners.put(key, listener);
    }

    @Override
    public void removeListener(String s) {
        listeners.remove(s);
    }

    @Override
    public void addDisconnectHandler(String key, Runnable handler) {
        disconnectHandlers.put(key, handler);
    }

    @Override
    public void removeDisconnectHandler(String key) {
        disconnectHandlers.remove(key);
    }

    @Override
    public void writeBytes(byte[] bytes) {
        int writtenBytes = serialPort.writeBytes(bytes, bytes.length);
        if(writtenBytes != bytes.length) {
            log.error("Was only able to write {} bytes to port!", writtenBytes);
        }
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if(event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
            log.info("Port disconnected!");
            serialPort.removeDataListener();
            serialPort.closePort();
            for(Runnable handler : disconnectHandlers.values()) {
                handler.run();
            }
        } else if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            byte[] newData = new byte[serialPort.bytesAvailable()];
            int readBytes = serialPort.readBytes(newData , newData.length);
            if(readBytes != newData.length) {
                log.error("Only read {} bytes, when there should be {} available!", readBytes, newData.length);
            }
            for(Consumer<byte[]> consumer : listeners.values()) {
                consumer.accept(newData);
            }
        } else {
            log.warn("Received unsupported event: {}!", Integer.toBinaryString(event.getEventType()));
        }
    }

    @Override
    public void openPort() {
        serialPort.openPort();
    }

    @Override
    public void closePort() {
        serialPort.removeDataListener();
        serialPort.closePort();
    }

    @Override
    public boolean isPortOpen() {
        return serialPort.isOpen();
    }
}
