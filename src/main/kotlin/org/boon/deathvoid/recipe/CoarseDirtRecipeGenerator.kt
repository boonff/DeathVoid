package org.boon.deathvoid.recipe

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Consumer
import org.boon.deathvoid.Deathvoid

class CoarseDirtRecipeGenerator(fabricDataGenerator: FabricDataGenerator) 
    : net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider(fabricDataGenerator) {
    
    override fun generateRecipes(exporter: Consumer<RecipeJsonProvider>) {
        // 自定义砂土合成配方
        ShapedRecipeJsonBuilder.create(Items.COARSE_DIRT, 4)
            .pattern("AB")
            .pattern("BA")
            .input('A', Items.DIRT)
            .input('B', Registry.ITEM.get(Identifier("botania", "pebble")))
            .criterion("has_dirt", conditionsFromItem(Items.DIRT))
            .criterion("has_pebble", conditionsFromItem(Registry.ITEM.get(Identifier("botania", "pebble"))))
            .offerTo(exporter, Identifier(Deathvoid.MOD_ID, "coarse_dirt_from_botania"))
    }
}