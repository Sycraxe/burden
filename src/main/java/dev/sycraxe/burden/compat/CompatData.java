package dev.sycraxe.burden.compat;

import net.neoforged.fml.ModList;

public record CompatData(String modId) {
    public boolean isLoaded() {
        return ModList.get().isLoaded(this.modId);
    }
}