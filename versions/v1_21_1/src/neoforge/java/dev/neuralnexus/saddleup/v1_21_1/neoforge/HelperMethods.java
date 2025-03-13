package dev.neuralnexus.saddleup.v1_21_1.neoforge;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HelperMethods {
    public static boolean isAlive(Object entity) {
        if (entity instanceof LivingEntity living) {
            return living.isAlive();
        }
        return false;
    }

    public static boolean isBaby(Object entity) {
        if (entity instanceof AgeableMob living) {
            return living.isBaby();
        }
        return false;
    }

    public static Level level(Object entity) {
        if (entity instanceof Entity e) {
            return e.level();
        }
        return null;
    }
}
