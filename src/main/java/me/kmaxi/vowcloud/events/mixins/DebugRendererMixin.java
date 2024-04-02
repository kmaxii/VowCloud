package me.kmaxi.vowcloud.events.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import me.kmaxi.vowcloud.Audio.RaycastRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onDrawBlockOutline(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, double x, double y, double z, CallbackInfo ci) {
        RaycastRenderer.renderRays(poseStack, bufferSource, x, y, z);
    }

}
