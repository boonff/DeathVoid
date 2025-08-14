package org.boon.deathvoid.event

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.LogManager

object PlayerDeathListener {
    private val logger = LogManager.getLogger("PlayerDeathListener")

    fun register() {

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register { world, entity, killedEntity ->
            if (killedEntity is ServerPlayerEntity) {
                val killerName = entity.name?.string ?: "环境"
                val playerName = killedEntity.name.string
                logger.info("玩家 $playerName 死亡，击杀者: $killerName")
            }
        }
    }
}
