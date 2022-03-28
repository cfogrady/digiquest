package com.digiquest.core;

import java.util.concurrent.CompletableFuture;

public interface SpriteLoader {
    public CompletableFuture<Void> loadSpriteAsync(String spriteName);
}
