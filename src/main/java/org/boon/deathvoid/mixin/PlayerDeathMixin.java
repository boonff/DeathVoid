package org.boon.deathvoid.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerDeathMixin {

    private static  final String ClassName = PlayerDeathMixin.class.toString();
    private static final Logger LOGGER = LogManager.getLogger(ClassName);

    /**
     * 注入到玩家死亡方法前
     */
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo info) {
        Configurator.setLevel(ClassName, Level.DEBUG);
        LOGGER.info("玩家死亡！伤害来源: " + source.getName());
        // 如果想做更多操作，可以在这里调用其他事件处理
    }
}
