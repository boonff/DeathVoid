import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AllowDeath
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object VoidDeathHandler {
    // 标记玩家虚空死亡
    private val voidDeathMap: MutableMap<ServerPlayerEntity?, Boolean?> = HashMap<ServerPlayerEntity?, Boolean?>()

    fun init() {
        // 玩家死亡事件，标记虚空死亡
        ServerPlayerEvents.ALLOW_DEATH.register(AllowDeath { player: ServerPlayerEntity?, damageSource: DamageSource?, amount: Float ->
            if ("outOfWorld" == damageSource!!.getName()) {
                voidDeathMap.put(player, true)
            }
            true
        })

        // 玩家复活事件
        ServerPlayerEvents.AFTER_RESPAWN.register(AfterRespawn { oldPlayer: ServerPlayerEntity?, newPlayer: ServerPlayerEntity?, alive: Boolean ->
            if (voidDeathMap.getOrDefault(oldPlayer, false) == true) {
                val stack = ItemStack(Items.DIAMOND, 1)
                newPlayer!!.getInventory().insertStack(stack)
                newPlayer.sendMessage(Text.of("掉入虚空，复活后获得了钻石！"), false)
                voidDeathMap.remove(oldPlayer)
            }
        })
    }
}