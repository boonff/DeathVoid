package org.boon.deathvoid

import VoidDeathHandler
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.LoggerFactory

class Deathvoid : ModInitializer {
    companion object {
        val LOGGER = LoggerFactory.getLogger("Main")
    }

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register {
            VoidDeathHandler.init()
        }

    }
}
