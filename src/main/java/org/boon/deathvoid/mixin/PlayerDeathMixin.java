package org.boon.deathvoid.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PlayerDeathMixin {
    @Unique
    private static final String ClassName = "PlayerDeathMixin";
    @Unique
    private static final Logger LOGGER = LogManager.getLogger(ClassName);

    static {
        LogManager.getLogger("MixinDebug").info("PlayerDeathMixin已初始化");
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        LOGGER.info("任何实体死亡触发: " + ((LivingEntity)(Object)this).getName().getString());
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        LOGGER.info("玩家死亡！伤害来源: " + source.getName());
    }


}

