package com.digiquest.core.util;

import java.io.File;

public interface FileManager {
    File getAppHome();
    void createRelativeDirectoryIfMissing(String relativeDirectory);
    File getFileRelativeToAppPath(String relativePath);
}
