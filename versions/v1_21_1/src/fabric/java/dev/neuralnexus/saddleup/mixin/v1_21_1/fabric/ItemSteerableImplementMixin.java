package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric;

import dev.neuralnexus.saddleup.v1_21_1.fabric.HelperMethods;
import dev.neuralnexus.saddleup.v1_21_1.fabric.SafeItemBasedSteering;
import dev.neuralnexus.saddleup.v1_21_1.fabric.SteeringBridge;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = {Bee.class, Cow.class})
@Implements(@Interface(iface = ItemSteerable.class, prefix = "steerable$", remap = Interface.Remap.NONE))
public abstract class ItemSteerableImplementMixin extends LivingEntity implements SteeringBridge {
    protected ItemSteerableImplementMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Unique
    private final ItemBasedSteering saddleup$steering = new SafeItemBasedSteering();
//    private final ItemBasedSteering saddleup$steering = new ItemBasedSteering(
//            this.entityData,
//            HelperMethods.getDATA_BOOST_TIME(this.getClass()),
//            HelperMethods.getDATA_SADDLE_ID(this.getClass())
//    );

    @Override
    public ItemBasedSteering bridge$steering() {
        return this.saddleup$steering;
    }

    @Intrinsic
    public boolean steerable$boost() {
        return this.bridge$steering().boost(this.getRandom());
    }
}
