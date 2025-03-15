package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class)
public abstract class EntityMixin {
    // @spotless:off
    @Shadow @Nullable public abstract LivingEntity shadow$getControllingPassenger();
    @Shadow public abstract boolean shadow$isEffectiveAi();
    // @spotless:on

    @Inject(method = "isControlledByLocalInstance", at = @At("HEAD"), cancellable = true)
    private void saddleup$isControlledByLocalInstance(CallbackInfoReturnable<Boolean> cir) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }

        LivingEntity entity = this.shadow$getControllingPassenger();
        if (entity instanceof Player player) {
            cir.setReturnValue(player.isLocalPlayer()
                    || FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT);
        } else {
            cir.setReturnValue(this.shadow$isEffectiveAi());
        }
    }
}
