/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
 */
package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric.saddle;

import dev.neuralnexus.saddleup.v1_21_1.fabric.Helper;
import dev.neuralnexus.saddleup.v1_21_1.fabric.SteeringBridge;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {Bee.class, Cow.class})
@Implements(
        @Interface(iface = Saddleable.class, prefix = "saddleable$", remap = Interface.Remap.NONE))
public abstract class SaddleableImplementMixin implements SteeringBridge {
    @Intrinsic
    public boolean saddleable$isSaddleable() {
        return Helper.isAlive(this) && !Helper.isBaby(this);
    }

    @SuppressWarnings("resource")
    @Intrinsic
    public void saddleable$equipSaddle(@NotNull ItemStack itemStack, @Nullable SoundSource sound) {
        Entity self = (Entity) (Object) this;
        this.bridge$steering().setSaddle(true);
        if (sound != null) {
            self.level().playSound(null, self, SoundEvents.PIG_SADDLE, sound, 0.5F, 1.0F);
        }
    }

    @Intrinsic
    public boolean saddleable$isSaddled() {
        return this.bridge$steering().hasSaddle();
    }
}
