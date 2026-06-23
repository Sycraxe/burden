package dev.sycraxe.burden.recipe;

import dev.sycraxe.burden.Burden;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class BurdenRecipeProvider extends RecipeProvider {
    public BurdenRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        SpecialRecipeBuilder.special(BackpackColoringRecipe::new).save(output, Burden.MOD_ID + ":backpack_coloring");
    }
}