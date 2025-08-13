package org.boon.deathvoid.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerDeathMixin {

    private static final Logger LOGGER = LogManager.getLogger("PlayerVoidDeathMixin");

    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        LOGGER.info("玩家死亡触发，原因: " + source.getName());

        // 检测虚空死亡
        if ("outOfWorld".equals(source.getName())) {
            // 给物品
            ItemStack stack = new ItemStack(Items.DIAMOND, 1);
            player.getInventory().insertStack(stack);
            player.sendMessage(Text.of("掉入虚空，获得了钻石！"), false);

        }
    }
}
