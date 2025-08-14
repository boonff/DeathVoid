import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AllowDeath
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import kotlin.math.log

object FelPumpkinSummon {
    val logger = LogManager.getLogger("FelPumpkinSummon")!!
    private val FEL_PUMPKIN_ID = Identifier("botania", "fel_pumpkin")
    private var felPumpkinBlock: Block? = null

    fun init() {
        // 预加载恶魔南瓜方块
        felPumpkinBlock = Registry.BLOCK.get(FEL_PUMPKIN_ID)
        if (felPumpkinBlock == Blocks.AIR) {
            logger.info("未找到恶魔南瓜方块! 请确保Botania模组已安装")
        } else
            logger.info("预加载恶魔南瓜方块成功")


        ServerPlayerEvents.ALLOW_DEATH.register(AllowDeath { player, _, _ ->
            logger.info("玩家死亡")
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
                            logger.info("检测到普通南瓜或雕刻南瓜")
                        }
                    }
                }
            }

            // 50%概率触发且存在南瓜
            if (pumpkinPositions.isNotEmpty() && roll()) {
                // 随机选择一个南瓜位置
                val targetPos = pumpkinPositions[world.random.nextInt(pumpkinPositions.size)]
                // 替换为恶魔南瓜
                world.setBlockState(targetPos, felPumpkinBlock!!.defaultState)
                logger.info("触发替换恶魔南瓜")
            }else{
                logger.info("未触发替换恶魔南瓜")
            }
            true
        })
    }

    private fun roll(percent: Float = 0.5f): Boolean {
        require(percent in 0.0..1.0) { "percent 必须在 0 到 100 之间" }
        return Math.random()  < percent
    }

}