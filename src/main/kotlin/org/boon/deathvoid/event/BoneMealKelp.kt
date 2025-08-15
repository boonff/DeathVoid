package org.boon.deathvoid.event

import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BoneMealItem
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager
import kotlin.random.Random

object BoneMealKelp {
    val logger = LogManager.getLogger("BoneMealKelp")!!

    private const val REPLACE_CHANCE = 0.5

    fun init() {
        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val stack = player.getStackInHand(hand)
            if (stack.item != Items.BONE_MEAL) return@UseBlockCallback ActionResult.PASS
            val blockPos = hitResult.blockPos

            if (world.isClient)
                return@UseBlockCallback ActionResult.PASS
            if (trigger(world, blockPos)) {
                logger.info("玩家 ${player.name.asString()} 使用骨粉在位置 $blockPos")
                ripening(world, blockPos, Random.nextInt(30, 45))
                lightSound(world, blockPos)
                stack.decrement(1) // 消耗骨粉
                logger.info("消耗骨粉1，当前骨粉数量：${stack.count}")
                return@UseBlockCallback ActionResult.SUCCESS
            } else
                return@UseBlockCallback ActionResult.PASS


        })
    }

    //限定骨粉的触发状态,防止影响骨粉在的原版逻辑
    private fun trigger(world: World, blockPos: BlockPos): Boolean {
        val blockState = world.getBlockState(blockPos)
        val blockUpState = world.getBlockState(blockPos.up())
        return (blockState.material.isSolid && blockUpState.isOf(Blocks.WATER))
    }

    private fun placeBlock(world: World, pos: BlockPos, block: Block) {
        world.setBlockState(pos, block.defaultState)
    }

    private fun ripening(world: World, pos: BlockPos, attempts: Int) {
        repeat(attempts) {
            // 随机偏移，类似原版骨粉效果
            val dx = Random.nextInt(-3, 4)
            val dy = Random.nextInt(-3, 4)
            val targetPos = pos.add(dx, 1, dy)
            val blockState = world.getBlockState(targetPos)
            val below = world.getBlockState(targetPos.down())

            //防止在空中生成海带和水草
            if (below.material.isSolid)
                when (Random.nextInt(3)) {
                    0 -> {
                        if (blockState.isOf(Blocks.WATER))
                            world.setBlockState(targetPos, Blocks.KELP.defaultState)
                    }

                    1 -> {
                        if (blockState.isOf(Blocks.WATER))
                            world.setBlockState(targetPos, Blocks.SEAGRASS.defaultState)
                    }

                    2 -> {
                        if (blockState.isOf(Blocks.WATER))
                            world.setBlockState(targetPos, Blocks.TALL_SEAGRASS.defaultState)
                    }
                }
        }
    }

    private fun lightSound(world: World, blockPos: BlockPos) {
        world.syncWorldEvent(2005, blockPos, 0) // 播放骨粉粒子
        world.playSound(
            null,
            blockPos,
            SoundEvents.ITEM_BONE_MEAL_USE,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f
        )
    }

}
