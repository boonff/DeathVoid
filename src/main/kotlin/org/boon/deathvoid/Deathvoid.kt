package org.boon.deathvoid

import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import org.boon.deathvoid.event.PlayerDeathListener

class Deathvoid : ModInitializer {

    override fun onInitialize() {
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) {
            PlayerDeathListener.register()
            VoidDeathHandler.init()
        }
    }
}
