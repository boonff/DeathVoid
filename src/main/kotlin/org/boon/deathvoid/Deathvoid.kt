package org.boon.deathvoid

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

class Deathvoid : ModInitializer {

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register {
           // VoidDeathHandler.init()
            FelPumpkinSummon.init()
        }

    }
}
