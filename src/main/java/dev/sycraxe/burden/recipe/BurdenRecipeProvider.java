package dev.sycraxe.burden.recipe;

import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.backpack.BackpackBlockEntity;
import dev.sycraxe.burden.register.ModItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BurdenRecipeProvider extends RecipeProvider {
    public BurdenRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        List<Item> wools = List.of(
            Items.BLACK_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.CYAN_WOOL,
            Items.GRAY_WOOL,
            Items.GREEN_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.LIME_WOOL,
            Items.MAGENTA_WOOL,
            Items.ORANGE_WOOL,
            Items.PINK_WOOL,
            Items.PURPLE_WOOL,
            Items.RED_WOOL,
            Items.YELLOW_WOOL,
            Items.WHITE_WOOL
        );
        List<DyeColor> colors = List.of(
            DyeColor.BLACK,
            DyeColor.BLUE,
            DyeColor.BROWN,
            DyeColor.CYAN,
            DyeColor.GRAY,
            DyeColor.GREEN,
            DyeColor.LIGHT_BLUE,
            DyeColor.LIGHT_GRAY,
            DyeColor.LIME,
            DyeColor.MAGENTA,
            DyeColor.ORANGE,
            DyeColor.PINK,
            DyeColor.PURPLE,
            DyeColor.RED,
            DyeColor.YELLOW,
            DyeColor.WHITE
        );
        for (int i = 0; i < wools.size(); ++i) {
            backpackFromWool(output, colors.get(i), wools.get(i));
        }
    }

    private void backpackFromWool(RecipeOutput output, DyeColor color, Item wool) {
        ItemStack stack = new ItemStack(ModItem.BACKPACK.get());
        if (color.getTextureDiffuseColor() != BackpackBlockEntity.getDefaultColor()) {
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor(), true));
        }
        ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, stack)
                .define('W', wool)
                .define('S', Items.STRING)
                .define('L', Items.LEATHER)
                .define('C', Items.CHEST)
                .pattern("SLS")
                .pattern("WCW")
                .pattern("SLS")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output, Burden.MOD_ID + ":" + color.getName() + "_backpack");
    }
}