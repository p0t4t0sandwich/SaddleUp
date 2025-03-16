/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/SaddleUp/blob/dev/LICENSE">MIT</a>
 */
package dev.neuralnexus.saddleup.mixin.v1_21_1.fabric.client.renderer;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends Entity, M extends EntityModel<T>> {
    @Shadow protected abstract boolean addLayer(RenderLayer<T, M> renderLayer);

    @SuppressWarnings({"ConstantValue", "rawtypes", "unchecked"})
    @Inject(method = "<init>", at = @At("TAIL"))
    private void saddleup$init(EntityRendererProvider.Context context, EntityModel<T> entityModel, float f, CallbackInfo ci) {
        EntityModel<T> newModelLayer = null;
//        if (entityModel instanceof BeeModel) {
//            new BeeModel<>(context.bakeLayer(ModelLayers.PIG_SADDLE));
//        } else
        if (entityModel instanceof CowModel) {
            new CowModel<>(context.bakeLayer(ModelLayers.PIG_SADDLE));
        }
        if (newModelLayer != null) {
            this.addLayer(
                    new SaddleLayer(
                            (RenderLayerParent) this,
                            newModelLayer,
                            ResourceLocation.withDefaultNamespace(
                                    "textures/entity/pig/pig_saddle.png")));
        }
    }
}
