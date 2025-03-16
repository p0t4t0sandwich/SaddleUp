/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
 */
package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric.saddle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

        if (this.shadow$getControllingPassenger() instanceof Player) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(this.shadow$isEffectiveAi());
        }
    }
}
