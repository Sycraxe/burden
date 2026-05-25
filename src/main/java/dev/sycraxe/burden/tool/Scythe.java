package dev.sycraxe.burden.tool;

import dev.sycraxe.burden.register.ModTag;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbility;

public class Scythe extends DiggerItem {

    public static final ItemAbility SCYTHE_HARVEST = ItemAbility.get("scythe_harvest");

    public Scythe(Tier tier, Properties properties) {
        super(tier, ModTag.MINEABLE_WITH_SCYTHE, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return toolAction == SCYTHE_HARVEST;
    }
}
