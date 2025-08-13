package org.boon.deathvoid.mixin;

import net.minecraft.entity.LivingEntity;
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

@Mixin(LivingEntity.class)
public abstract class PlayerDeathMixin {

    private static final Logger LOGGER = LogManager.getLogger(PlayerDeathMixin.class);

    static {
        System.out.println("PlayerDeathMixin类已加载！");
        LogManager.getLogger("MixinDebug").info("PlayerDeathMixin已初始化");
    }

    @Inject(
            method = "onDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"),
            cancellable = false
    )
    private void onEntityDeath(DamageSource source, CallbackInfo info) {
        LivingEntity self = (LivingEntity)(Object)this; // 强制转换到被混入的实体
        if (self instanceof PlayerEntity) {
            LOGGER.info("玩家死亡！伤害来源: " + source.getName());
        }
    }
}

