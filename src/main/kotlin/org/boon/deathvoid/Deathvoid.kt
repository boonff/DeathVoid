package org.boon.deathvoid

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.boon.deathvoid.event.BoneMealKelp
import org.boon.deathvoid.event.BoneMealMushroom
import org.boon.deathvoid.event.FelPumpkinSummon
import org.boon.deathvoid.event.VoidDeathHandler
import org.boon.deathvoid.recipe.CoarseDirtRecipeGenerator
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

class Deathvoid : ModInitializer {

    companion object{
        const val MOD_ID = "deathvoid"
    }

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register {
            VoidDeathHandler.init()
            FelPumpkinSummon.init()
            BoneMealKelp.init()
            BoneMealMushroom.init()
        }
    }


}
