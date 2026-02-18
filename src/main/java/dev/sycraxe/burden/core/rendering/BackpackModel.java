package dev.sycraxe.burden.core.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sycraxe.burden.Burden;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BackpackModel extends EntityModel<AbstractClientPlayer> {
	private static final ResourceLocation BACKPACK_ENTITY_TEXTURE =
			ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "textures/model/backpack.png");
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(
					ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "backpack"),
					"main"
			);
	private final ModelPart backpack;

	public BackpackModel(ModelPart root) {
		this.backpack = root.getChild("backpack");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild(
				"backpack",
				CubeListBuilder
						.create()
						.texOffs(0, 0)
						.addBox(
								0.0F, 0.0F, 0.0F,
								8.0F, 8.0F, 4.0F,
								new CubeDeformation(0.0F)
						),
				PartPose.offset(-4.0F, 0.0F, 2.0F)
		);

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.setup(player);
	}

	public void setup(AbstractClientPlayer player) {
		if (player.isCrouching()) {
			this.backpack.xRot = 0.5F;
			this.backpack.y = 3.2F;
		} else {
			this.backpack.xRot = 0.0F;
			this.backpack.y = 0.0F;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		this.backpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(BACKPACK_ENTITY_TEXTURE));
		this.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
	}

	public void setupAndRender(AbstractClientPlayer player, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		this.setup(player);
		this.render(poseStack, buffer, packedLight);
	}
}