package dev.sycraxe.burden.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sycraxe.burden.Burden;
import dev.sycraxe.burden.AllItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BackpackLayerRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation BACKPACK_LOCATION =
            ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "textures/model/backpack.png");
    private final BackpackModel model;

    public BackpackLayerRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BackpackModel(modelSet.bakeLayer(BackpackModel.LAYER_LOCATION));
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
        if (livingEntity.getInventory().getItem(38).is(AllItem.BACKPACK)) {
            this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.model.renderToBuffer(
                    poseStack,
                    buffer.getBuffer(RenderType.entitySolid(BACKPACK_LOCATION)),
                    packedLight,
                    LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F)
            );
        }
    }
}
