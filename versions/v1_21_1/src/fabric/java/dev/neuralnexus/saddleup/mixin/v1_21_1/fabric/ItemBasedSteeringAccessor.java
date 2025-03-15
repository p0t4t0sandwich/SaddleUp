package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric;

import net.minecraft.world.entity.ItemBasedSteering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemBasedSteering.class)
public interface ItemBasedSteeringAccessor {
    @Accessor("boostTime")
    int accessor$boostTime();

    @Accessor("boostTime")
    void accessor$setBoostTime(int boostTime);

    @Accessor("boosting")
    boolean accessor$boosting();

    @Accessor("boosting")
    void accessor$setBoosting(boolean boosting);
}
