package dev.sycraxe.burden.compatibility;

import net.neoforged.fml.ModList;

public record CompatibiltyData(String modId) {
    public boolean isLoaded() {
        return ModList.get().isLoaded(this.modId);
    }
}