package dev.neuralnexus.saddleup.mixin.v1_21_1.neoforge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, remap = false)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void dropEquipment(CallbackInfo ci) {
        Object self = this;
        if (self instanceof Pig || self instanceof Strider || self instanceof AbstractHorse) {
            return;
        }
        if (((Object) this) instanceof Saddleable saddleable && saddleable.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
            saddleable.equipSaddle(ItemStack.EMPTY, null);
        }
    }
}
