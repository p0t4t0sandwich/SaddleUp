package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric;

import dev.neuralnexus.saddleup.v1_21_1.fabric.SteeringBridge;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
    @Shadow @Nullable public abstract AttributeInstance shadow$getAttribute(Holder<Attribute> holder);
    @Shadow public abstract double shadow$getAttributeValue(Holder<Attribute> holder);
    // @spotless:on

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> holder);

    @Shadow protected abstract boolean isAffectedByFluids();

    @Shadow public abstract boolean canStandOnFluid(FluidState fluidState);

    @Shadow protected abstract float getWaterSlowDown();

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Holder<Attribute> holder);

    @Shadow public abstract double getAttributeValue(Holder<Attribute> holder);

    @Shadow public abstract float getSpeed();

    @Shadow public abstract boolean onClimbable();

    @Shadow public abstract Vec3 getFluidFallingAdjustedMovement(double d, boolean bl, Vec3 vec3);

    @Shadow public abstract Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3, float f);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(Holder<MobEffect> holder);

    @Shadow public abstract boolean shouldDiscardFriction();

    @Shadow public abstract void calculateEntityAnimation(boolean bl);

    @Shadow public abstract boolean isFallFlying();

    @Shadow protected abstract SoundEvent getFallDamageSound(int i);

    @Shadow protected abstract Vec3 getRiddenInput(Player player, Vec3 vec3);

    @Shadow protected abstract void tickRidden(Player player, Vec3 vec3);

    @Shadow public abstract void setSpeed(float f);

    @Shadow protected abstract float getRiddenSpeed(Player player);

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

