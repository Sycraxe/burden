package dev.sycraxe.burden.backpack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.sycraxe.burden.register.ModItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BackpackLayerRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public BackpackLayerRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
    }

    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        ItemStack stack = livingEntity.getInventory().getItem(38);
        if (stack.is(ModItem.BACKPACK)) {
            backpackRender(this.getParentModel(), poseStack, buffer, packedLight, stack, livingEntity);
        }
    }

    public static void backpackRender(HumanoidModel<?> model, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack stack, Player player) {
        poseStack.pushPose();

        model.body.translateAndRotate(poseStack);

        poseStack.translate(0.0F, 0.12F, 0.28F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        BakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getModel(stack, player.level(), player, player.getId());
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedModel);

        poseStack.popPose();
    }
}
