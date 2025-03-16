/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
 */
package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric.saddle;

import dev.neuralnexus.saddleup.v1_21_1.fabric.SteeringBridge;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SteeringBridge {
    // @spotless:off
    @Shadow public float yBodyRot;
    @Shadow public float yHeadRot;
    @Shadow public abstract double shadow$getAttributeValue(Holder<Attribute> holder);
    // @spotless:on

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void saddleup$dropEquipment(CallbackInfo ci) {
        Object self = this;
        if (self instanceof Pig || self instanceof Strider || self instanceof AbstractHorse) {
            return;
        }
        if (self instanceof Saddleable saddleable && saddleable.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
            saddleable.equipSaddle(ItemStack.EMPTY, null);
        }
    }

    @SuppressWarnings({"ConstantValue", "resource"})
    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    private void saddleup$onSyncedDataUpdated(EntityDataAccessor<?> accessor, CallbackInfo ci) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }
        if (this.bridge$steering() != null && this.level().isClientSide) {
            this.bridge$steering().onSynced();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saddleup$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }
        this.bridge$steering().addAdditionalSaveData(compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void saddleup$readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }
        this.bridge$steering().readAdditionalSaveData(compoundTag);
    }

    @Inject(method = "tickRidden", at = @At("HEAD"))
    private void saddleup$tickRidden(Player player, Vec3 vec3, CallbackInfo ci) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.bridge$steering().tickBoost();
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void saddleup$getRiddenInput(
            Player player, Vec3 vec3, CallbackInfoReturnable<Vec3> cir) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }
        cir.setReturnValue(new Vec3(0.0F, 0.0F, 1.0F));
    }

    @Inject(method = "getRiddenSpeed", at = @At("HEAD"), cancellable = true)
    private void saddleup$getRiddenSpeed(CallbackInfoReturnable<Float> cir) {
        Object self = this;
        if (!(self instanceof Saddleable)
                || self instanceof Pig
                || self instanceof Strider
                || self instanceof AbstractHorse) {
            return;
        }
        cir.setReturnValue(
                (float)
                        (this.shadow$getAttributeValue(Attributes.MOVEMENT_SPEED)
                                * 0.225
                                * (double) this.bridge$steering().boostFactor()));
    }
}
