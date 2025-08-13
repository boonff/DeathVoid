package org.boon.deathvoid.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
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

    // 标记玩家是否虚空死亡
    private boolean voidDeath = false;

    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        if ("outOfWorld".equals(source.getName())) {
            voidDeath = true; // 标记虚空死亡
            LOGGER.info("玩家掉入虚空，等待复活给予物品");
        }
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (voidDeath) {
            // 给物品
            ItemStack stack = new ItemStack(Items.DIAMOND, 1);
            player.getInventory().insertStack(stack);
            player.sendMessage(Text.of("掉入虚空，复活后获得了钻石！"), false);

            LOGGER.info("玩家 " + player.getName().getString() + " 虚空死亡复活，给予物品成功");

            voidDeath = false; // 清除标记
        }
    }
}
