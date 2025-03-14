package dev.neuralnexus.saddleup.mixin.v1_21_1.neoforge;

import dev.neuralnexus.saddleup.v1_21_1.neoforge.HelperMethods;

import dev.neuralnexus.saddleup.v1_21_1.neoforge.SteeringBridge;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {Bee.class, Cow.class}, remap = false)
@Implements(@Interface(iface = Saddleable.class, prefix = "saddleable$", remap = Interface.Remap.NONE))
public abstract class SaddleableImplementMixin extends LivingEntity implements SteeringBridge {
    protected SaddleableImplementMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Intrinsic
    public boolean saddleable$isSaddleable() {
        return HelperMethods.isAlive(this) && !HelperMethods.isBaby(this);
    }

    @SuppressWarnings("resource")
    @Intrinsic
    public void saddleable$equipSaddle(@NotNull ItemStack itemStack, @Nullable SoundSource sound) {
        this.bridge$steering().setSaddle(true);
        if (sound != null) {
            HelperMethods.level(this).playSound(null, this, SoundEvents.PIG_SADDLE, sound, 0.5F, 1.0F);
        }
    }

    @Intrinsic
    public boolean saddleable$isSaddled() {
         return this.bridge$steering().hasSaddle();
    }
}
