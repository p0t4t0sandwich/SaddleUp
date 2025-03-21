/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
 */
package dev.neuralnexus.saddleup.v1_21_1.fabric;

import dev.neuralnexus.saddleup.mixin.v1_21_1.fabric.steering.ItemBasedSteeringAccessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemBasedSteering;

import org.jetbrains.annotations.NotNull;

public class SafeItemBasedSteering extends ItemBasedSteering {
    private static final int MIN_BOOST_TIME = 140;
    private static final int MAX_BOOST_TIME = 700;
    private int boostTime;
    private boolean hasSaddle;

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
            this.boostTime = random.nextInt(MAX_BOOST_TIME + MIN_BOOST_TIME + 1) + MIN_BOOST_TIME;
            return true;
        }
    }

    @Override
    public void tickBoost() {
        if (this.accessor().accessor$boosting() && this.incBoostTime() > this.boostTime) {
            this.accessor().accessor$setBoosting(false);
        }
    }

    @Override
    public float boostFactor() {
        return this.accessor().accessor$boosting()
                ? 1.0F
                        + 1.15F
                                * Mth.sin(
                                        (float) this.accessor().accessor$boostTime()
                                                / (float) this.boostTime
                                                * (float) Math.PI)
                : 1.0F;
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
        this.hasSaddle = hasSaddle;
    }

    @Override
    public boolean hasSaddle() {
        return this.hasSaddle;
    }
}
