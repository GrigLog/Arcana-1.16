package arcana.mixin_interfaces;

import arcana.common.blocks.tiles.QuantumChestInventory;

public interface IQuantumInventory {
    QuantumChestInventory arcana$get();

    void arcana$set(QuantumChestInventory inv);
}
