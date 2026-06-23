package dev.sycraxe.burden.recipe;

import dev.sycraxe.burden.register.ModItem;
import dev.sycraxe.burden.register.ModRecipeSerializer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public class BackpackColoringRecipe extends CustomRecipe {

    public BackpackColoringRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int backpacks = 0;
        int dyes = 0;

        for(int slot = 0; slot < input.size(); slot++) {
            ItemStack itemstack = input.getItem(slot);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(ModItem.BACKPACK)) {
                    ++backpacks;
                }
                else if (itemstack.is(Tags.Items.DYES)) {
                    ++dyes;
                }
                else {
                    return false;
                }
            }
        }

        return backpacks == 1 && dyes == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack backpack = ItemStack.EMPTY;
        DyeColor color = DyeColor.WHITE;

        for(int slot = 0; slot < input.size(); slot++) {
            ItemStack itemstack = input.getItem(slot);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(ModItem.BACKPACK)) {
                    backpack = itemstack;
                }
                else {
                    DyeColor tmp = DyeColor.getColor(itemstack);
                    if (tmp != null) {
                        color = tmp;
                    }
                }
            }
        }

        ItemStack result = backpack.copy();
        result.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor(), false));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.BACKPACK_COLORING.get();
    }
}
