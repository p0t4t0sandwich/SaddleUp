package dev.neuralnexus.saddleup.v1_21_1.neoforge;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.concurrent.ConcurrentHashMap;

public class HelperMethods {
    private static final ConcurrentHashMap<Class<?>, EntityDataAccessor<Boolean>> entityData$DATA_SADDLE_ID = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, EntityDataAccessor<Integer>> entityData$DATA_BOOST_TIME = new ConcurrentHashMap<>();

    public static EntityDataAccessor<Boolean> getDATA_SADDLE_ID(Class<? extends SyncedDataHolder> clazz) {
        if (!entityData$DATA_SADDLE_ID.containsKey(clazz)) {
            entityData$DATA_SADDLE_ID.put(clazz, SynchedEntityData.defineId(clazz, EntityDataSerializers.BOOLEAN));
        }
        return entityData$DATA_SADDLE_ID.get(clazz);
    }

    public static EntityDataAccessor<Integer> getDATA_BOOST_TIME(Class<? extends SyncedDataHolder> clazz) {
        if (!entityData$DATA_BOOST_TIME.containsKey(clazz)) {
            entityData$DATA_BOOST_TIME.put(clazz, SynchedEntityData.defineId(clazz, EntityDataSerializers.INT));
        }
        return entityData$DATA_BOOST_TIME.get(clazz);
    }

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
