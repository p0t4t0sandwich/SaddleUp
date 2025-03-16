/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
 */
package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric.steering;

import dev.neuralnexus.saddleup.v1_21_1.fabric.Helper;
import dev.neuralnexus.saddleup.v1_21_1.fabric.SafeItemBasedSteering;
import dev.neuralnexus.saddleup.v1_21_1.fabric.SteeringBridge;

import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = {Bee.class, Cow.class})
@Implements(
        @Interface(
                iface = ItemSteerable.class,
                prefix = "steerable$",
                remap = Interface.Remap.NONE))
public abstract class ItemSteerableImplementMixin implements SteeringBridge {
    @Unique private final ItemBasedSteering saddleup$steering = new SafeItemBasedSteering();

    @Override
    public ItemBasedSteering bridge$steering() {
        return this.saddleup$steering;
    }

    @Intrinsic
    public boolean steerable$boost() {
        return this.bridge$steering().boost(Helper.asEntity(this).getRandom());
    }
}
