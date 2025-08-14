import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AllowDeath
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.registry.Registries
import org.slf4j.LoggerFactory

object FelPumpkinSummon {
    val LOGGER = LoggerFactory.getLogger("FelPumpkinSummon")
    private val FEL_PUMPKIN_ID = Identifier("botania", "felpumpkin")
    private var felPumpkinBlock: Block? = null

    fun init() {
        // 预加载恶魔南瓜方块
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) {
            felPumpkinBlock = Registries.BLOCK.get(FEL_PUMPKIN_ID)
            if (felPumpkinBlock == Blocks.AIR) {
                LOGGER.warn("未找到恶魔南瓜方块! 请确保Botania模组已安装")
            }
        }

        ServerPlayerEvents.ALLOW_DEATH.register(AllowDeath { player, _, _ ->
            if (felPumpkinBlock == null) return@AllowDeath true
            
            val world = player.world
            val centerPos = player.blockPos
            
            // 收集3×3×3区域内所有南瓜位置
            val pumpkinPositions = mutableListOf<BlockPos>()
            for (x in -1..1) {
                for (y in -1..1) {
                    for (z in -1..1) {
                        val checkPos = centerPos.add(x, y, z)
                        val state = world.getBlockState(checkPos)
                        
                        // 检测普通南瓜或雕刻南瓜
                        if (state.isOf(Blocks.PUMPKIN) || state.isOf(Blocks.CARVED_PUMPKIN)) {
                            pumpkinPositions.add(checkPos.toImmutable())
                        }
                    }
                }
            }
            
            // 50%概率触发且存在南瓜
            if (pumpkinPositions.isNotEmpty() && world.random.nextFloat() < 0.5f) {
                // 随机选择一个南瓜位置
                val targetPos = pumpkinPositions[world.random.nextInt(pumpkinPositions.size)]
                
                // 替换为恶魔南瓜
                world.setBlockState(targetPos, felPumpkinBlock!!.defaultState)
                
                // 可选：向服务器发送提示
                if (world.random.nextFloat() < 0.3f) { // 30%概率显示提示
                    world.server?.playerManager?.broadcast(
                        Text.translatable("event.felpumpkin.summon", player.name), 
                        false
                    )
                }
            }
            true
        })
    }
}