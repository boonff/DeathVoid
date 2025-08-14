package org.boon.deathvoid.event

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AllowDeath
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.LogManager

object VoidDeathHandler {
    // 标记玩家虚空死亡
    val logger = LogManager.getLogger("VoidDeathHandler")!!
    private val voidDeathMap: MutableMap<ServerPlayerEntity?, Boolean?> = HashMap()

    fun init() {
        // 玩家死亡事件，标记虚空死亡
        ServerPlayerEvents.ALLOW_DEATH.register(AllowDeath { player, damageSource, amount->
            if ("outOfWorld" == damageSource!!.getName()) {
                voidDeathMap[player] = true
            }
            true
        })

        // 玩家复活事件
        ServerPlayerEvents.AFTER_RESPAWN.register(AfterRespawn { oldPlayer, newPlayer, alive ->
            if (voidDeathMap.getOrDefault(oldPlayer, false) == true) {
                val stack = ItemStack(Items.ENDER_PEARL, 1)
                voidDeathMap.remove(oldPlayer)
                logger.info("掉入虚空，复活后获得了末影珍珠！")
            }
        })
    }
}