package net.nty.renderoffhand.mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Invoker("renderPlayerArm")
    abstract void invokeRenderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light,
                                        float equippedProgress, float swingProgress, HumanoidArm arm);

    @Inject(method = "renderArmWithItem", at = @At("TAIL"))
    private void renderOffhandArmWhenEmpty(AbstractClientPlayer player, float f, float g,
                                           InteractionHand hand, float swingProgress,
                                           ItemStack stack, float equippedProgress,
                                           PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                                           int light, CallbackInfo ci) {

        if (hand == InteractionHand.OFF_HAND && stack.isEmpty()) {
            HumanoidArm offArm = player.getMainArm().getOpposite();
            invokeRenderPlayerArm(poseStack, submitNodeCollector, light, equippedProgress, swingProgress, offArm);
        }
    }
}
