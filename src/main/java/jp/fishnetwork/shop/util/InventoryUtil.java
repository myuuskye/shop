package jp.fishnetwork.shop.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InventoryUtil {

    public static boolean canAddItem(PlayerInventory inventory, ItemStack item) {
        int freeSlots = 0;
        for(ItemStack slotItem: inventory.getStorageContents()) {
            if(slotItem == null || slotItem.getType() == Material.AIR) {
                freeSlots += item.getMaxStackSize();
            }else if(slotItem.isSimilar(item)) {
                freeSlots += Math.max(0, slotItem.getMaxStackSize() - slotItem.getAmount());
            }
        }
        return freeSlots >= item.getAmount();
    }

    public static int getItemAllCount(ItemStack item, Inventory inventory) {
        int count = 0;
        for(ItemStack content: inventory.getContents()) {
            if(content == null || !content.isSimilar(item)) continue;
            count += content.getAmount();
        }
        return count;
    }

}
