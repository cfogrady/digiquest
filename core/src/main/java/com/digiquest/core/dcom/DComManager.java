package com.digiquest.core.dcom;

import com.github.cfogrady.dcom.serial.DComInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.delayedExecutor;

@RequiredArgsConstructor
@Slf4j
public class DComManager {
    private static final String DISCONNECT_KEY = "DComManagerDisconnectHandlerKey";

    private final DComPortFactory dComPortFactory;
    private DComSerialEventPort dComPort;

    public boolean isDComInitialized() {
        return dComPort == null ? false : dComPort.isPortOpen();
    }

    public boolean isDComLikeDeviceConnected() {
        return dComPortFactory.getConnectedPorts() > 0;
    }

    public boolean noDComLikeDeviceConnected() {
        return dComPortFactory.getConnectedPorts() == 0;
    }

    public void onDeviceConnected(Runnable deviceConnected) {
        log.info("Checking for serial device");
        if(isDComLikeDeviceConnected()) {
            log.info("Serial device found.");
            deviceConnected.run();
        } else {
            Executors.newScheduledThreadPool(1)
                    .schedule(() -> onDeviceConnected(deviceConnected), 250, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized CompletableFuture<DComSerialEventPort> getDComPort() {
        if(dComPort != null) {
            return CompletableFuture.completedFuture(dComPort);
        }
        DComSerialEventPort port = dComPortFactory.createDComPort().orElseThrow();
        port.openPort();
        DComInitializer dComInitializer = new DComInitializer(port);
        port.addDisconnectHandler(DISCONNECT_KEY, () -> {
            dComPort = null;
        });
        log.info("Initializing dcom");
        return dComInitializer.initialize().thenApply(v -> {
            log.info("D-com initialized");
            dComPort = port;
            return port;
        });
    }

    public void close() {
        if(dComPort != null) {
            dComPort.closePort();
        }
    }
}
