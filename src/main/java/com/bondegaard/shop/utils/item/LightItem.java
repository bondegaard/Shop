package com.bondegaard.shop.utils.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class LightItem {

    private final int typeId;

    private final byte data;

    public static LightItem from(ItemStack itemStack) {
        return new LightItem(itemStack.getTypeId(), (byte) itemStack.getDurability());
    }

    public boolean matches(Block block) {
        return this.matches(block.getTypeId(), block.getData());
    }

    public boolean matches(ItemStack item) {
        return this.matches(item.getTypeId(), item.getData().getData());
    }

    public boolean matches(int typeId, byte data) {
        if (this.typeId != typeId) return false;

        if (this.data == -1) return true;

        return data == this.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.typeId, this.data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        LightItem lightItem = (LightItem) o;
        return this.data == lightItem.data && this.typeId == lightItem.typeId;
    }

}
