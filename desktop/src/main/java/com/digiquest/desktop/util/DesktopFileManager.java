package com.digiquest.desktop.util;

import com.digiquest.core.util.FileManager;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;

public class DesktopFileManager implements FileManager {
    public static final String APP_NAME = "digiquest";

    private final String appPath;

    public DesktopFileManager() {
        appPath = AppDirsFactory.getInstance().getUserDataDir(APP_NAME, null, null);
    }


    @Override
    public File getAppHome() {
        return new File(appPath);
    }

    public String getAppHomeString() {
        return appPath;
    }

    @Override
    public void createRelativeDirectoryIfMissing(String relativeDirectory) {
        String directory = appPath + "/" + relativeDirectory;
        File directoryFile = new File(directory);
        if(!directoryFile.exists()) {
            directoryFile.mkdir();
        }
    }

    @Override
    public File getFileRelativeToAppPath(String relativePath) {
        return new File(appPath + "/" + relativePath);
    }
}
