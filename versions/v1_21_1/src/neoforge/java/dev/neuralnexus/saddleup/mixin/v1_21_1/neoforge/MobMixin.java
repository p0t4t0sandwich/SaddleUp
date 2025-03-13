package dev.neuralnexus.saddleup.mixin.v1_21_1.neoforge;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Mob.class, remap = false)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    // Inject into the end of Animal#mobInteract
    // Logic adapted from Pig#mobInteract
    @SuppressWarnings("resource")
    @Inject(method = "mobInteract", at = @At("TAIL"), cancellable = true)
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (((Object) this) instanceof Saddleable saddleable) {
            if (saddleable.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
                if (!this.level().isClientSide) {
                    player.startRiding(this);
                }
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            } else {
                ItemStack itemstack = player.getItemInHand(hand);
                if (itemstack.is(Items.SADDLE)) {
                    cir.setReturnValue(itemstack.interactLivingEntity(player, this, hand));
                }
            }
        }
    }
}
