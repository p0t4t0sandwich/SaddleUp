package dev.neuralnexus.saddleup.mixin.v1_21_1.neoforge;

import dev.neuralnexus.saddleup.v1_21_1.neoforge.HelperMethods;

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
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(value = {Bee.class, Cow.class}, remap = false)
@Implements(@Interface(iface = Saddleable.class, prefix = "saddleable$", remap = Interface.Remap.NONE))
public abstract class SaddleableImplementMixin extends LivingEntity {
//    @Unique
//    private static EntityDataAccessor<Boolean> DATA_SADDLE_ID;
//    @Unique
//    private static EntityDataAccessor<Integer> DATA_BOOST_TIME;
//    @Unique
//    private ItemBasedSteering saddleable$steering;

    // Saddleable Interface
    @Unique private boolean saddleup$isSaddled = false;

    protected SaddleableImplementMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public boolean saddleable$isSaddleable() {
        return HelperMethods.isAlive(this) && !HelperMethods.isBaby(this);
    }

    @SuppressWarnings("resource")
    public void saddleable$equipSaddle(@NotNull ItemStack itemStack, @Nullable SoundSource sound) {
        // this.steering.setSaddle(true);
        this.saddleup$isSaddled = true;
        if (sound != null) {
            HelperMethods.level(this).playSound(null, this, SoundEvents.PIG_SADDLE, sound, 0.5F, 1.0F);
        }
    }

    public boolean saddleable$isSaddled() {
        // this.steering.hasSaddle();
        return this.saddleup$isSaddled;
    }
}
