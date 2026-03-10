package dev.sycraxe.burden.backpack;

import dev.sycraxe.burden.inventory.InventoryHandlerSlot;
import dev.sycraxe.burden.register.ModInventoryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public abstract class BackpackContext {
    public abstract ContextType getType();

    public void toBuffer(FriendlyByteBuf buffer) {
        this.getType().toBuffer(buffer);
        this.addToBuffer(buffer);
    }

    public static BackpackContext fromBuffer(FriendlyByteBuf buffer) {
        ContextType type = ContextType.fromBuffer(buffer);
        return switch (type) {
            case ITEM_BACKPACK -> Item.fromItemBuffer(buffer);
            case BLOCK_BACKPACK -> Block.fromBlockBuffer(buffer);
        };
    }

    public abstract void addToBuffer(FriendlyByteBuf buffer);

    public enum ContextType {
        ITEM_BACKPACK,
        BLOCK_BACKPACK;

        public void toBuffer(FriendlyByteBuf buffer) {
            buffer.writeShort(this.ordinal());
        }

        public static ContextType fromBuffer(FriendlyByteBuf buffer) {
            int value = buffer.readShort();
            return ContextType.values()[0 <= value && value < ContextType.values().length ? value : 0];
        }
    }

    public static class Item extends BackpackContext {
        private final InventoryHandlerSlot slot;

        public Item(InventoryHandlerSlot slot) {
            this.slot = slot;
        }

        public ContextType getType() {
            return ContextType.ITEM_BACKPACK;
        }

        public InventoryHandlerSlot getHandlerSlot() {
            return this.slot;
        }

        public static BackpackContext fromItemBuffer(FriendlyByteBuf buffer) {
            return new Item(new InventoryHandlerSlot(ModInventoryHandler.getHandler(buffer.readUtf()), buffer.readInt()));
        }

        public void addToBuffer(FriendlyByteBuf buffer) {
            buffer.writeUtf(ModInventoryHandler.getHandlerId(this.getHandlerSlot().handler()));
            buffer.writeInt(this.getHandlerSlot().index());
        };
    }

    public static class Block extends BackpackContext {
        private final BlockPos pos;

        public Block(BlockPos pos) {
            this.pos = pos;
        }

        public ContextType getType() {
            return ContextType.BLOCK_BACKPACK;
        }

        public BlockPos getBackpackPosition() {
            return this.pos;
        }

        public static BackpackContext fromBlockBuffer(FriendlyByteBuf buffer) {
            return new Block(BlockPos.of(buffer.readLong()));
        }

        public void addToBuffer(FriendlyByteBuf buffer) {
            buffer.writeLong(this.pos.asLong());
        };
    }
}
