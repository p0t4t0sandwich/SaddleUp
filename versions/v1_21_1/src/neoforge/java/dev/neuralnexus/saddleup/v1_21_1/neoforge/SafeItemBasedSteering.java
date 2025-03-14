package dev.neuralnexus.saddleup.v1_21_1.neoforge;

import dev.neuralnexus.saddleup.mixin.v1_21_1.neoforge.ItemBasedSteeringAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemBasedSteering;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

public class SafeItemBasedSteering extends ItemBasedSteering {
    private static final int MIN_BOOST_TIME = 140;
    private static final int MAX_BOOST_TIME = 700;
    private int boostTimeAccessor;
    private boolean hasSaddleAccessor;
    private boolean boosting;
    private int boostTime;

    public SafeItemBasedSteering() {
        super(null, null, null);
    }

    private ItemBasedSteeringAccessor accessor() {
        return (ItemBasedSteeringAccessor) this;
    }

    private int incBoostTime() {
        int boostTime = this.accessor().accessor$boostTime() + 1;
        this.accessor().accessor$setBoostTime(boostTime);
        return boostTime;
    }

    @Override
    public void onSynced() {
        this.accessor().accessor$setBoosting(true);
        this.accessor().accessor$setBoostTime(0);
    }

    @Override
    public boolean boost(@NotNull RandomSource random) {
        if (this.accessor().accessor$boosting()) {
            return false;
        } else {
            this.accessor().accessor$setBoosting(true);
            this.accessor().accessor$setBoostTime(0);
            this.boostTimeAccessor = random.nextInt(MAX_BOOST_TIME + MIN_BOOST_TIME + 1) + MIN_BOOST_TIME;
            return true;
        }
    }

    @Override
    public void tickBoost() {
        if (this.accessor().accessor$boosting() && this.incBoostTime() > this.boostTimeTotal()) {
            this.accessor().accessor$setBoosting(false);
        }
    }

    @Override
    public float boostFactor() {
        return this.accessor().accessor$boosting() ? 1.0F + 1.15F * Mth.sin((float)this.accessor().accessor$boostTime() / (float)this.boostTimeTotal() * (float)Math.PI) : 1.0F;
    }

    private int boostTimeTotal() {
        return this.boostTimeAccessor;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putBoolean("Saddle", this.hasSaddle());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setSaddle(compoundTag.getBoolean("Saddle"));
    }

    @Override
    public void setSaddle(boolean hasSaddle) {
        this.hasSaddleAccessor = hasSaddle;
    }

    @Override
    public boolean hasSaddle() {
        return this.hasSaddleAccessor;
    }
}
