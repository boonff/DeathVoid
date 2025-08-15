package org.boon.deathvoid.event

import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.networking.v1.PlayerLookup.world
import net.minecraft.block.Blocks
import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager
import kotlin.random.Random

object BoneMealMushroom {

    private val logger = LogManager.getLogger("BoneMealMushroom")

    fun init() {
        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val stack = player.getStackInHand(hand)
            if (stack.item != Items.BONE_MEAL) return@UseBlockCallback ActionResult.PASS

            if (world.isClient) return@UseBlockCallback ActionResult.PASS

            val blockPos = hitResult.blockPos

            if (tryGenerateMushrooms(world, blockPos))
                return@UseBlockCallback ActionResult.SUCCESS
            else
                return@UseBlockCallback ActionResult.PASS

        })
    }


    fun isDarkAndCovered(world: World, pos: BlockPos): Boolean {
        val above = pos.up() // 上方一格

        // 确保在服务端世界调用
        if (world.isClient) {
            logger.info("客户端世界，不判断亮度")
            return false
        }

        val stateAbove = world.getBlockState(above)
        val isCovered = !world.isSkyVisible(above)
        val lightLevel = world.getLightLevel(above) // 0~15

        // 输出日志
        logger.info("检查位置: $pos, 上方方块: ${stateAbove.block}, 是否覆盖天空: $isCovered, 光照等级: $lightLevel")

        return isCovered && lightLevel < 7
    }


    private fun tryGenerateMushrooms(world: World, pos: BlockPos): Boolean {
        // 在 3x3 范围生成 1~3 个蘑菇
        val mushroomCount = Random.nextInt(1, 4)
        var isGenMushrooms = false
        repeat(mushroomCount) {
            val offsetX = Random.nextInt(-1, 2)
            val offsetZ = Random.nextInt(-1, 2)
            val targetPos = pos.add(offsetX, 1, offsetZ)

            if (canPlaceMushroom(world, targetPos)) {
                val mushroom = if (Random.nextBoolean()) Blocks.BROWN_MUSHROOM else Blocks.RED_MUSHROOM
                world.setBlockState(targetPos, mushroom.defaultState)
                isGenMushrooms = true
                logger.info("生成了一个 ${mushroom.translationKey} 在 $targetPos")
            }
        }
        return isGenMushrooms
    }

    private fun canPlaceMushroom(world: World, pos: BlockPos): Boolean {
        val state = world.getBlockState(pos)
        val aboveState = world.getBlockState(pos.up())

        //选中方块是实体方块且上方是空气
        return if (state.material.isSolid && !aboveState.isAir) false
        else
            isDarkAndCovered(world, pos)


    }

}