//    /**
//     * @author p0t40t0sandwich
//     * @reason TODO: This is a test, yell at me if this is forgotten
//     */
//    @Overwrite
//    public void travelRidden(Player player, Vec3 vec3) {
//        Vec3 vec32 = this.getRiddenInput(player, vec3);
//        this.tickRidden(player, vec32);
//        System.out.println("TRAVEL_RIDDEN");
//        if (this.isControlledByLocalInstance()) {
//            System.out.println("TRAVEL_BY_LOCAL_INSTANCE");
//            this.setSpeed(this.getRiddenSpeed(player));
//            this.travel(vec32);
//        } else {
//            System.out.println("TRAVEL_NOT_BY_LOCAL_INSTANCE");
//            this.calculateEntityAnimation(false);
//            this.setDeltaMovement(Vec3.ZERO);
//            this.tryCheckInsideBlocks();
//        }
//
//    }
//
//    /**
//     * @author p0t40t0sandwich
//     * @reason TODO: This is a test, yell at me if this is forgotten
//     */
//    @Overwrite
//    public void travel(Vec3 vec3) {
//        Object self = this;
//        if (!(self instanceof Saddleable)
//                || self instanceof Pig
//                || self instanceof Strider
//                || self instanceof AbstractHorse) {
//            this.move(MoverType.SELF, this.getDeltaMovement());
//            return;
//        }
//
////        System.out.println("TRAVEL");
//        if (this.isControlledByLocalInstance()) {
//            System.out.println("CONTROLLED_BY_LOCAL_INSTANCE");
//            double d = this.getGravity();
//            boolean bl = this.getDeltaMovement().y <= (double)0.0F;
//            if (bl && this.hasEffect(MobEffects.SLOW_FALLING)) {
//                d = Math.min(d, 0.01);
//            }
//
//            FluidState fluidState = this.level().getFluidState(this.blockPosition());
//            if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidState)) {
//                double e = this.getY();
//                float f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
//                float g = 0.02F;
//                float h = (float)this.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
//                if (!this.onGround()) {
//                    h *= 0.5F;
//                }
//
//                if (h > 0.0F) {
//                    f += (0.54600006F - f) * h;
//                    g += (this.getSpeed() - g) * h;
//                }
//
//                if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
//                    f = 0.96F;
//                }
//
//                this.moveRelative(g, vec3);
//                this.move(MoverType.SELF, this.getDeltaMovement());
//                Vec3 vec32 = this.getDeltaMovement();
//                if (this.horizontalCollision && this.onClimbable()) {
//                    vec32 = new Vec3(vec32.x, 0.2, vec32.z);
//                }
//
//                this.setDeltaMovement(vec32.multiply((double)f, (double)0.8F, (double)f));
//                Vec3 vec33 = this.getFluidFallingAdjustedMovement(d, bl, this.getDeltaMovement());
//                this.setDeltaMovement(vec33);
//                if (this.horizontalCollision && this.isFree(vec33.x, vec33.y + (double)0.6F - this.getY() + e, vec33.z)) {
//                    this.setDeltaMovement(vec33.x, (double)0.3F, vec33.z);
//                }
//            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidState)) {
//                double e = this.getY();
//                this.moveRelative(0.02F, vec3);
//                this.move(MoverType.SELF, this.getDeltaMovement());
//                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
//                    this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.5F, (double)0.8F, (double)0.5F));
//                    Vec3 vec34 = this.getFluidFallingAdjustedMovement(d, bl, this.getDeltaMovement());
//                    this.setDeltaMovement(vec34);
//                } else {
//                    this.setDeltaMovement(this.getDeltaMovement().scale((double)0.5F));
//                }
//
//                if (d != (double)0.0F) {
//                    this.setDeltaMovement(this.getDeltaMovement().add((double)0.0F, -d / (double)4.0F, (double)0.0F));
//                }
//
//                Vec3 vec34 = this.getDeltaMovement();
//                if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + (double)0.6F - this.getY() + e, vec34.z)) {
//                    this.setDeltaMovement(vec34.x, (double)0.3F, vec34.z);
//                }
//            } else if (this.isFallFlying()) {
//                this.checkSlowFallDistance();
//                Vec3 vec35 = this.getDeltaMovement();
//                Vec3 vec36 = this.getLookAngle();
//                float f = this.getXRot() * ((float)Math.PI / 180F);
//                double i = Math.sqrt(vec36.x * vec36.x + vec36.z * vec36.z);
//                double j = vec35.horizontalDistance();
//                double k = vec36.length();
//                double l = Math.cos((double)f);
//                l = l * l * Math.min((double)1.0F, k / 0.4);
//                vec35 = this.getDeltaMovement().add((double)0.0F, d * ((double)-1.0F + l * (double)0.75F), (double)0.0F);
//                if (vec35.y < (double)0.0F && i > (double)0.0F) {
//                    double m = vec35.y * -0.1 * l;
//                    vec35 = vec35.add(vec36.x * m / i, m, vec36.z * m / i);
//                }
//
//                if (f < 0.0F && i > (double)0.0F) {
//                    double m = j * (double)(-Mth.sin(f)) * 0.04;
//                    vec35 = vec35.add(-vec36.x * m / i, m * 3.2, -vec36.z * m / i);
//                }
//
//                if (i > (double)0.0F) {
//                    vec35 = vec35.add((vec36.x / i * j - vec35.x) * 0.1, (double)0.0F, (vec36.z / i * j - vec35.z) * 0.1);
//                }
//
//                this.setDeltaMovement(vec35.multiply((double)0.99F, (double)0.98F, (double)0.99F));
//                this.move(MoverType.SELF, this.getDeltaMovement());
//                if (this.horizontalCollision && !this.level().isClientSide) {
//                    double m = this.getDeltaMovement().horizontalDistance();
//                    double n = j - m;
//                    float o = (float)(n * (double)10.0F - (double)3.0F);
//                    if (o > 0.0F) {
//                        this.playSound(this.getFallDamageSound((int)o), 1.0F, 1.0F);
//                        this.hurt(this.damageSources().flyIntoWall(), o);
//                    }
//                }
//
//                if (this.onGround() && !this.level().isClientSide) {
//                    this.setSharedFlag(7, false);
//                }
//            } else {
//                BlockPos blockPos = this.getBlockPosBelowThatAffectsMyMovement();
//                float p = this.level().getBlockState(blockPos).getBlock().getFriction();
//                float f = this.onGround() ? p * 0.91F : 0.91F;
//                Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);
////                System.out.println(vec3.x + " " + vec3.y + " " + vec3.z);
//                System.out.println(vec37.x + " " + vec37.y + " " + vec37.z);
//                double q = vec37.y;
//                if (this.hasEffect(MobEffects.LEVITATION)) {
//                    q += (0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec37.y) * 0.2;
//                } else if (this.level().isClientSide && !this.level().hasChunkAt(blockPos)) {
//                    if (this.getY() > (double)this.level().getMinBuildHeight()) {
//                        q = -0.1;
//                    } else {
//                        q = (double)0.0F;
//                    }
//                } else {
//                    q -= d;
//                }
//
//                if (this.shouldDiscardFriction()) {
//                    this.setDeltaMovement(vec37.x, q, vec37.z);
//                } else {
//                    this.setDeltaMovement(vec37.x * (double)f, this instanceof FlyingAnimal ? q * (double)f : q * (double)0.98F, vec37.z * (double)f);
//                }
//            }
//        }
//
//        this.calculateEntityAnimation(this instanceof FlyingAnimal);
//    }

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
//        if (HelperMethods.getDATA_BOOST_TIME(this.getClass()).equals(accessor) && this.level().isClientSide) {
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
        //
        this.setDeltaMovement(vec3);
        // System.out.println(vec37.x + " " + vec37.y + " " + vec37.z);
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void saddleup$getRiddenInput(Player player, Vec3 vec3, CallbackInfoReturnable<Vec3> cir) {
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
        cir.setReturnValue((float)
                (this.shadow$getAttributeValue(Attributes.MOVEMENT_SPEED)
                        * 0.225 * (double) this.bridge$steering().boostFactor()));
    }
}
