package dev.sycraxe.burden.gui.backpack;

import dev.sycraxe.burden.Burden;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BackpackScreen extends AbstractContainerScreen<AbstractBackpackMenu> {
    private static final ResourceLocation BACKPACK =
            ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "textures/gui/container/backpack.png");
    private static final ResourceLocation INVENTORY =
            ResourceLocation.fromNamespaceAndPath(Burden.MOD_ID, "textures/gui/container/inventory.png");

    public BackpackScreen(AbstractBackpackMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 248;
        this.imageHeight = 191;
        this.titleLabelX += 36;
        this.titleLabelY = 8;
        this.inventoryLabelX += 36;
        this.inventoryLabelY = 92;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKPACK, this.leftPos + 36, this.topPos, 0, 0, 176, 81);
        graphics.blit(INVENTORY, this.leftPos, this.topPos + 77, 0, 0, this.imageWidth, 108);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 13942436, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

}
