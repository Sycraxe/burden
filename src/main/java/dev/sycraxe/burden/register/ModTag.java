package dev.sycraxe.burden.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTag {
    public static final TagKey<Block> MINEABLE_WITH_SCYTHE = commonBlockTag("mineable/scythe");

    private static TagKey<Block> commonBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
