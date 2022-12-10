package jp.fishnetwork.shop.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

@UtilityClass
public class ItemUtil {

    @SuppressWarnings("unchecked")
    public static ItemStack deserialize(Map<String, Object> data) {
        ItemStack item = new ItemStack(Material.valueOf(data.get("material").toString()));
        ItemMeta meta = item.getItemMeta();
        if(item.getType() == Material.AIR) return item;
        if(data.containsKey("amount")) item.setAmount((int)data.get("amount"));
        if(data.containsKey("damage")) ((Damageable)item).setDamage((int)data.get("damage"));
        if(data.containsKey("display_name")) meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', data.get("display_name").toString())));
        if(data.containsKey("enchantments")) ((Map<String, Integer>)data.get("enchantments")).forEach((enchantment, level) -> {
            meta.addEnchant(Enchantment.getByKey(NamespacedKey.fromString(enchantment)), level, true);
        });
        if(data.containsKey("lores")) {
            meta.lore(((List<String>)data.get("lores")).stream().map(lore -> Component.text(ChatColor.translateAlternateColorCodes('&', lore))).collect(Collectors.toList()));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setDisplayName(ItemStack item, Component displayName) {
        item.editMeta(meta -> meta.displayName(displayName));
        return item;
    }

    public static ItemStack addLore(ItemStack item, Component ...loresArray) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lores = meta.hasLore() ? meta.lore() : new ArrayList<>();
        for(Component lore: loresArray) lores.add(lore);
        meta.lore(lores);
        item.setItemMeta(meta);
        return item;
    }

}
