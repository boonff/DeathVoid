package org.boon.deathvoid.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 一个简单的 Mixin Hello World
 * 作用：在玩家跳跃时输出一条日志
 */
@Mixin(PlayerEntity.class)
public abstract class HelloWorldMixin {

    // 创建一个 Log4j2 Logger
    private static final Logger LOGGER = LogManager.getLogger("DeadVoid");

    @Inject(method = "jump", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        LOGGER.info("Hello from Mixin! 玩家跳了一下~");
        LOGGER.debug("这是调试信息（需要在 log4j2 配置里开启 DEBUG 才能看到）");
    }
}
