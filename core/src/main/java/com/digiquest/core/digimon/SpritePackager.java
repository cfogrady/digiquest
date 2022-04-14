package com.digiquest.core.digimon;

import java.io.InputStream;
import java.io.OutputStream;

public interface SpritePackager {
    void addSpriteToStream(String spriteName, OutputStream outputStream);
    void importSpriteFromStream(String spriteName, InputStream inputStream);
}
