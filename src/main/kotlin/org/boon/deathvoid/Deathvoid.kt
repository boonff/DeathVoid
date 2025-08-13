package org.boon.deathvoid

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import org.boon.deathvoid.event.PlayerDeathListener

class Deathvoid : ModInitializer {

    override fun onInitialize() {
        PlayerDeathListener.register()
    }
}
